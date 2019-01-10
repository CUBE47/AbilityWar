package Marlang.AbilityWar.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
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
	 * ��Ʈ �Ҹ��� �÷��̾�� ����մϴ�.
	 */
	public static void sendNote(Player p, Instrument i, Note n) {
		p.playNote(p.getLocation(), i, n);
	}

	/**
	 * ������ ����մϴ�.
	 */
	public static void broadcastTitle(String Title, String SubTitle) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendTitle(Title, SubTitle, 20, 60, 20);
		}
	}

	/**
	 * ������ �÷��̾�� �����մϴ�.
	 */
	public static void sendTitle(Player p, String Title, String SubTitle) {
		p.sendTitle(Title, SubTitle, 20, 60, 20);
	}

	/**
	 * ������ �÷��̾�� �����մϴ�.
	 */
	public static void sendTitle(Player p, String Title, String SubTitle, Integer Duration) {
		p.sendTitle(Title, SubTitle, 20, Duration, 20);
	}
	
}
