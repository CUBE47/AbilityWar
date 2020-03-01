package daybreak.abilitywar.ability.list;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.ability.SubscribeEvent;
import daybreak.abilitywar.game.games.mode.AbstractGame.Participant;
import daybreak.abilitywar.utils.annotations.Beta;
import daybreak.abilitywar.utils.base.concurrent.TimeUnit;
import daybreak.abilitywar.utils.library.SoundLib;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

@Beta
@AbilityManifest(Name = "유령", Rank = Rank.A, Species = Species.OTHERS)
public class Ghost extends AbilityBase {

	public Ghost(Participant participant) throws IllegalStateException {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&fBETA"));
	}

	private static final Vector ZERO = new Vector();

	private Location targetLocation;

	private final Timer skill = new Timer() {
		GameMode originalMode;
		Player p = getPlayer();

		@Override
		protected void onStart() {
			originalMode = p.getGameMode();
			getParticipant().attributes().TARGETABLE.setValue(false);
			p.setGameMode(GameMode.SPECTATOR);
			SoundLib.ITEM_CHORUS_FRUIT_TELEPORT.playSound(p);
		}

		@Override
		protected void run(int count) {
			if (targetLocation != null && count <= 30) {
				Location playerLocation = p.getLocation();
				p.setVelocity(targetLocation.toVector().subtract(playerLocation.toVector()).multiply(0.5));
				if (playerLocation.distanceSquared(targetLocation) < 1) {
					stop(false);
					p.teleport(targetLocation.setDirection(getPlayer().getLocation().getDirection()));
				}
			} else {
				stop(true);
				if (targetLocation != null)
					p.teleport(targetLocation.setDirection(getPlayer().getLocation().getDirection()));
			}
		}

		@Override
		protected void onEnd() {
			p.setGameMode(originalMode);
			p.setVelocity(ZERO);
			getParticipant().attributes().TARGETABLE.setValue(true);
		}

		@Override
		protected void onSilentEnd() {
			p.setGameMode(originalMode);
			p.setVelocity(ZERO);
			getParticipant().attributes().TARGETABLE.setValue(true);
		}
	}.setPeriod(TimeUnit.TICKS, 1);

	@SubscribeEvent(onlyRelevant = true)
	private void onMove(PlayerMoveEvent e) {
	}

	@SubscribeEvent(onlyRelevant = true)
	private void onGameModeChange(PlayerGameModeChangeEvent e) {
		if (skill.isRunning() && getPlayer().getGameMode() == GameMode.SPECTATOR) e.setCancelled(true);
	}

	@SubscribeEvent(onlyRelevant = true)
	private void onPlayerTeleport(PlayerTeleportEvent e) {
		if (skill.isRunning() && getPlayer().getGameMode() == GameMode.SPECTATOR) e.setCancelled(true);
	}

	@Override
	public boolean ActiveSkill(Material materialType, ClickType clickType) {
		if (materialType == Material.IRON_INGOT && clickType == ClickType.RIGHT_CLICK && !skill.isRunning()) {
			Block lastEmpty = null;
			try {
				for (BlockIterator iterator = new BlockIterator(getPlayer().getWorld(), getPlayer().getLocation().toVector(), getPlayer().getLocation().getDirection(), 1, 7); iterator.hasNext(); ) {
					Block block = iterator.next();
					if (!block.getType().isSolid()) {
						lastEmpty = block;
					}
				}
			} catch (IllegalStateException ignored) {
			}
			if (lastEmpty != null) {
				this.targetLocation = lastEmpty.getLocation();
				skill.start();
				return true;
			} else {
				getPlayer().sendMessage(ChatColor.RED + "바라보는 방향에 이동할 수 있는 곳이 없습니다.");
			}
		}
		return false;
	}

	@Override
	public void TargetSkill(Material materialType, LivingEntity entity) {
	}

}