package Marlang.AbilityWar.GameManager;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.Utils.Messager;

/**
 * ���� ���� Ŭ����
 * @author _Marlang ����
 */
public class Game extends Thread {
	
	private static AbilityWar Plugin;
	
	public static void Initialize(AbilityWar Plugin) {
		Game.Plugin = Plugin;
	}
	
	int Seconds = 0;
	
	ArrayList<Player> Spectators = new ArrayList<Player>();
	ArrayList<Player> Players = new ArrayList<Player>();
	
	@Override
	public void run() {
		Seconds++;
		GameProgress(Seconds);
	}
	
	public void GameProgress(Integer Seconds) {
		switch(Seconds) {
			case 1:
				SetupPlayers();
				broadcastPlayerList();
				break;
			case 5:
				broadcastPluginDescription();
				break;
		}
	}
	
	public void broadcastPlayerList() {
		int Count = 0;
		
        ArrayList<String> msg = new ArrayList<String>();
		
		msg.add(ChatColor.translateAlternateColorCodes('&', "&6==== &e���� ������ ��� &6===="));
		for(Player p : Players) {
			Count++;
			msg.add(ChatColor.translateAlternateColorCodes('&', "&a" + Count + ". &f" + p.getName()));
		}
		msg.add(ChatColor.translateAlternateColorCodes('&', "&e�� �ο��� : " + Count + "��"));
		msg.add(ChatColor.translateAlternateColorCodes('&', "&6==========================="));
		
		Messager.broadcastSyncMessage(msg);
	}
	
	public void broadcastPluginDescription() {
		ArrayList<String> msg = Messager.getArrayList(
				ChatColor.translateAlternateColorCodes('&', "&cAbilityWar &f- &6�ɷ��� ����"),
				ChatColor.translateAlternateColorCodes('&', "&e���� &7: &f" + Plugin.getDescription().getVersion()),
				ChatColor.translateAlternateColorCodes('&', "&b������ &7: &f_Marlang"),
				ChatColor.translateAlternateColorCodes('&', "&9���ڵ� &7: &f����&7#5908"));
		
		Messager.broadcastSyncMessage(msg);
	}
	
	public ArrayList<Player> getSpectators() {
		return Spectators;
	}
	
	public void SetupPlayers() {
		ArrayList<Player> Players = new ArrayList<Player>();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!getSpectators().contains(p)) {
				Players.add(p);
			}
		}

		this.Players = Players;
	}
	
}
