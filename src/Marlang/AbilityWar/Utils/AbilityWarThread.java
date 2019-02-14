package Marlang.AbilityWar.Utils;

import org.bukkit.Bukkit;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.GameManager.Game.AbstractGame;

/**
 * Ability War �÷����� ������
 * @author _Marlang ����
 */
public class AbilityWarThread {
	
	private static int GameTask = -1;

	private static AbstractGame Game;
	
	/**
	 * ������ ���۽�ŵ�ϴ�.
	 * @param Game ���۽�ų ����
	 */
	public static void startGame(AbstractGame Game) {
		if(!isGameTaskRunning()) {
			setGame(Game);
			while(!isGameTaskRunning()) {
				GameTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(AbilityWar.getPlugin(), getGame(), 0, 20);
			}
		}
	}
	
	/**
	 * �������� ������ �����մϴ�.
	 */
	public static void stopGame() {
		if(isGameTaskRunning()) {
			//Notify
			getGame().onGameEnd();
			//Notify
			
			Bukkit.getScheduler().cancelTask(GameTask);
			setGame(null);
			GameTask = -1;
		}
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
	
	private static void setGame(AbstractGame game) {
		Game = game;
	}
	
}
