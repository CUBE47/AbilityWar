package DayBreak.AbilityWar.Game.Manager;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

import DayBreak.AbilityWar.Config.AbilityWarSettings.DeathSettings;
import DayBreak.AbilityWar.Game.Events.ParticipantDeathEvent;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.KoreanUtil;
import DayBreak.AbilityWar.Utils.Messager;

/**
 * Death Manager
 * @author DayBreak ����
 */
public class DeathManager implements Listener {
	
	private final AbstractGame game;

	public DeathManager(AbstractGame game) {
		this.game = game;
		game.registerListener(this);
	}

	@EventHandler
	private final void onDeath(PlayerDeathEvent e) {
		Player victimPlayer = e.getEntity();
		Player killerPlayer = victimPlayer.getKiller();
		if(victimPlayer.getLastDamageCause() != null) {
			DamageCause Cause = victimPlayer.getLastDamageCause().getCause();

			if(killerPlayer != null) {
				e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&a" + killerPlayer.getName() + "&f���� &c" + victimPlayer.getName() + "&f���� �׿����ϴ�."));
			} else {
				if(Cause.equals(DamageCause.CONTACT)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + victimPlayer.getName() + "&f���� ��� �׾����ϴ�."));
				} else if(Cause.equals(DamageCause.FALL)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + victimPlayer.getName() + "&f���� ������ �׾����ϴ�."));
				} else if(Cause.equals(DamageCause.FALLING_BLOCK)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + victimPlayer.getName() + "&f���� �������� ��Ͽ� �¾� �׾����ϴ�."));
				} else if(Cause.equals(DamageCause.SUFFOCATION)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + victimPlayer.getName() + "&f���� ���� �׾����ϴ�."));
				} else if(Cause.equals(DamageCause.DROWNING)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + victimPlayer.getName() + "&f���� �ͻ��߽��ϴ�."));
				} else if(Cause.equals(DamageCause.ENTITY_EXPLOSION)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + victimPlayer.getName() + "&f���� �����߽��ϴ�."));
				} else if(Cause.equals(DamageCause.LAVA)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + victimPlayer.getName() + "&f���� ��Ͽ� ���� �׾����ϴ�."));
				} else if(Cause.equals(DamageCause.FIRE) || Cause.equals(DamageCause.FIRE_TICK)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + victimPlayer.getName() + "&f���� �븩�븩�ϰ� ���������ϴ�."));
				} else {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + victimPlayer.getName() + "&f���� �׾����ϴ�."));
				}
			}
		} else {
			e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + victimPlayer.getName() + "&f���� �׾����ϴ�."));
		}

		if(DeathSettings.getItemDrop()) {
			e.setKeepInventory(false);
			victimPlayer.getInventory().clear();
		} else {
			e.setKeepInventory(true);
		}

		if(game.isParticipating(victimPlayer)) {
			Participant victim = game.getParticipant(victimPlayer);
			
			Bukkit.getPluginManager().callEvent(new ParticipantDeathEvent(victim));
			
			if(DeathSettings.getAbilityReveal()) {
				if(victim.hasAbility()) {
					String name = victim.getAbility().getName();
					if(name != null) {
						Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&c�ɷ�&f] &c" + victimPlayer.getName() + "&f���� �ɷ��� " + KoreanUtil.getCompleteWord("&e" + name, "&f�̾�", "&f��") + "���ϴ�."));
					}
				} else {
					Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&c�ɷ�&f] &c" + victimPlayer.getName() + "&f���� �ɷ��� �����ϴ�."));
				}
			}
		}
		
		this.onPlayerDeath(e);
	}

	protected void onPlayerDeath(PlayerDeathEvent e) {
		Player Victim = e.getEntity();
		
		if(game.isGameStarted() && game.isParticipating(Victim)) {
			switch(DeathSettings.getOperation()) {
			case Ż��:
				this.Eliminate(Victim);
				break;
			case �������:
				Victim.setGameMode(GameMode.SPECTATOR);
				break;
			default:
				break;
			}
			if(DeathSettings.getAbilityRemoval()) game.getParticipant(Victim).removeAbility();
		}
	}
	
	/**
	 * Ż���� ���� UUID ���
	 */
	private final ArrayList<UUID> Eliminated = new ArrayList<UUID>();
	
	/**
	 * �÷��̾ Ż����ŵ�ϴ�.
	 * @param p   Ż����ų �÷��̾��Դϴ�.
	 */
	public final void Eliminate(Player p) {
		Eliminated.add(p.getUniqueId());
		p.kickPlayer(
				Messager.getPrefix()
				+ "\n"
				+ ChatColor.translateAlternateColorCodes('&', "&fŻ���ϼ̽��ϴ�."));
		Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c" + p.getName() + "&f���� Ż���ϼ̽��ϴ�."));
	}
	
	/**
	 * �÷��̾��� Ż�� ���θ� Ȯ���մϴ�.
	 */
	public final boolean isEliminated(Player p) {
		return Eliminated.contains(p.getUniqueId());
	}
	
}
