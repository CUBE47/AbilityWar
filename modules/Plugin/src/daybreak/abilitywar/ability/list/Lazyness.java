package daybreak.abilitywar.ability.list;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.SubscribeEvent;
import daybreak.abilitywar.game.AbstractGame;
import daybreak.abilitywar.utils.library.SoundLib;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

@AbilityManifest(name = "지금의 일은 나중의 나에게", rank = AbilityManifest.Rank.A, species = AbilityManifest.Species.HUMAN, explain = {
		"지금 받을 대미지를 3초 뒤의 나에게 미룹니다.",
		"넉백을 무시합니다."
})
public class Lazyness extends AbilityBase {

	public Lazyness(AbstractGame.Participant participant) {
		super(participant);
	}

	@SubscribeEvent
	private void onEntityDamage(EntityDamageEvent e) {
		if (e.getEntity().equals(getPlayer())) {
			new DamageTimer(e.getFinalDamage());
			getPlayer().setNoDamageTicks(getPlayer().getMaximumNoDamageTicks());
			e.setCancelled(true);
		}
	}

	@SubscribeEvent
	private void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		onEntityDamage(e);
	}

	@SubscribeEvent
	private void onEntityDamageByBlock(EntityDamageByBlockEvent e) {
		onEntityDamage(e);
	}

	private class DamageTimer extends Timer {

		private final double damage;

		private DamageTimer(double damage) {
			super(3);
			this.damage = damage;
			start();
		}

		@Override
		protected void run(int count) {
		}

		@Override
		protected void onEnd() {
			SoundLib.ENTITY_PLAYER_HURT.playSound(getPlayer());
			if (!getPlayer().isDead()) {
				getPlayer().setHealth(Math.max(getPlayer().getHealth() - damage, 0.0));
			}
		}

	}

}
