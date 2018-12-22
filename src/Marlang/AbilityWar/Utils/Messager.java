package Marlang.AbilityWar.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Marlang.AbilityWar.Ability.AbilityBase;

/**
 * �޽��� ���� Ŭ����
 */
public class Messager {
	
	private static String Prefix = ChatColor.translateAlternateColorCodes('&', "&2��&aAbilityWar&2��&f");

	/**
	 * �ֿܼ� �޽����� �����մϴ�.
	 */
	public static void sendMessage(String msg) {
		System.out.println(Prefix + msg);
	}

	/**
	 * �÷��̾�� �޽����� �����մϴ�.
	 */
	public static void sendMessage(Player p, String msg) {
		p.sendMessage(msg);
	}
	
	/**
	 * ��ɾ ������ ��ü���� �޽����� �����մϴ�.
	 */
	public static void sendMessage(CommandSender sender, String msg) {
		sender.sendMessage(msg);
	}

	/**
	 * �ֿܼ� ���� �޽����� �����մϴ�.
	 */
	public static void sendErrorMessage(String msg) {
		System.out.println(Prefix + ChatColor.RED + msg);
	}

	/**
	 * �÷��̾�� ���� �޽����� �����մϴ�.
	 */
	public static void sendErrorMessage(Player p, String msg) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&l[&c&l!&f&l] &f") + msg);
	}
	
	/**
	 * ��ɾ ������ ��ü���� ���� �޽����� �����մϴ�.
	 */
	public static void sendErrorMessage(CommandSender sender, String msg) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&l[&c&l!&f&l] &f") + msg);
	}
	
	/**
	 * �޽����� �����մϴ�.
	 */
	public static void broadcastMessage(String msg) {
		Bukkit.broadcastMessage(msg);
	}
	
	/**
	 * ������ �����մϴ�.
	 */
	public static String formatTitle(String title) {
		String Base = "______________________________________________________________";
		int Pivot = Base.length() / 2;
		String Center = ChatColor.translateAlternateColorCodes('&', "[ " + "&e" + title + "&6" + " ]");
		String Return = ChatColor.translateAlternateColorCodes('&', "&6" + Base.substring(0, Math.max(0, (Pivot - Center.length() / 2))));
		Return += Center + Base.substring(Pivot + Center.length() / 2);
		return Return;
	}
	
	/**
	 * ª�� ������ �����մϴ�.
	 */
	public static String formatShortTitle(String title) {
		String Base = "_____________________________________";
		int Pivot = Base.length() / 2;
		String Center = ChatColor.translateAlternateColorCodes('&', "[ " + "&e" + title + "&6" + " ]");
		String Return = ChatColor.translateAlternateColorCodes('&', "&6" + Base.substring(0, Math.max(0, (Pivot - Center.length() / 2))));
		Return += Center + Base.substring(Pivot + Center.length() / 2);
		return Return;
	}

	/**
	 * ������ �����մϴ�.
	 */
	public static String formatTitle(ChatColor First, ChatColor Second, String title) {
		String Base = "______________________________________________________________";
		int Pivot = Base.length() / 2;
		String Center = "[ " + Second + title + First + " ]";
		String Return = ChatColor.translateAlternateColorCodes('&', First + Base.substring(0, Math.max(0, (Pivot - Center.length() / 2))));
		Return += Center + Base.substring(Pivot + Center.length() / 2);
		return Return;
	}
	
	/**
	 * ª�� ������ �����մϴ�.
	 */
	public static String formatShortTitle(ChatColor First, ChatColor Second, String title) {
		String Base = "_____________________________________";
		int Pivot = Base.length() / 2;
		String Center = "[ " + Second + title + First + " ]";
		String Return = ChatColor.translateAlternateColorCodes('&', First + Base.substring(0, Math.max(0, (Pivot - Center.length() / 2))));
		Return += Center + Base.substring(Pivot + Center.length() / 2);
		return Return;
	}
	
	/**
	 * �ɷ� ������ �����մϴ�.
	 */
	public static ArrayList<String> formatAbility(AbilityBase Ability) {
		ArrayList<String> AbilityInfo = new ArrayList<String>();
		AbilityInfo.add(formatShortTitle(ChatColor.GREEN, ChatColor.YELLOW, "�ɷ� ����"));
		if(AbilityWarThread.getGame().isAbilityRestricted()) {
			AbilityInfo.add(ChatColor.translateAlternateColorCodes('&', "&b" + Ability.getAbilityName() + " &f[&7�ɷ� ��Ȱ��ȭ��&f] " + Ability.getRank().getRankName()));
		} else if(!AbilityWarThread.getGame().isAbilityRestricted()) {
			AbilityInfo.add(ChatColor.translateAlternateColorCodes('&', "&b" + Ability.getAbilityName() + " &f[&a�ɷ� Ȱ��ȭ��&f] " + Ability.getRank().getRankName()));
		}
		
		for(String s : Ability.getExplain()) {
			AbilityInfo.add(ChatColor.translateAlternateColorCodes('&', "&f" + s));
		}
		
		AbilityInfo.add(ChatColor.translateAlternateColorCodes('&', "&a-----------------------------------------"));
		
		return AbilityInfo;
	}
	

	/**
	 * ��ɾ� ������ �����մϴ�.
	 */
	public static String formatCommand(String Label, String Command, String Help, boolean AdminCommand) {
		if(!AdminCommand) {
			return ChatColor.translateAlternateColorCodes('&', "&a��  ��: &6/" + Label + " &e" + Command + " &7: &f" + Help);
		} else {
			return ChatColor.translateAlternateColorCodes('&', "&c������: &6/" + Label + " &e" + Command + " &7: &f" + Help);
		}
	}

	/**
	 * ��ɾ� ������ �����մϴ�.
	 */
	public static String formatCommand(String Label, String Command, String Help) {
		return ChatColor.translateAlternateColorCodes('&', "&6/" + Label + " &e" + Command + " &7: &f" + Help);
	}
	
	/**
	 * ArrayList�� ����ϴ�.
	 */
	public static ArrayList<String> getArrayList(String... str) {
		ArrayList<String> Return = new ArrayList<String>();
		for(String s : str) {
			Return.add(s);
		}
		
		return Return;
	}
	
	/**
	 * �޽��� ����� �����մϴ�.
	 */
	public static void broadcastStringList(List<String> msg) {
		for(String s : msg) {
			broadcastMessage(s);
		}
	}
	
	/**
	 * ��ɾ ������ ��ü���� �޽��� ����� �����մϴ�.
	 */
	public static void sendStringList(CommandSender sender, ArrayList<String> msg) {
		for(String s : msg) {
			sender.sendMessage(s);
		}
	}

	/**
	 * �÷��̾�� Sync �޽����� �����մϴ�.
	 */
	public static void sendStringList(Player p, ArrayList<String> msg) {
		for(String s : msg) {
			p.sendMessage(s);
		}
	}
	
}
