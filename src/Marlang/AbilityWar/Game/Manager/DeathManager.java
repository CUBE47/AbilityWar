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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.EventExecutor;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.Game.Games.AbstractGame;
import Marlang.AbilityWar.Game.Games.AbstractGame.Participant;
import Marlang.AbilityWar.Utils.Messager;

/**
 * Death Manager
 * @author _Marlang ����
 */
public class DeathManager implements EventExecutor {
	
	private final AbstractGame game;
	
	public DeathManager(AbstractGame game) {
		this.game = game;
		Bukkit.getPluginManager().registerEvent(PlayerDeathEvent.class, game, EventPriority.HIGHEST, this, AbilityWar.getPlugin());
	}
	
	@Override
	public void execute(Listener listener, Event event) throws EventException {
		if(event instanceof PlayerDeathEvent) {
			PlayerDeathEvent e = (PlayerDeathEvent) event;
			
			Player Victim = e.getEntity();
			Player Killer = Victim.getKiller();
			if(Victim.getLastDamageCause() != null) {
				DamageCause Cause = Victim.getLastDamageCause().getCause();

				if(Killer != null) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&a" + Killer.getName() + "&f���� &c" + Victim.getName() + "&f���� �׿����ϴ�."));
				} else {
					if(Cause.equals(DamageCause.CONTACT)) {
						e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� ��� �׾����ϴ�."));
					} else if(Cause.equals(DamageCause.FALL)) {
						e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� ������ �׾����ϴ�."));
					} else if(Cause.equals(DamageCause.FALLING_BLOCK)) {
						e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� �������� ��Ͽ� �¾� �׾����ϴ�."));
					} else if(Cause.equals(DamageCause.SUFFOCATION)) {
						e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� ���� �׾����ϴ�."));
					} else if(Cause.equals(DamageCause.DROWNING)) {
						e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� ���� ���� �׾����ϴ�."));
					} else if(Cause.equals(DamageCause.ENTITY_EXPLOSION)) {
						e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� �����Ͽ����ϴ�."));
					} else if(Cause.equals(DamageCause.LAVA)) {
						e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� ��Ͽ� ���� �׾����ϴ�."));
					} else if(Cause.equals(DamageCause.FIRE) || Cause.equals(DamageCause.FIRE_TICK)) {
						e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� �븩�븩�ϰ� ���������ϴ�."));
					} else {
						e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� �׾����ϴ�."));
					}
				}
			} else {
				e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� �׾����ϴ�."));
			}

			if(AbilityWarSettings.getAbilityReveal()) {
				if(game.isParticipating(Victim)) {
					Participant victim = game.getParticipant(Victim);
					if(victim.hasAbility()) {
						AbilityBase ability = victim.getAbility();
						
						String name = ability.getName();
						if(name != null) {
							Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&c�ɷ�&f] &c" + Victim.getName() + "&f���� &e" + name + " &f�ɷ��̾����ϴ�!"));
						}
					} else {
						Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&c�ɷ�&f] &c" + Victim.getName() + "&f���� �ɷ��� �����ϴ�!"));
					}
				}
			}
			
			if(game.isGameStarted()) {
				if(AbilityWarSettings.getItemDrop()) {
					e.setKeepInventory(false);
					Victim.getInventory().clear();
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
				Messager.getPrefix()
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
