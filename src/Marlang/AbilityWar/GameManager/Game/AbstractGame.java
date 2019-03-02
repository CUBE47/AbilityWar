package Marlang.AbilityWar.GameManager.Game;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.EventExecutor;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.GameManager.Manager.AbilitySelect;
import Marlang.AbilityWar.GameManager.Manager.DeathManager;
import Marlang.AbilityWar.GameManager.Manager.Firewall;
import Marlang.AbilityWar.GameManager.Manager.GameListener;

abstract public class AbstractGame extends Thread implements Listener, EventExecutor {
	
	private static List<String> Spectators = new ArrayList<String>();
	
	private List<Player> Participants = setupParticipants();
	
	private HashMap<Player, AbilityBase> Abilities = new HashMap<Player, AbilityBase>();
	
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
	abstract protected List<Player> setupParticipants();
	
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
	 * �÷��̾ �ɷ������� Ȯ���մϴ�.
	 * @param p	Ȯ���� �÷��̾�
	 * @return	�ɷ��� ����
	 */
	public boolean hasAbility(Player p) {
		return Abilities.containsKey(p) && Abilities.get(p) != null;
	}
	
	/**
	 * �÷��̾�� �ɷ��� ���� ��� �ɷ��� ��ȯ�մϴ�.
	 * �÷��̾�� �ɷ��� ���� ��� null�� ��ȯ�մϴ�.
	 */
	public AbilityBase getAbility(Player p) {
		if(hasAbility(p)) {
			return Abilities.get(p);
		} else {
			return null;
		}
	}
	
	/**
	 * �÷��̾�� �ش� �ɷ��� �ο��մϴ�.
	 * @param p				�ɷ��� �ο��� �÷��̾�
	 * @param abilityClass	�ο��� �ɷ��� ���� (�ɷ� Ŭ����)
	 * @throws Exception	�ɷ��� �ο��ϴ� ���� ������ �߻��Ͽ��� ���
	 */
	public void addAbility(Player p, Class<? extends AbilityBase> abilityClass) throws Exception {
		Constructor<? extends AbilityBase> constructor = abilityClass.getConstructor(Player.class);
		AbilityBase Ability = constructor.newInstance(p);
		
		addAbility(Ability);
	}
	
	/**
	 * AbilityBase�� �ɷ� ��Ͽ� �߰��մϴ�.
	 * @param Ability
	 */
	private void addAbility(AbilityBase Ability) {
		Player p = Ability.getPlayer();
		if(isParticipating(p)) {
			if(isRestricted()) {
				Ability.setRestricted(true);
			} else {
				if(isGameStarted()) {
					Ability.setRestricted(false);
				} else {
					Ability.setRestricted(true);
				}
			}
			
			if(hasAbility(p)) {
				removeAbility(p);
			}
			
			Abilities.put(p, Ability);
		} else {
			throw new IllegalArgumentException("����� ���ӿ� ���������� �ʽ��ϴ�.");
		}
	}
	
	/**
	 * �÷��̾��� �ɷ��� �����մϴ�.
	 * @param p		�ɷ��� ������ �÷��̾�
	 */
	public void removeAbility(Player p) {
		if(Abilities.containsKey(p)) {
			AbilityBase Ability = Abilities.get(p);
			Ability.DeleteAbility();
			Abilities.remove(p);
		}
	}
	
	/**
	 * Ability.getPlayer()�� �ɷ��� to���� �ű�ϴ�.
	 * to�� ���ӿ� ���������� ���� ��� �ƹ� �۾��� ���� �ʽ��ϴ�.
	 */
	public void transferAbility(AbilityBase Ability, Player to) {
		if(isParticipating(to)) {
			removeAbility(Ability.getPlayer());
			Ability.updatePlayer(to);
			addAbility(Ability);
		}
	}
	
	/**
	 * one.getPlayer()�� two.getPlayer()�� �ɷ��� ���� �ڹٲߴϴ�.
	 * @param one	ù��° �÷��̾�
	 * @param two	�ι�° �÷��̾�
	 */
	public void swapAbility(AbilityBase one, AbilityBase two) {
		Player onePlayer = one.getPlayer();
		Player twoPlayer = two.getPlayer();
		
		removeAbility(onePlayer);
		removeAbility(twoPlayer);
		
		one.updatePlayer(twoPlayer);
		two.updatePlayer(onePlayer);
		
		addAbility(one);
		addAbility(two);
	}
	
	/**
	 * �÷��̾�� �⺻ Ŷ�� �����մϴ�.
	 * @param p	Ŷ�� ������ �÷��̾�
	 */
	abstract public void GiveDefaultKit(Player p);
	
	/**
	 * ��� �÷��̾�鿡�� �⺻ Ŷ�� �����մϴ�.
	 */
	public void GiveDefaultKit() {
		for(Player p : getParticipants()) {
			GiveDefaultKit(p);
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
	public List<Player> getParticipants() {
		return Participants;
	}
	
	/**
	 * ��� �÷��̾��� ���� ���θ� ��ȯ�մϴ�.
	 * @param p	��� �÷��̾�
	 * @return	��� �÷��̾��� ���� ����
	 */
	public boolean isParticipating(Player p) {
		return Participants.contains(p);
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
	
	public HashMap<Player, AbilityBase> getAbilities() {
		return Abilities;
	}
	
	protected void startAbilitySelect() {
		this.abilitySelect = setupAbilitySelect();
	}
	
	protected void setGameStarted(boolean gameStarted) {
		GameStarted = gameStarted;
	}
	
	abstract protected void onEnd();
	
}
