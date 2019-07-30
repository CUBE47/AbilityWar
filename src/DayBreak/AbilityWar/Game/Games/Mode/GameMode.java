package DayBreak.AbilityWar.Game.Games.Mode;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import DayBreak.AbilityWar.Config.AbilityWarSettings;
import DayBreak.AbilityWar.Config.Nodes.ConfigNodes;
import DayBreak.AbilityWar.Game.Games.ChangeAbility.ChangeAbilityWar;
import DayBreak.AbilityWar.Game.Games.Default.DefaultGame;
import DayBreak.AbilityWar.Game.Games.SquirtGunFight.SummerVacation;
import DayBreak.AbilityWar.Game.Games.TeamGame.TeamFight;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Thread.AbilityWarThread;

/**
 * ���� ���
 * @author DayBreak ����
 */
public class GameMode {
	
	private static ArrayList<Class<? extends AbstractGame>> modeList = new ArrayList<>();

	static {
		registerGameMode(DefaultGame.class);
		registerGameMode(ChangeAbilityWar.class);
		registerGameMode(SummerVacation.class);
		registerGameMode(TeamFight.class);
	}

	public static void registerGameMode(Class<? extends AbstractGame> gameClass) {
		if(!modeList.contains(gameClass)) {
			GameManifest manifest = gameClass.getAnnotation(GameManifest.class);
			if(manifest != null) {
				if(!containsName(manifest.Name())) {
					try {
						gameClass.getConstructor();
						modeList.add(gameClass);
					} catch (NoSuchMethodException | SecurityException e) {
						Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + gameClass.getName() + " &f���Ӹ��� �����ڰ� �ùٸ��� �ʾ� ��ϵ��� �ʾҽ��ϴ�."));
					}
				} else {
					Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + gameClass.getName() + " &f���Ӹ��� ��ġ�� �̸��� �־� ��ϵ��� �ʾҽ��ϴ�."));
				}
			} else {
				Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + gameClass.getName() + " &f���Ӹ��� GameManifest ������̼��� �������� �ʾ� ��ϵ��� �ʾҽ��ϴ�."));
			}
		} else {
			Messager.sendMessage(gameClass.getName() + " ���Ӹ��� ��ġ�� �̸��� �־� ��ϵ��� �ʾҽ��ϴ�.");
		}
	}

	private static boolean containsName(String name) {
		for(Class<? extends AbstractGame> gameClass : modeList) {
			GameManifest manifest = gameClass.getAnnotation(GameManifest.class);
			if(manifest != null) {
				if(manifest.Name().equalsIgnoreCase(name)) {
					return true;
				}
			}
		}
		
		return false;
	}

	public static List<String> nameValues() {
		ArrayList<String> Values = new ArrayList<String>();
		
		for(Class<? extends AbstractGame> gameClass : modeList) {
			GameManifest manifest = gameClass.getAnnotation(GameManifest.class);
			if(manifest != null) {
				Values.add(manifest.Name());
			}
		}
		
		return Values;
	}
	
	public static Class<? extends AbstractGame> getByString(String name) {
		for(Class<? extends AbstractGame> gameClass : modeList) {
			GameManifest manifest = gameClass.getAnnotation(GameManifest.class);
			if(manifest != null) {
				if(manifest.Name().equalsIgnoreCase(name)) {
					return gameClass;
				}
			}
		}
		
		return null;
	}
	
	public static boolean startGame() {
		try {
			AbilityWarThread.StartGame(AbilityWarSettings.getGameMode().newInstance());
			return true;
		} catch (InstantiationException | IllegalAccessException e) {
			AbilityWarSettings.setNewProperty(ConfigNodes.GameMode, DefaultGame.class.getName());
			AbilityWarThread.StartGame(new DefaultGame());
			return false;
		}
	}
	
}
