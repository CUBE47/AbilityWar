package daybreak.abilitywar.ability.list;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.ability.SubscribeEvent;
import daybreak.abilitywar.ability.decorator.TargetHandler;
import daybreak.abilitywar.config.ability.AbilitySettings.SettingObject;
import daybreak.abilitywar.game.games.mode.AbstractGame.Participant;
import daybreak.abilitywar.utils.base.concurrent.TimeUnit;
import daybreak.abilitywar.utils.library.ParticleLib.RGB;
import daybreak.abilitywar.utils.math.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.util.ArrayList;
import java.util.List;

@AbilityManifest(Name = "좀비", Rank = Rank.A, Species = Species.UNDEAD)
public class Zombie extends AbilityBase implements TargetHandler {

	private static final SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(Zombie.class, "Cooldown", 100, "# 쿨타임") {

		@Override
		public boolean Condition(Integer arg0) {
			return arg0 >= 0;
		}

	};

	private static final SettingObject<Integer> DurationConfig = new SettingObject<Integer>(Zombie.class, "Duration", 15, "# 지속시간") {

		@Override
		public boolean Condition(Integer arg0) {
			return arg0 >= 1;
		}

	};

	private static final SettingObject<Double> RadiusConfig = new SettingObject<Double>(Zombie.class, "Radius", 10.0, "# 스킬 반경") {

		@Override
		public boolean Condition(Double arg0) {
			return arg0 >= 1;
		}

	};

	private static final SettingObject<Integer> ZombieCountConfig = new SettingObject<Integer>(Zombie.class, "ZombieCount", 15, "# 생성할 좀비 수") {

		@Override
		public boolean Condition(Integer arg0) {
			return arg0 >= 1;
		}

	};

	public Zombie(Participant participant) {
		super(participant,
				"좀비가 당신을 타게팅하지 않습니다.",
				"다른 플레이어를 철괴로 우클릭하면 주변 " + RadiusConfig.getValue() + "칸 안에 속도가 점차 줄어드는",
				ZombieCountConfig.getValue() + "마리의 " + ChatColor.DARK_PURPLE + "좀비" + ChatColor.WHITE + "를 소환합니다.",
				"소환된 좀비들은 불에 타지 않고, 대미지를 받지 않으며, 대상 플레이어를 공격합니다.");
	}

	@Override
	public boolean ActiveSkill(Material materialType, ClickType clickType) {
		return false;
	}

	private static final RGB DARK_RED = RGB.of(61, 6, 1);

	@SubscribeEvent
	private void onMobTarget(EntityTargetLivingEntityEvent e) {
		if (getPlayer().equals(e.getTarget()) && e.getEntityType().equals(EntityType.ZOMBIE)) {
			e.setCancelled(true);
		}
	}

	private final double radius = RadiusConfig.getValue();
	private final int zombieCount = ZombieCountConfig.getValue();
	private final CooldownTimer cooldownTimer = new CooldownTimer(100);
	private Player target;
	private final DurationTimer skill = new DurationTimer(DurationConfig.getValue() * 20, cooldownTimer) {
		List<org.bukkit.entity.Zombie> zombies;

		@Override
		protected void onDurationStart() {
			zombies = new ArrayList<>(zombieCount);
			for (Location location : LocationUtil.getRandomLocations(getPlayer().getLocation(), radius, zombieCount)) {
				org.bukkit.entity.Zombie zombie = getPlayer().getWorld().spawn(location, org.bukkit.entity.Zombie.class);
				zombie.setGlowing(true);
				zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.75);
				zombies.add(zombie);
			}
		}

		@Override
		protected void onDurationProcess(int count) {
			for (org.bukkit.entity.Zombie zombie : zombies) {
				zombie.setInvulnerable(true);
				zombie.setFireTicks(0);
				zombie.setTarget(target);
				AttributeInstance movement = zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
				if (movement.getValue() > 0.3) {
					movement.setBaseValue(Math.max(0.3, movement.getValue() - 0.0025));
				}
			}
		}

		@Override
		protected void onDurationEnd() {
			for (org.bukkit.entity.Zombie zombie : zombies) {
				zombie.remove();
			}
			target = null;
		}

		@Override
		protected void onDurationSilentEnd() {
			for (org.bukkit.entity.Zombie zombie : zombies) {
				zombie.remove();
			}
			target = null;
		}
	}.setPeriod(TimeUnit.TICKS, 1);

	@Override
	public void TargetSkill(Material materialType, LivingEntity entity) {
		if (materialType == Material.IRON_INGOT && entity instanceof Player && !skill.isDuration() && !cooldownTimer.isCooldown()) {
			this.target = (Player) entity;
			skill.start();
		}
	}

}