package Marlang.AbilityWar.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * 
 * @author _Marlang
 */
public class EffectUtil {
	
	/**
	 * �Ҹ��� ����մϴ�.
	 */
	public static void broadcastSound(Sound s) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.playSound(p.getLocation(), s, 5, 1);
		}
	}
	
	/**
	 * �Ҹ��� �÷��̾�� ����մϴ�.
	 */
	public static void sendSound(Player p, Sound s) {
		p.playSound(p.getLocation(), s, 5, 1);
	}
	
	/**
	 * ������ ����մϴ�.
	 */
	public static void broadcastTitle(String Title, String SubTitle) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendTitle(Title, SubTitle, 20, 60, 20);
		}
	}
	
}
