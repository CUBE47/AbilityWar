package Marlang.AbilityWar.Game.Manager;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.EventExecutor;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.Game.Games.AbstractGame;
import Marlang.AbilityWar.Utils.Messager;

/**
 * Death Manager
 * @author _Marlang ����
 */
public class DeathManager implements EventExecutor {
	
	private AbstractGame game;
	
	public DeathManager(AbstractGame game) {
		this.game = game;
		Bukkit.getPluginManager().registerEvent(PlayerDeathEvent.class, game, EventPriority.HIGHEST, this, AbilityWar.getPlugin());
	}
	
	@Override
	public void execute(Listener listener, Event event) throws EventException {
		if(event instanceof PlayerDeathEvent) {
			PlayerDeathEvent e = (PlayerDeathEvent) event;
			
			Player p = e.getEntity();
			if(game.isGameStarted()) {
				if(AbilityWarSettings.getItemDrop()) {
					e.setKeepInventory(false);
					p.getInventory().clear();
				} else {
					e.setKeepInventory(true);
				}
			}
			
			game.onPlayerDeath(e);
		}
	}
	
	/**
	 * Ż���� ���� UUID ���
	 */
	private ArrayList<UUID> Eliminated = new ArrayList<UUID>();
	
	/**
	 * �÷��̾ Ż����ŵ�ϴ�.
	 * @param p   Ż����ų �÷��̾��Դϴ�.
	 */
	public void Eliminate(Player p) {
		Eliminated.add(p.getUniqueId());
		p.kickPlayer(
				ChatColor.translateAlternateColorCodes('&', "&2��&aAbilityWar&2��")
				+ "\n"
				+ ChatColor.translateAlternateColorCodes('&', "&fŻ���ϼ̽��ϴ�."));
		Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c" + p.getName() + "&f���� Ż���ϼ̽��ϴ�."));
	}
	
	/**
	 * �÷��̾��� Ż�� ���θ� Ȯ���մϴ�.
	 */
	public boolean isEliminated(Player p) {
		return Eliminated.contains(p.getUniqueId());
	}
	
}
