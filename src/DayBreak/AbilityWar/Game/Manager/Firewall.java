package DayBreak.AbilityWar.Game.Manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.EventExecutor;

import DayBreak.AbilityWar.AbilityWar;
import DayBreak.AbilityWar.Config.AbilityWarSettings;
import DayBreak.AbilityWar.Config.AbilityWarSettings.DeathSettings;
import DayBreak.AbilityWar.Config.Enums.OnDeath;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;

/**
 * ��ȭ��
 * @author DayBreak ����
 */
public class Firewall implements EventExecutor {
	
	private AbstractGame game;
	
	public Firewall(AbstractGame game) {
		this.game = game;
		Bukkit.getPluginManager().registerEvent(PlayerLoginEvent.class, game, EventPriority.HIGHEST, this, AbilityWar.getPlugin());
	}
	
	@Override
	public void execute(Listener listener, Event event) throws EventException {
		if(event instanceof PlayerLoginEvent) {
			PlayerLoginEvent e = (PlayerLoginEvent) event;

			boolean canLogin = false;
			
			Player p = e.getPlayer();
			
			if(AbilityWarSettings.getFirewall()) {
				
				if(p.isOp()) {
					canLogin = true;
				}
				
				for(Participant participant : game.getParticipants()) {
					if(participant.getPlayer().getName().equals(p.getName())) {
						canLogin = true;
					}
				}
				
				for(String playerName : SpectatorManager.getSpectators()) {
					if(p.getName().equals(playerName)) {
						canLogin = true;
					}
				}
				
				if(!canLogin) {
					e.disallow(Result.KICK_OTHER,
							ChatColor.translateAlternateColorCodes('&', "&2��&aAbilityWar&2��")
							+ "\n"
							+ ChatColor.translateAlternateColorCodes('&', "&f���� �������̹Ƿ� ������ �� �����ϴ�."));
				}
			}
			
			if(DeathSettings.getOperation().equals(OnDeath.Ż��)) {
				if(game.getDeathManager().isEliminated(p) && !p.isOp()) {
					e.disallow(Result.KICK_OTHER,
							ChatColor.translateAlternateColorCodes('&', "&2��&aAbilityWar&2��")
							+ "\n"
							+ ChatColor.translateAlternateColorCodes('&', "&fŻ���ϼ̽��ϴ�."));
				}
			}
		}
	}
	
}
