package Marlang.AbilityWar.GameManager.Game;

import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.EventExecutor;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityBase.ClickType;
import Marlang.AbilityWar.Ability.AbilityBase.MaterialType;
import Marlang.AbilityWar.GameManager.Manager.AbilitySelect;
import Marlang.AbilityWar.GameManager.Manager.DeathManager;
import Marlang.AbilityWar.GameManager.Manager.Firewall;
import Marlang.AbilityWar.GameManager.Manager.GameListener;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.VersionCompat.PlayerCompat;

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
	
	private Integer Seconds = 0;
	
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
	abstract protected List<Player> setupPlayers();
	
	private List<Participant> setupParticipants() {
		List<Participant> participantList = new ArrayList<Participant>();
		
		for(Player p : setupPlayers()) {
			participantList.add(new Participant(this, p));
		}
		
		return participantList;
	}
	
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

	private HashMap<String, Participant> participantCache = new HashMap<String, Participant>();
	
	/**
	 * �ش� �÷��̾ ������� �ϴ� �����ڸ� ��ȯ�մϴ�.
	 * @param player	Ž���� �÷��̾�
	 * @return			�����ڰ� ������ ��� �����ڸ� ��ȯ�մϴ�.
	 * 					�����ڰ� �������� ���� ��� null�� ��ȯ�մϴ�.
	 * 					null üũ�� �ʿ��մϴ�.
	 */
	public Participant getParticipant(Player player) {
		String Key = player.getUniqueId().toString();
		if(participantCache.containsKey(Key)) {
			return participantCache.get(Key);
		} else {
			for(Participant participant : getParticipants()) {
				if(participant.getPlayer().equals(player)) {
					participantCache.put(Key, participant);
					return participant;
				}
			}

			participantCache.put(Key, null);
			return null;
		}
	}
	
	/**
	 * ��� �÷��̾��� ���� ���θ� ��ȯ�մϴ�.
	 * @param p	��� �÷��̾�
	 * @return	��� �÷��̾��� ���� ����
	 */
	public boolean isParticipating(Player player) {
		return getParticipant(player) != null;
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
	
	protected Integer getSeconds() {
		return Seconds;
	}

	protected void setSeconds(Integer seconds) {
		Seconds = seconds;
	}

	protected void startAbilitySelect() {
		this.abilitySelect = setupAbilitySelect();
	}
	
	protected void setGameStarted(boolean gameStarted) {
		GameStarted = gameStarted;
	}
	
	abstract protected void onEnd();
	
	public class Participant implements EventExecutor {
	
		private Player player;
		private final AbstractGame game;

		private Participant(AbstractGame game, Player player) {
			this.game = game;
			this.player = player;

			Bukkit.getPluginManager().registerEvent(PlayerLoginEvent.class, game, EventPriority.HIGH, this, AbilityWar.getPlugin());
			Bukkit.getPluginManager().registerEvent(PlayerInteractEvent.class, game, EventPriority.HIGH, this, AbilityWar.getPlugin());
			Bukkit.getPluginManager().registerEvent(EntityDamageByEntityEvent.class, game, EventPriority.HIGH, this, AbilityWar.getPlugin());
		}

		private Instant lastClick = Instant.now();

		@Override
		public void execute(Listener listener, Event event) throws EventException {
			if (event instanceof PlayerLoginEvent) {
				PlayerLoginEvent e = (PlayerLoginEvent) event;
				if (e.getPlayer().getUniqueId().equals(player.getUniqueId())) {
					this.setPlayer(e.getPlayer());
				}
			} else if (event instanceof PlayerInteractEvent) {
				PlayerInteractEvent e = (PlayerInteractEvent) event;

				Player p = e.getPlayer();
				if (p.equals(getPlayer())) {
					MaterialType mt = parseMaterialType(PlayerCompat.getItemInHand(p).getType());
					ClickType ct = e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK) ? ClickType.RightClick : ClickType.LeftClick;
					if (mt != null) {
						if (hasAbility()) {
							if (!getAbility().isRestricted()) {
								Instant Now = Instant.now();
								long Duration = java.time.Duration.between(lastClick, Now).toMillis();
								if (Duration >= 250) {
									this.lastClick = Now;
									ActiveSkill(getAbility(), mt, ct);

									if (ct.equals(ClickType.LeftClick)) {
										getAbility().TargetSkill(mt, null);
									}
								}
							}
						}
					}
				}
			} else if (event instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
				
				if(e.getDamager() instanceof Player) {
					Player p = (Player) e.getDamager();
					if (p.equals(getPlayer())) {
						MaterialType mt = parseMaterialType(PlayerCompat.getItemInHand(p).getType());
						if(mt != null) {
							if(!e.isCancelled()) {
								if(this.hasAbility()) {
									AbilityBase Ability = this.getAbility();
									if(!Ability.isRestricted()) {
										Instant Now = Instant.now();
										long Duration = java.time.Duration.between(lastClick, Now).toMillis();
										if (Duration >= 250) {
											this.lastClick = Now;
											Ability.TargetSkill(mt, e.getEntity());
										}
									}
								}
							}
						}
					}
				}
			}
		}

		private void ActiveSkill(AbilityBase Ability, MaterialType mt, ClickType ct) {
			boolean Executed = Ability.ActiveSkill(mt, ct);

			if (Executed) {
				Messager.sendMessage(Ability.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&d�ɷ��� ����Ͽ����ϴ�."));
			}
		}

		private MaterialType parseMaterialType(Material m) {
			for (MaterialType Type : MaterialType.values()) {
				if (Type.getMaterial().equals(m)) {
					return Type;
				}
			}

			return null;
		}

		private AbilityBase ability;

		/**
		 * �÷��̾�� �ش� �ɷ��� �ο��մϴ�.
		 * @param p            	�ɷ��� �ο��� �÷��̾�
		 * @param abilityClass 	�ο��� �ɷ��� ���� (�ɷ� Ŭ����)
		 * @throws Exception 	�ɷ��� �ο��ϴ� ���� ������ �߻��Ͽ��� ���
		 */
		public void setAbility(Class<? extends AbilityBase> abilityClass) throws Exception {
			if(hasAbility()) {
				removeAbility();
			}
			
			Constructor<? extends AbilityBase> constructor = abilityClass.getConstructor(Participant.class);
			AbilityBase ability = constructor.newInstance(this);
			
			if(this.game.isRestricted()) {
				ability.setRestricted(true);
			} else {
				if(this.game.isGameStarted()) {
					ability.setRestricted(false);
				} else {
					ability.setRestricted(true);
				}
			}

			this.ability = ability;
		}

		/**
		 * �÷��̾�� �ش� �ɷ��� �ο��մϴ�.
		 * @param ability	�ο��� �ɷ�
		 */
		public void setAbility(AbilityBase ability) {
			if(hasAbility()) {
				removeAbility();
			}
			
			if(this.game.isRestricted()) {
				ability.setRestricted(true);
			} else {
				if(this.game.isGameStarted()) {
					ability.setRestricted(false);
				} else {
					ability.setRestricted(true);
				}
			}
			
			this.ability = ability;
		}

		public boolean hasAbility() {
			return ability != null;
		}

		public AbilityBase getAbility() {
			return ability;
		}
		
		public void removeAbility() {
			if(getAbility() != null) {
				getAbility().DeleteAbility();
				ability = null;
			}
		}
		
		/**
		 * �� �÷��̾��� �ɷ��� to���� �ű�ϴ�.
		 * to�� ���ӿ� �����ϰ� ���� �ʰų� �� �÷��̾�� �ɷ��� ���� ��� �ƹ� �۾��� ���� �ʽ��ϴ�.
		 */
		public void transferAbility(Participant target) {
			if(hasAbility()) {
				AbilityBase Ability = getAbility();
				removeAbility();
				
				Ability.updateParticipant(target);
				
				target.setAbility(Ability);
			}
		}
		
		/**
		 * one.getPlayer()�� two.getPlayer()�� �ɷ��� ���� �ڹٲߴϴ�.
		 * @param one	ù��° �÷��̾�
		 * @param two	�ι�° �÷��̾�
		 */
		public void swapAbility(Participant target) {
			if(hasAbility() && target.hasAbility()) {
				AbilityBase first = getAbility();
				AbilityBase second = target.getAbility();
				
				removeAbility();
				target.removeAbility();
				
				first.updateParticipant(target);
				second.updateParticipant(this);
				
				this.setAbility(second);
				target.setAbility(first);
			}
		}
		
		public Player getPlayer() {
			return player;
		}

		private void setPlayer(Player player) {
			this.player = player;
		}

	}
	
}
