package Marlang.AbilityWar.Utils;

import org.bukkit.ChatColor;

/**
 * �޽��� ���� Ŭ����
 */
public class Messager {
	
	private static String Prefix = ChatColor.translateAlternateColorCodes('&', "&2��&aAbilityWar&2��&f");
	
	/**
	 * �ֿܼ� �޽����� �����մϴ�.
	 * @param msg
	 */
	public static void sendMessage(String msg) {
		System.out.println(Prefix + msg);
	}
	
}
