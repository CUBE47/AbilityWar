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
import daybreak.abilitywar.utils.library.ParticleLib;
import daybreak.abilitywar.utils.library.PotionEffects;
import daybreak.abilitywar.utils.library.SoundLib;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@AbilityManifest(name = "Berserker", rank = Rank.B, species = Species.HUMAN, explain = {
		"Empower for 5 seconds after right-clicking iron ingot. $[CooldownConfig]",
		"Empowered attacks will do $[StrengthConfig]times of normal damage, after attacking",
		"you cannot do for $[DebuffConfig]seconds.",
		"Cooldown is reduced by half if you did not attack when activated."
})
public class Berserker extends AbilityBase implements ActiveHandler {

	public static final SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(Berserker.class, "Cooldown", 80,
			"# Cooldown") {

		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}

		@Override
		public String toString() {
			return Formatter.formatCooldown(getValue());
		}

	};

	public static final SettingObject<Double> StrengthConfig = new SettingObject<Double>(Berserker.class, "Strength", 2.5,
			"# Increased damage") {

		@Override
		public boolean Condition(Double value) {
			return value >= 2;
		}

	};

	public static final SettingObject<Integer> DebuffConfig = new SettingObject<Integer>(Berserker.class, "Debuff", 5,
			"# 능력 사용 후 디버프를 받는 시간",
			"# 단위 : 초") {

		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}

	};

	public Berserker(Participant participant) {
		super(participant);
	}

	private final CooldownTimer cooldownTimer = new CooldownTimer(CooldownConfig.getValue());

	private class BerserkerTimer extends DurationTimer {

		private BerserkerTimer() {
			super(5);
		}

		@Override
		protected void onDurationProcess(int count) {
		}

		@Override
		protected void onDurationEnd() {
			cooldownTimer.start();
			cooldownTimer.setCount(cooldownTimer.getMaximumCount() / 2);
		}

		public boolean stop() {
			boolean bool = super.stop(true);
			cooldownTimer.start();
			return bool;
		}

	}

	private final BerserkerTimer berserkerTimer = new BerserkerTimer();

	@Override
	public boolean ActiveSkill(Material materialType, ClickType clickType) {
		if (materialType.equals(Material.IRON_INGOT) && clickType == ClickType.RIGHT_CLICK) {
			if (!berserkerTimer.isDuration() && !cooldownTimer.isCooldown()) {
				berserkerTimer.start();
				return true;
			}
		}

		return false;
	}

	private final double strength = StrengthConfig.getValue();
	private final int debuff = DebuffConfig.getValue();

	@SubscribeEvent
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager().equals(getPlayer()) && !e.isCancelled()) {
			if (berserkerTimer.isRunning()) {
				berserkerTimer.stop();
				e.setDamage(e.getDamage() * strength);
				SoundLib.ENTITY_PLAYER_ATTACK_SWEEP.playSound(getPlayer());
				ParticleLib.SWEEP_ATTACK.spawnParticle(e.getEntity().getLocation(), 1, 1, 1, 5);
				PotionEffects.WEAKNESS.addPotionEffect(getPlayer(), debuff * 20, 1, true);
			}
		}
	}

}
