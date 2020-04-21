package daybreak.abilitywar.ability.list;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.ability.Scheduled;
import daybreak.abilitywar.ability.SubscribeEvent;
import daybreak.abilitywar.ability.event.AbilityDestroyEvent;
import daybreak.abilitywar.game.AbstractGame.Participant;
import daybreak.abilitywar.utils.library.MaterialX;
import daybreak.abilitywar.utils.library.PotionEffects;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.LinkedList;

@AbilityManifest(name = "Devil's Boots", rank = Rank.B, species = Species.OTHERS, explain = {
		"Moves swiftly,causing flame on the spot where player was.Player is immune to fire."
})
public class DevilBoots extends AbilityBase {

	public DevilBoots(Participant participant) {
		super(participant);
	}

	@Scheduled
	private final Timer speed = new Timer() {
		@Override
		protected void run(int count) {
			PotionEffects.SPEED.addPotionEffect(getPlayer(), 20, 1, true);
		}
	};

	private final LinkedList<Block> blocks = new LinkedList<>();

	@SubscribeEvent
	public void onPlayerMove(PlayerMoveEvent e) {
		if (e.getPlayer().equals(getPlayer())) {
			Block to = e.getTo().getBlock();
			Block toBelow = to.getLocation().subtract(0, 1, 0).getBlock();
			if (to.getType().equals(Material.AIR) || to.getType().equals(Material.SNOW)) {
				to.setType(Material.FIRE);
				if (to.getType().equals(Material.FIRE)) {
					blocks.add(to);
					if (blocks.size() >= 30) {
						blocks.removeFirst().setType(Material.AIR);
					}
				}
			}
			if (toBelow.getType().equals(Material.SNOW_BLOCK)) {
				toBelow.setType(Material.DIRT);
			} else if (toBelow.getType().equals(Material.PACKED_ICE) || toBelow.getType().equals(Material.ICE) || MaterialX.FROSTED_ICE.compareType(toBelow) || MaterialX.BLUE_ICE.compareType(toBelow)) {
				toBelow.setType(Material.WATER);
			}
		}
	}

	@SubscribeEvent
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getEntity().equals(getPlayer())) {
			DamageCause cause = e.getCause();
			if (cause.equals(DamageCause.FIRE) || cause.equals(DamageCause.FIRE_TICK) || cause.equals(DamageCause.LAVA)) {
				e.setCancelled(true);
			}
		}
	}

	@SubscribeEvent(onlyRelevant = true)
	public void onAbilityDestroy(AbilityDestroyEvent e) {
		for (Block block : blocks) {
			if (block.getType() == Material.FIRE) block.setType(Material.AIR);
		}
	}

}
