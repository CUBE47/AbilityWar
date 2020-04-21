package daybreak.abilitywar.ability.list;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.ability.decorator.ActiveHandler;
import daybreak.abilitywar.config.ability.AbilitySettings.SettingObject;
import daybreak.abilitywar.game.AbstractGame.Participant;
import daybreak.abilitywar.utils.base.Formatter;
import daybreak.abilitywar.utils.base.concurrent.TimeUnit;
import daybreak.abilitywar.utils.base.math.LocationUtil;
import daybreak.abilitywar.utils.library.ParticleLib;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;

@AbilityManifest(name = "Chaos", rank = Rank.S, species = Species.GOD, explain = {
		"God of Begin,Chaos.",
		"Pull all entities into pitch darkness for 5 seconds",
		"when righ-click iron ingot. $[CooldownConfig]"
})
public class Chaos extends AbilityBase implements ActiveHandler {

	public static final SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(Chaos.class, "Cooldown", 80,
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

	public static final SettingObject<Integer> DurationConfig = new SettingObject<Integer>(Chaos.class, "Duration", 5,
			"# 능력 지속 시간") {

		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}

	};

	public static final SettingObject<Integer> DistanceConfig = new SettingObject<Integer>(Chaos.class, "Distance", 5,
			"# 거리 설정") {

		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}

	};

	public Chaos(Participant participant) {
		super(participant);
	}

	private final CooldownTimer cooldownTimer = new CooldownTimer(CooldownConfig.getValue());
	private final DurationTimer skill = new DurationTimer(DurationConfig.getValue() * 20, cooldownTimer) {

		private Location center;
		private int distance;

		@Override
		public void onDurationStart() {
			this.center = getPlayer().getLocation();
			this.distance = DistanceConfig.getValue();
		}

		@Override
		public void onDurationProcess(int seconds) {
			ParticleLib.SMOKE_LARGE.spawnParticle(center, 0, 0, 0, 100);
			for (Damageable damageable : LocationUtil.getNearbyEntities(Damageable.class, getPlayer(), distance, distance)) {
				damageable.damage(1);
				damageable.setVelocity(center.toVector().subtract(damageable.getLocation().toVector()).multiply(0.7));
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

}
