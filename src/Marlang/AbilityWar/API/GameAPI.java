package Marlang.AbilityWar.API;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import Marlang.AbilityWar.API.Exception.GameException;
import Marlang.AbilityWar.API.Exception.PlayerException;
import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.GameManager.AbilitySelect;
import Marlang.AbilityWar.GameManager.Game;
import Marlang.AbilityWar.Utils.AbilityWarThread;
import Marlang.AbilityWar.Utils.TimerBase;

/**
 * ���� API
 * @author _Marlang ����
 */
public class GameAPI {
	
	Game game;
	
	public GameAPI(Game game) {
		this.game = game;
	}
	
	/**
	 *                       ���� API�� �޾ƿɴϴ�.
	 * @return               GameAPI
	 * @throws GameException ������ ���۵��� ���� ���
	 */
	public static GameAPI getAPI() throws GameException {
		if(AbilityWarThread.isGameTaskRunning()) {
			return AbilityWarThread.getGame().getGameAPI();
		} else {
			throw new GameException();
		}
	}
	
	/**
	 * ���� ���� ���θ� �޾ƿɴϴ�.
	 * ���� �������̸� True,
	 * ���� �������� �ƴϸ� False�� ��ȯ�մϴ�.
	 */
	public static boolean isGameRunning() {
		return AbilityWarThread.isGameTaskRunning();
	}
	
	/**
	 *                       ������ ���۽�ŵ�ϴ�.
	 * @throws GameException ������ �̹� ���۵� ���
	 */
	public static void StartGame() throws GameException {
		if(!AbilityWarThread.isGameTaskRunning()) {
			AbilityWarThread.toggleGameTask(true);
		} else {
			throw new GameException();
		}
	}
	
	/**
	 *                       ������ �����ŵ�ϴ�.
	 * @throws GameException ������ ���۵��� ���� ���
	 */
	public static void StopGame() throws GameException {
		if(AbilityWarThread.isGameTaskRunning()) {
			TimerBase.ResetTasks();
			HandlerList.unregisterAll(AbilityWarThread.getGame().getDeathManager());	
			AbilityWarThread.toggleGameTask(false);
			AbilityWarThread.setGame(null);
		} else {
			throw new GameException();
		}
	}
	
	/**
	 *                       �ɷ� ������ ��ŵ�մϴ�.
	 * @param AdminName      �ɷ� ������ ��ŵ�� �������� �̸�
	 * @throws GameException �ɷ� ������ ���������� ���� ���
	 */
	public void SkipAbilitySelect(String AdminName) throws GameException {
		AbilitySelect select = game.getAbilitySelect();
		if(select != null && !select.isAbilitySelectFinished()) {
			select.Skip(AdminName);
		} else {
			throw new GameException();
		}
	}
	
	/**
	 *                       �ɷ� ������ ��ŵ�մϴ�.
	 * @throws GameException �ɷ� ������ ���������� ���� ���
	 */
	public void SkipAbilitySelect() throws GameException {
		AbilitySelect select = game.getAbilitySelect();
		if(select != null && !select.isAbilitySelectFinished()) {
			select.Skip("CONSOLE");
		} else {
			throw new GameException();
		}
	}
	
	public boolean HasAbility(Player p) {
		return game.getAbilities().containsKey(p);
	}
	
	/**
	 *                         �÷��̾��� �ɷ��� �޾ƿɴϴ�.
	 * @return                 �÷��̾��� �ɷ�
	 * @throws PlayerException �÷��̾�� �ɷ��� ���� ���
	 */
	public AbilityBase GetAbility(Player p) throws PlayerException {
		if(game.getAbilities().containsKey(p)) {
			return game.getAbilities().get(p);
		} else {
			throw new PlayerException();
		}
	}
	
	/**
	 *                         �÷��̾�� �ɷ��� �ο��մϴ�.
	 * @param p                �ɷ��� �ο��� �÷��̾�
	 * @param Ability          �÷��̾�� �ο��� �ɷ� Ŭ����
	 * @throws PlayerException �÷��̾ ���� �����ڰ� �ƴ� ���
	 */
	public void SetAbility(Player p, Class<? extends AbilityBase> AbilityClass) throws PlayerException {
		if(game.getPlayers().contains(p)) {
			try {
				AbilityBase Ability = AbilityClass.newInstance();
				Ability.setPlayer(p);
				game.getAbilities().put(p, Ability);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			throw new PlayerException();
		}
	}
	
}
