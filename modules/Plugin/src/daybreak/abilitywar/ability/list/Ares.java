package daybreak.abilitywar.ability.list;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.ability.SubscribeEvent;
import daybreak.abilitywar.ability.decorator.ActiveHandler;
import daybreak.abilitywar.config.ability.AbilitySettings.SettingObject;
import daybreak.abilitywar.game.AbstractGame.Participant;
import daybreak.abilitywar.utils.base.Formatter;
import daybreak.abilitywar.utils.base.concurrent.TimeUnit;
import daybreak.abilitywar.utils.base.math.LocationUtil;
import daybreak.abilitywar.utils.library.ParticleLib;
import daybreak.abilitywar.utils.library.SoundLib;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AbilityManifest(name = "Ares", rank = Rank.A, species = Species.GOD, explain = {
		"God of War,Ares.",
		"Right-click iron ingot will dash in front to give damage to nearby entities,",
		"and pushing all damaged entities. $[CooldownConfig]"
})
public class Ares extends AbilityBase implements ActiveHandler {

	public static final SettingObject<Integer> DamageConfig = new SettingObject<Integer>(Ares.class, "DamagePercent", 75,
			"# 스킬 대미지 (단위: 백분율)") {

		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}

	};

	public static final SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(Ares.class, "Cooldown", 60,
			"# 쿨타임") {

		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}

		@Override
		public String toString() {
			return Formatter.formatCooldown(getValue());
		}

	};

	public static final SettingObject<Boolean> DashConfig = new SettingObject<Boolean>(Ares.class, "DashIntoTheAir", true,
			"# true로 설정하면 아레스 능력 사용 시 공중으로 돌진 할 수 있습니다.") {

		@Override
		public boolean Condition(Boolean value) {
			return true;
		}

	};

	public Ares(Participant participant) {
		super(participant);
	}

	private final CooldownTimer cooldownTimer = new CooldownTimer(CooldownConfig.getValue());

	private final DurationTimer skill = new DurationTimer(20, cooldownTimer) {

		private Set<Damageable> attacked;

		@Override
		protected void onDurationStart() {
			attacked = new HashSet<>();
			Collection<Player> nearby = LocationUtil.getNearbyPlayers(getPlayer().getLocation(), 10, 10);
			SoundLib.ENTITY_PLAYER_ATTACK_SWEEP.playSound(nearby);
		}

		@Override
		public void onDurationProcess(int seconds) {
			Player p = getPlayer();

			ParticleLib.LAVA.spawnParticle(p.getLocation(), 4, 4, 4, 40);

			if (DashConfig.getValue()) {
				p.setVelocity(p.getVelocity().add(p.getLocation().getDirection().multiply(0.7)));
			} else {
				p.setVelocity(p.getVelocity().add(p.getLocation().getDirection().multiply(0.7).setY(0)));
			}

			for (Damageable damageable : LocationUtil.getNearbyDamageableEntities(p, 4, 4)) {
				double damage = (damageable.getHealth() / 100) * DamageConfig.getValue();
				if (!attacked.contains(damageable)) {
					damageable.damage(damage, p);
					attacked.add(damageable);
					SoundLib.BLOCK_ANVIL_LAND.playSound(p, 0.5f, 1);
				} else {
					damageable.damage(damage / 5, p);
				}

				damageable.setVelocity(p.getLocation().toVector().subtract(damageable.getLocation().toVector()).multiply(-1).setY(1));
			}
		}

	}.setPeriod(TimeUnit.TICKS, 1);

	@Override
	public boolean ActiveSkill(Material materialType, ClickType clickType) {
		if (materialType.equals(Material.IRON_INGOT)) {
			if (clickType.equals(ClickType.RIGHT_CLICK)) {
				if (!skill.isDuration() && !cooldownTimer.isCooldown()) {
					skill.start();

					return true;
				}
			}
		}

		return false;
	}

	@SubscribeEvent
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getEntity().equals(getPlayer()) && e.getCause().equals(DamageCause.FALL) && skill.isDuration()) {
			e.setCancelled(true);
		}
	}

}
