package DayBreak.AbilityWar.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Utils.AutoUpdate.AutoUpdate.UpdateObject;

/**
 * �޽��� ���� Ŭ����
 * @author DayBreak ����
 */
public class Messager {
	
	private Messager() {}
	
	private static final String Prefix = ChatColor.translateAlternateColorCodes('&', "&2��&aAbilityWar&2��&f");
	
	public static String getPrefix() {
		return Prefix;
	}
	
	/**
	 * �ֿܼ� �޽����� �����մϴ�.
	 */
	public static void sendMessage(String msg) {
		Bukkit.getConsoleSender().sendMessage(Prefix + msg);
	}
	
	/**
	 * �ֿܼ� �޽����� �����մϴ�.
	 */
	public static void sendMessage(List<String> messages) {
		for(String msg : messages) {
			sendMessage(msg);
		}
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
	 * �ֿܼ� ����� �޽����� �����մϴ�.
	 */
	public static void sendDebugMessage(String msg) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&7AbilityWar&8] &f" + msg));
	}
	
	/**
	 * �ֿܼ� ���� �޽����� �����մϴ�.
	 */
	public static void sendErrorMessage(String msg) {
		System.out.println(ChatColor.translateAlternateColorCodes('&', "&f&l[&c&l!&f&l] &r&c" + msg));
	}

	/**
	 * �ֿܼ� ���� �޽����� �����մϴ�.
	 */
	public static void sendErrorMessage() {
		System.out.println(ChatColor.translateAlternateColorCodes('&', "&f&l[&c&l!&f&l] &r&c������ �߻��Ͽ����ϴ�."));
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
	 * ���� �޽����� �����մϴ�.
	 */
	public static void broadcastErrorMessage(String msg) {
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f&l[&c&l!&f&l] &f") + msg);
	}
	
	/**
	 * �޽����� �����մϴ�.
	 */
	public static void broadcastMessage(String msg) {
		Bukkit.broadcastMessage(msg);
	}

	public static void broadcastMessage(List<String> messages) {
		for(String msg : messages) {
			broadcastMessage(msg);
		}
	}

	/**
	 * ä��â�� û���մϴ�.
	 */
	public static void clearChat() {
		for(int i = 0; i < 100; i++) for(Player p : Bukkit.getOnlinePlayers()) Messager.sendMessage(p, "");
	}

	/**
	 * �÷��̾��� ä��â�� û���մϴ�.
	 */
	public static void clearChat(Player target) {
		for(int i = 0; i < 100; i++) Messager.sendMessage(target, "");
	}
	
	/**
	 * ������ �����մϴ�.
	 */
	public static String formatTitle(String title) {
		String Base = "_________________________________________________________";
		int Pivot = Base.length() / 2;
		String Center = ChatColor.translateAlternateColorCodes('&', "[ " + "&e" + title + "&6" + " ]&m&l");
		String Return = ChatColor.translateAlternateColorCodes('&', "&6&m&l" + Base.substring(0, Math.max(0, (Pivot - Center.length() / 2))) + "&r&6");
		Return += Center + Base.substring(Pivot + Center.length() / 2);
		return Return;
	}
	
	/**
	 * ª�� ������ �����մϴ�.
	 */
	public static String formatShortTitle(String title) {
		String Base = "________________________________";
		int Pivot = Base.length() / 2;
		String Center = ChatColor.translateAlternateColorCodes('&', "[ " + "&e" + title + "&6" + " ]&m&l");
		String Return = ChatColor.translateAlternateColorCodes('&', "&6&m&l" + Base.substring(0, Math.max(0, (Pivot - Center.length() / 2))) + "&r&6");
		Return += Center + Base.substring(Pivot + Center.length() / 2);
		return Return;
	}

	/**
	 * ������ �����մϴ�.
	 */
	public static String formatTitle(ChatColor First, ChatColor Second, String title) {
		String Base = "_________________________________________________________";
		int Pivot = Base.length() / 2;
		String Center = ChatColor.translateAlternateColorCodes('&', "[ " + Second + title + First + " ]&m&l");
		String Return = ChatColor.translateAlternateColorCodes('&', First + "&m&l" + Base.substring(0, Math.max(0, (Pivot - Center.length() / 2))) + "&r" + First);
		Return += Center + Base.substring(Pivot + Center.length() / 2);
		return Return;
	}

	/**
	 * ª�� ������ �����մϴ�.
	 */
	public static String formatShortTitle(ChatColor First, ChatColor Second, String title) {
		String Base = "________________________________";
		int Pivot = Base.length() / 2;
		String Center = ChatColor.translateAlternateColorCodes('&', "[ " + Second + title + First + " ]&m&l");
		String Return = ChatColor.translateAlternateColorCodes('&', First + "&m&l" + Base.substring(0, Math.max(0, (Pivot - Center.length() / 2))) + "&r" + First);
		Return += Center + Base.substring(Pivot + Center.length() / 2);
		return Return;
	}

	/**
	 * ������Ʈ ������ �����մϴ�.
	 * @throws IOException 
	 */
	public static ArrayList<String> formatUpdate(UpdateObject update) throws IOException {
		ArrayList<String> UpdateInfo = new ArrayList<String>();
		UpdateInfo.add(Messager.formatTitle(ChatColor.DARK_GREEN, ChatColor.GREEN, "������Ʈ"));
		UpdateInfo.add(ChatColor.translateAlternateColorCodes('&', "&b" + update.getTag() + " &f������Ʈ &f(&7v" + update.getVersion() + "&f) "
				+ "(&7" + (update.getFileSize() / 1024) + "KB&f)"));
		for(String s : update.getPatchNote()) {
			UpdateInfo.add(s);
		}
		UpdateInfo.add(ChatColor.translateAlternateColorCodes('&', "&2-----------------------------------------------------"));
		
		return UpdateInfo;
	}

	/**
	 * ������Ʈ ������ �����մϴ�.
	 * @throws IOException 
	 */
	public static ArrayList<String> formatUpdateNotice(UpdateObject update) throws IOException {
		ArrayList<String> UpdateInfo = new ArrayList<String>();
		UpdateInfo.add(Messager.formatTitle(ChatColor.DARK_AQUA, ChatColor.AQUA, "������Ʈ"));
		UpdateInfo.add(ChatColor.translateAlternateColorCodes('&', "&f���� ������ &3������Ʈ&f�� �ֽ��ϴ�: &b" + update.getTag() + " &f������Ʈ &f(&7v" + update.getVersion() + "&f) "
				+ "(&7" + (update.getFileSize() / 1024) + "KB&f)"));
		UpdateInfo.add(ChatColor.translateAlternateColorCodes('&', "&3������Ʈ&f�� �����Ϸ��� &e/aw update &f��ɾ ����ϼ���."));
		UpdateInfo.add(ChatColor.translateAlternateColorCodes('&', "&3---------------------------------------------------------"));
		
		return UpdateInfo;
	}
	
	/**
	 * �ɷ� ������ �����մϴ�.
	 */
	public static ArrayList<String> formatAbilityInfo(AbilityBase Ability) {
		ArrayList<String> AbilityInfo = new ArrayList<String>();
		AbilityInfo.add(formatShortTitle(ChatColor.GREEN, ChatColor.YELLOW, "�ɷ� ����"));
		
		String name = Ability.getName();
		Rank rank = Ability.getRank();
		Species species = Ability.getSpecies();
		
		if(name != null && rank != null) {
			String Restricted = Ability.isRestricted() ? "&f[&7�ɷ� ��Ȱ��ȭ��&f]" : "&f[&a�ɷ� Ȱ��ȭ��&f]";
			AbilityInfo.add(ChatColor.translateAlternateColorCodes('&', "&b" + name + " " + Restricted + " " + rank.getRankName() + " " + species.getName()));
			
			for(String s : Ability.getExplain()) {
				AbilityInfo.add(ChatColor.translateAlternateColorCodes('&', "&f" + s));
			}
		} else {
			AbilityInfo.add(ChatColor.translateAlternateColorCodes('&', "&c�ɷ� ������ �ҷ����� ���� ������ �߻��Ͽ����ϴ�."));
			AbilityInfo.add(ChatColor.translateAlternateColorCodes('&', "&cAbility Class : " + Ability.getClass().getName()));
		}
		
		AbilityInfo.add(ChatColor.translateAlternateColorCodes('&', "&a------------------------------"));
		
		return AbilityInfo;
	}

	/**
	 * ��Ÿ�� ������ �����մϴ�.
	 */
	public static String formatCooldown(Integer Cool) {
		return ChatColor.translateAlternateColorCodes('&', "&c��Ÿ�� &7: &f" + Cool + "��");
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
	 * String ArrayList�� ����ϴ�.
	 */
	public static ArrayList<String> getStringList(String[] arr, String... str) {
		ArrayList<String> Return = new ArrayList<String>();
		for(String s : arr) {
			Return.add(s);
		}
		
		for(String s : str) {
			Return.add(s);
		}
		
		return Return;
	}
	
	/**
	 * String ArrayList�� ����ϴ�.
	 */
	public static ArrayList<String> getStringList(String... str) {
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
	public static void sendStringList(CommandSender sender, List<String> msg) {
		for(String s : msg) {
			sender.sendMessage(s);
		}
	}

	/**
	 * �÷��̾�� �޽��� ����� �����մϴ�.
	 */
	public static void sendStringList(Player p, ArrayList<String> msg) {
		for(String s : msg) {
			p.sendMessage(s);
		}
	}
	
	/**
	 * String �迭���� ù��° �μ��� �����մϴ�.
	 */
	public static String[] removeFirstArg(String[] args) {
		return removeArgs(args, 1);
	}
	
	/**
	 * String �迭���� �μ��� �����մϴ�.
	 */
	public static String[] removeArgs(String[] args, int startIndex) {
		if (args.length == 0)
			return args;
		else if (args.length < startIndex)
			return new String[0];
		else {
			String[] newArgs = new String[args.length - startIndex];
			System.arraycopy(args, startIndex, newArgs, 0, args.length - startIndex);
			return newArgs;
		}
	}

}
