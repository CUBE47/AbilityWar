package DayBreak.AbilityWar.Utils.Thread;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import DayBreak.AbilityWar.AbilityWar;
import DayBreak.AbilityWar.Game.Games.AbstractGame;
import DayBreak.AbilityWar.Utils.Messager;

/**
 * Ability War �÷����� ������
 * @author DayBreak ����
 */
public class AbilityWarThread {
	
	private AbilityWarThread() {}
	
	private static int GameTask = -1;

	private static AbstractGame Game = null;
	
	/**
	 * ������ ���۽�ŵ�ϴ�.
	 * �������� ������ ���� ��� �ƹ� �۾��� ���� �ʽ��ϴ�.
	 * @param Game ���۽�ų ����
	 */
	public static void StartGame(final AbstractGame Game) {
		if(!isGameTaskRunning()) {
			setGame(Game);
			while(!isGameTaskRunning()) {
				GameTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(AbilityWar.getPlugin(), Game, 0, 20);
			}
		}
	}
	
	/**
	 * �������� ������ �����մϴ�.
	 * �������� ������ ���� ��� �ƹ� �۾��� ���� �ʽ��ϴ�.
	 */
	public static void StopGame() {
		if(isGameTaskRunning()) {
			//Notify
			try {
				Method onEnd = AbstractGame.class.getDeclaredMethod("onEnd");
				onEnd.setAccessible(true);
				onEnd.invoke(getGame());
				onEnd.setAccessible(false);
				
				TimerBase.ResetTasks();
				
				Field gameListener = AbstractGame.class.getDeclaredField("gameListener");
				gameListener.setAccessible(true);
				HandlerList.unregisterAll((Listener) gameListener.get(getGame()));
				gameListener.setAccessible(false);
				
				HandlerList.unregisterAll(getGame());
			} catch(Exception ex) {
				//Should Not Happen
			}
			
			Bukkit.getScheduler().cancelTask(GameTask);
			setGame(null);
			GameTask = -1;
			
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7������ �ʱ�ȭ�Ǿ����ϴ�."));
		}
	}

	private static void setGame(AbstractGame game) {
		Game = game;
	}
	
	/**
	 * ������ �������� ��� true, �ƴ� ��� false�� ��ȯ�մϴ�.
	 */
	public static boolean isGameTaskRunning() {
		return GameTask != -1;
	}
	
	/**
	 * AbstractGame�� ��ȯ�մϴ�.
	 * �������� ������ ���� ��� null�� ��ȯ�մϴ�.
	 */
	public static AbstractGame getGame() {
		return Game;
	}
	
}
