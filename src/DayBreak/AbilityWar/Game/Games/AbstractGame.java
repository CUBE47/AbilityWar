package DayBreak.AbilityWar.Game.Games;

import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
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

import DayBreak.AbilityWar.AbilityWar;
import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityBase.ClickType;
import DayBreak.AbilityWar.Ability.AbilityBase.MaterialType;
import DayBreak.AbilityWar.Game.Manager.DeathManager;
import DayBreak.AbilityWar.Game.Manager.Firewall;
import DayBreak.AbilityWar.Game.Manager.GameListener;
import DayBreak.AbilityWar.Game.Manager.Invincibility;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;
import DayBreak.AbilityWar.Utils.VersionCompat.VersionUtil;

abstract public class AbstractGame extends Thread implements Listener, EventExecutor {
	
	private static List<String> Spectators = new ArrayList<String>();
	
	public static boolean isSpectator(String name) {
		return Spectators.contains(name);
	}
	
	public static void addSpectator(String name) {
		if(!Spectators.contains(name)) {
			Spectators.add(name);
		}
	}
	
	public static void removeSpectator(String name) {
		Spectators.remove(name);
	}
	
	public static List<String> getSpectators() {
		return Spectators;
	}
	
	private final List<Participant> Participants = setupParticipants();
	
	@SuppressWarnings("unused")
	private final GameListener gameListener = new GameListener(this);
	
	private final DeathManager deathManager = new DeathManager(this);

	private final Invincibility invincibility = new Invincibility(this);
	
	@SuppressWarnings("unused")
	private final Firewall fireWall = new Firewall(this);
	
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
	
	/**
	 * setupPlayers()���� ���� �÷��̾� ����� �������� Participant ����� ����� ��ȯ�մϴ�.
	 * ��ȯ�� ����� Read-Only ������� ��Ұ� ����� �� �����ϴ�.
	 */
	private List<Participant> setupParticipants() {
		List<Participant> list = new ArrayList<Participant>();
		
		for(Player p : setupPlayers()) {
			list.add(new Participant(this, p));
		}
		
		return Collections.unmodifiableList(list);
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
	
	public Invincibility getInvincibility() {
		return invincibility;
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

		private Participant(AbstractGame game, Player player) {
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
					MaterialType mt = parseMaterialType(VersionUtil.getItemInHand(p).getType());
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
						MaterialType mt = parseMaterialType(VersionUtil.getItemInHand(p).getType());
						if(mt != null) {
							if(!e.isCancelled()) {
								if(this.hasAbility()) {
									AbilityBase Ability = this.getAbility();
									if(!Ability.isRestricted()) {
										Instant Now = Instant.now();
										long Duration = java.time.Duration.between(lastClick, Now).toMillis();
										if (Duration >= 250) {
											Entity target = e.getEntity();
											
											if(target instanceof Player) {
												Player t = (Player) target;
												if(AbstractGame.this.isParticipating(t)) {
													this.lastClick = Now;
													Ability.TargetSkill(mt, e.getEntity());
												}
											} else {
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

			ability.setRestricted(isRestricted() || !isGameStarted());

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

			ability.setRestricted(isRestricted() || !isGameStarted());
			
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
				getAbility().Remove();
				ability = null;
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
	
	abstract public class AbilitySelect extends TimerBase {
		
		private HashMap<Participant, Integer> Selectors = new HashMap<Participant, Integer>();
		
		public HashMap<Participant, Integer> getMap() {
			return Selectors;
		}

		public List<Participant> getSelectors() {
			return new ArrayList<Participant>(Selectors.keySet());
		}
		
		public boolean hasDecided(Participant p) {
			return Selectors.get(p) <= 0;
		}
		
		private void setRemainingChangeCount(Participant participant, int count) {
			Selectors.put(participant, count);
			
			if(count == 0) {
				Player p = participant.getPlayer();

				Messager.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "&6�ɷ��� Ȯ���Ǽ̽��ϴ�. �ٸ� �÷��̾ ��ٷ��ּ���."));
				
				Messager.broadcastStringList(Messager.getStringList(
						ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + "&f���� �ɷ��� Ȯ���ϼ̽��ϴ�."),
						ChatColor.translateAlternateColorCodes('&', "&a���� �ο� &7: &f" + getLeftPlayers() + "��")));
			}
		}
		
		private void setDecided(Participant participant) {
			setRemainingChangeCount(participant, 0);
		}
		
		public boolean isSelector(Participant participant) {
			return Selectors.containsKey(participant);
		}
		
		protected AbilitySelect() {
			final int ChangeCount = getChangeCount();
			
			for(Participant p : setupPlayers()) {
				Selectors.put(p, ChangeCount);
			}
			
			this.drawAbility();
			this.StartTimer();
		}
		
		public void decideAbility(Participant participant) {
			if(isSelector(participant)) {
				setDecided(participant);
			}
		}
		
		private int getLeftPlayers() {
			int i = 0;
			for(Participant p : Selectors.keySet()) {
				if(!hasDecided(p)) {
					i++;
				}
			}
			
			return i;
		}
		
		public void Skip(String admin) {
			for(Participant p : Selectors.keySet()) {
				if(!hasDecided(p)) {
					decideAbility(p);
				}
			}

			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f������ &e" + admin + "&f���� ��� �÷��̾��� �ɷ��� ������ Ȯ�����׽��ϴ�."));
			this.StopTimer(false);
		}
		
		/**
		 * �ɷ� ���� ���� Ƚ���� ��ȯ�մϴ�.
		 */
		abstract protected int getChangeCount();
		
		abstract protected void drawAbility();
		
		abstract protected boolean changeAbility(Participant participant);
		
		abstract protected List<Participant> setupPlayers();
		
		abstract protected void onSelectEnd();
		
		public void alterAbility(Participant participant) {
			if(isSelector(participant) && !hasDecided(participant)) {
				setRemainingChangeCount(participant, Selectors.get(participant) - 1);
				if(changeAbility(participant)) {
					Player p = participant.getPlayer();
					
					if(!hasDecided(participant)) {
						Messager.sendStringList(p, Messager.getStringList(
								ChatColor.translateAlternateColorCodes('&', "&a��ſ��� �ɷ��� �Ҵ�Ǿ����ϴ�. &e/ability check&f�� Ȯ�� �� �� �ֽ��ϴ�."),
								ChatColor.translateAlternateColorCodes('&', "&e/ability yes &f��ɾ ����ϸ� �ɷ��� Ȯ���մϴ�."),
								ChatColor.translateAlternateColorCodes('&', "&e/ability no &f��ɾ ����ϸ� �ɷ��� ������ �� �ֽ��ϴ�.")));
					} else {
						Messager.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "&a����� �ɷ��� ����Ǿ����ϴ�. &e/ability check&f�� Ȯ�� �� �� �ֽ��ϴ�."));
					}
				}
			}
		}
		
		private boolean Ended = false;
		
		public boolean isEnded() {
			return Ended;
		}
		
		private boolean isEveryoneSelected() {
			for(Participant Key : getSelectors()) {
				if(!hasDecided(Key)) {
					return false;
				}
			}
			
			return true;
		}
		
		private int Count = 0;
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			if(!isEveryoneSelected()) {
				Count++;
				
				if(Count >= 20) {
					Messager.broadcastStringList(Messager.getStringList(
							ChatColor.translateAlternateColorCodes('&', "&c���� ��� ������ �ɷ��� Ȯ������ �ʾҽ��ϴ�."),
							ChatColor.translateAlternateColorCodes('&', "&c/ability yes�� /ability no ��ɾ�� �ɷ��� Ȯ�����ּ���.")));
					Count = 0;
				}
			} else {
				this.StopTimer(false);
			}
		}
		
		@Override
		public void onEnd() {
			Ended = true;
			onSelectEnd();
		}
		
	}
	
}
