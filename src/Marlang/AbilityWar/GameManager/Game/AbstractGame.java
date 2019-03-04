package Marlang.AbilityWar.GameManager.Game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.EventExecutor;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.GameManager.Manager.AbilitySelect;
import Marlang.AbilityWar.GameManager.Manager.DeathManager;
import Marlang.AbilityWar.GameManager.Manager.Firewall;
import Marlang.AbilityWar.GameManager.Manager.GameListener;
import Marlang.AbilityWar.GameManager.Object.Participant;

abstract public class AbstractGame extends Thread implements Listener, EventExecutor {
	
	private static List<String> Spectators = new ArrayList<String>();
	
	private List<Participant> Participants = setupParticipants();
	
	@SuppressWarnings("unused")
	private GameListener gameListener = new GameListener(this);
	
	private DeathManager deathManager = new DeathManager(this);
	
	@SuppressWarnings("unused")
	private Firewall fireWall = new Firewall(this);
	
	private AbilitySelect abilitySelect;
	
	private boolean Restricted = true;
	
	private boolean GameStarted = false;
	
	protected Integer Seconds = 0;
	
	@Override
	public void run() {
		if(gameCondition()) {
			if(getAbilitySelect() == null || (getAbilitySelect() != null && getAbilitySelect().isEnded())) {
				Seconds++;
				progressGame(Seconds);
			}
		}
	}
	
	/**
	 * Register Event Handler
	 */
	protected void registerEvent(Class<? extends Event> event) {
		Bukkit.getPluginManager().registerEvent(event, this, EventPriority.HIGHEST, this, AbilityWar.getPlugin());
	}
	
	/**
	 * ���� ����
	 */
	abstract protected void progressGame(Integer Seconds);
	
	/**
	 * ���� ���� ����
	 */
	abstract protected boolean gameCondition();
	
	/**
	 * ������ �ʱ갪 ����
	 */
	abstract protected List<Participant> setupParticipants();
	
	/**
	 * AbilitySelect �ʱ갪 ����
	 * �ɷ� �Ҵ��� ���� ���� �����̶�� null�� ��ȯ�ص� �˴ϴ�.
	 */
	abstract protected AbilitySelect setupAbilitySelect();
	
	/**
	 * ������ �÷��̾ ������� ��� ȣ���
	 */
	abstract public void onPlayerDeath(PlayerDeathEvent e);
	
	/**
	 * �÷��̾�� �⺻ Ŷ�� �����մϴ�.
	 * @param p	Ŷ�� ������ �÷��̾�
	 */
	abstract public void GiveDefaultKit(Player p);
	
	/**
	 * ��� �÷��̾�鿡�� �⺻ Ŷ�� �����մϴ�.
	 */
	public void GiveDefaultKit() {
		for(Participant p : getParticipants()) {
			GiveDefaultKit(p.getPlayer());
		}
	}
	
	/**
	 * ������ ����� ��ȯ�մϴ�.
	 * @return	������ ���
	 */
	public static List<String> getSpectators() {
		return Spectators;
	}
	
	/**
	 * ������ ����� ��ȯ�մϴ�.
	 * @return	������ ���
	 */
	public List<Participant> getParticipants() {
		return Participants;
	}
	
	/**
	 * ��� �÷��̾��� ���� ���θ� ��ȯ�մϴ�.
	 * @param p	��� �÷��̾�
	 * @return	��� �÷��̾��� ���� ����
	 */
	public boolean isParticipating(Player p) {
		return Participant.checkParticipant(p);
	}
	
	/**
	 * DeathManager�� ��ȯ�մϴ�.
	 * @return	DeathManager
	 */
	public DeathManager getDeathManager() {
		return deathManager;
	}
	
	public boolean isRestricted() {
		return Restricted;
	}
	
	public void setRestricted(boolean restricted) {
		Restricted = restricted;
	}
	
	public boolean isGameStarted() {
		return GameStarted;
	}
	
	/**
	 * AbilitySelect�� �޾ƿɴϴ�.
	 * @return AbilitySelect (������� ���� ��� Null ��ȯ, �ɷ� ��÷ ���� ��� null ��ȯ)
	 */
	public AbilitySelect getAbilitySelect() {
		return abilitySelect;
	}
	
	protected void startAbilitySelect() {
		this.abilitySelect = setupAbilitySelect();
	}
	
	protected void setGameStarted(boolean gameStarted) {
		GameStarted = gameStarted;
	}
	
	abstract protected void onEnd();
	
}
