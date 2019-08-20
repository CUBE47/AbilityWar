package DayBreak.AbilityWar.Utils.Thread;

import org.bukkit.ChatColor;

import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame;
import DayBreak.AbilityWar.Utils.Messager;

/**
 * Ability War �÷����� ������
 * @author DayBreak ����
 */
public class AbilityWarThread {
	
	private AbilityWarThread() {}
	
	private static AbstractGame Game = null;
	
	/**
	 * ������ ���۽�ŵ�ϴ�.
	 * �������� ������ ���� ��� �ƹ� �۾��� ���� �ʽ��ϴ�.
	 * @param Game ���۽�ų ����
	 */
	public static void StartGame(final AbstractGame Game) {
		if(!isGameTaskRunning()) {
			setGame(Game);
			Game.StartTimer();
		}
	}
	
	/**
	 * �������� ������ �����մϴ�.
	 * �������� ������ ���� ��� �ƹ� �۾��� ���� �ʽ��ϴ�.
	 */
	public static void StopGame() {
		if(isGameTaskRunning()) {
			Game.StopTimer();
			setGame(null);
			
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7������ �����Ǿ����ϴ�."));
		}
	}

	private static void setGame(final AbstractGame game) {
		Game = game;
	}
	
	/**
	 * ������ �������� ��� true, �ƴ� ��� false�� ��ȯ�մϴ�.
	 */
	public static boolean isGameTaskRunning() {
		return Game != null && Game.isTimerRunning();
	}
	
	/**
	 * AbstractGame�� ��ȯ�մϴ�.
	 * �������� ������ ���� ��� null�� ��ȯ�մϴ�.
	 */
	public static AbstractGame getGame() {
		return Game;
	}
	
}
