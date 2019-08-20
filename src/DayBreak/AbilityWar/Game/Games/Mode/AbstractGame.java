package DayBreak.AbilityWar.Game.Games.Mode;

import static DayBreak.AbilityWar.Utils.Validate.notNull;

import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.EventExecutor;

import DayBreak.AbilityWar.AbilityWar;
import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityBase.ClickType;
import DayBreak.AbilityWar.Ability.AbilityBase.MaterialType;
import DayBreak.AbilityWar.Config.AbilityWarSettings;
import DayBreak.AbilityWar.Game.Events.GameEndEvent;
import DayBreak.AbilityWar.Game.Events.GameReadyEvent;
import DayBreak.AbilityWar.Game.Events.GameStartEvent;
import DayBreak.AbilityWar.Game.Manager.DeathManager;
import DayBreak.AbilityWar.Game.Manager.EffectManager;
import DayBreak.AbilityWar.Game.Manager.Firewall;
import DayBreak.AbilityWar.Game.Manager.Invincibility;
import DayBreak.AbilityWar.Game.Manager.ScoreboardManager;
import DayBreak.AbilityWar.Game.Manager.WRECK;
import DayBreak.AbilityWar.Game.Manager.PassiveManager.PassiveManager;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Thread.Timer;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;
import DayBreak.AbilityWar.Utils.VersionCompat.VersionUtil;

public abstract class AbstractGame extends Timer implements Listener {

	private final Map<String, Participant> participants = initParticipants();
	
	/**
	 * {@link String}�� key�̰� {@link Participant}�� value�� ���� �ʱⰪ�� �����ϱ� ���� ���Ǵ� �޼ҵ��Դϴ�.
	 * @return ���� Unmodifiable {@link Map}���� ��ȯ�Ǿ� ��Ұ� ����� �� �����ϴ�.
	 * @throws
	 */
	private final Map<String, Participant> initParticipants() {
		Map<String, Participant> initial = new HashMap<>();
		for(Player p : notNull(initPlayers())) initial.put(p.getUniqueId().toString(), new Participant(this, p));
		return Collections.unmodifiableMap(initial);
	}
	
	/**
	 * ���ӿ� ������ {@link Player} {@link List} �ʱ갪 ����
	 * @NotNull
	 */
	protected abstract List<Player> initPlayers();

	private final List<Listener> registeredListeners = new ArrayList<>();
	/**
	 * ������ ����� �� ��� �����Ǿ�� �ϴ� {@link Listener}�� ����մϴ�.
	 */
	public final void registerListener(Listener listener) {
		Bukkit.getPluginManager().registerEvents(notNull(listener), AbilityWar.getPlugin());
		registeredListeners.add(listener);
	}

	private final DeathManager deathManager = notNull(setupDeathManager());
	
	private final Invincibility invincibility = new Invincibility(this);
	
	private final EffectManager effectManager = new EffectManager(this);
	
	private final WRECK wreck = new WRECK(this);
	
	private final ScoreboardManager scoreboardManager = new ScoreboardManager(this);
	
	private final PassiveManager passiveManager = new PassiveManager(this);
	
	@SuppressWarnings("unused")
	private final Firewall fireWall = new Firewall(this);
	
	private AbilitySelect abilitySelect = null;
	
	private boolean Restricted = true;
	
	private boolean GameStarted = false;
	
	private int Seconds = 0;

	@Override
	protected void onStart() {
		Bukkit.getPluginManager().callEvent(new GameReadyEvent(this));
		registerListener(this);
	}
	
	@Override
	protected void TimerProcess(Integer count) {
		if(getAbilitySelect() == null || (getAbilitySelect() != null && getAbilitySelect().isEnded())) {
			Seconds++;
			progressGame(Seconds);
		}
	}

	@Override
	protected void onEnd() {
		TimerBase.ResetTasks();
		HandlerList.unregisterAll(this);
		for(Listener lis : registeredListeners) HandlerList.unregisterAll(lis);
		this.scoreboardManager.Clear();
		this.onGameEnd();
		Bukkit.getPluginManager().callEvent(new GameEndEvent(this));
	}
	
	protected abstract void onGameEnd();
	
	/**
	 * ���� ����
	 */
	protected abstract void progressGame(Integer Seconds);
	
	/**
	 * AbilitySelect �ʱ갪 ����
	 * @Nullable �ɷ� �Ҵ��� �ʿ����� ���� ��� null�� ��ȯ�ϼ���.
	 */
	protected abstract AbilitySelect setupAbilitySelect();

	/**
	 * DeathManager �ʱ갪 ����
	 * @NotNull
	 */
	protected DeathManager setupDeathManager() {
		return new DeathManager(this);
	}
	
	/**
	 * �÷��̾�� �⺻ Ŷ�� �����մϴ�.
	 * @param p	Ŷ�� ������ �÷��̾�
	 */
	public abstract void GiveDefaultKit(Player p);
	
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
	public Collection<Participant> getParticipants() {
		return participants.values();
	}

	protected ScoreboardManager getScoreboardManager() {
		return scoreboardManager;
	}

	/**
	 * {@link Player}�� ������� �ϴ� {@link Participant}�� Ž���մϴ�.
	 * @param player	Ž���� �÷��̾�
	 * @return			������ ��� {@link Participant}�� ��ȯ�մϴ�.
	 * 					�������� ���� ��� null�� ��ȯ�մϴ�.
	 * @Nullable
	 */
	public final Participant getParticipant(final Player player) {
		String key = player.getUniqueId().toString();
		if(participants.containsKey(key)) return participants.get(key);
		return null;
	}

	/**
	 * �ش� {@link UUID}�� ������ �ִ� {@link Player}�� ������� �ϴ� {@link Participant}�� Ž���մϴ�.
	 * @param player	Ž���� �÷��̾�
	 * @return			������ ��� {@link Participant}�� ��ȯ�մϴ�.
	 * 					�������� ���� ��� null�� ��ȯ�մϴ�.
	 * @Nullable
	 */
	public final Participant getParticipant(final UUID uuid) {
		String key = uuid.toString();
		if(participants.containsKey(key)) return participants.get(key);
		return null;
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
	 * @NotNull
	 */
	public DeathManager getDeathManager() {
		return deathManager;
	}

	/**
	 * EffectManager�� ��ȯ�մϴ�.
	 * @NotNull
	 */
	public EffectManager getEffectManager() {
		return effectManager;
	}

	/**
	 * WRECK�� ��ȯ�մϴ�.
	 * @NotNull
	 */
	public WRECK getWRECK() {
		return wreck;
	}

	/**
	 * PassiveManager�� ��ȯ�մϴ�.
	 * @NotNull
	 */
	public PassiveManager getPassiveManager() {
		return passiveManager;
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
	 * AbilitySelect�� ��ȯ�մϴ�.
	 * @Nullable �ɷ� �Ҵ� ���̰ų� �ɷ� �Ҵ� ����� ������� ���� ��� null�� ��ȯ�մϴ�.
	 */
	public AbilitySelect getAbilitySelect() {
		return abilitySelect;
	}
	
	/**
	 * Invincibility�� ��ȯ�մϴ�.
	 * @NotNull
	 */
	public Invincibility getInvincibility() {
		return invincibility;
	}

	protected int getSeconds() {
		return Seconds;
	}

	protected void setSeconds(Integer seconds) {
		Seconds = seconds;
	}

	protected void startAbilitySelect() {
		this.abilitySelect = setupAbilitySelect();
	}
	
	protected void startGame() {
		GameStarted = true;
		wreck.noticeIfEnabled();
		this.getScoreboardManager().Initialize();
		Bukkit.getPluginManager().callEvent(new GameStartEvent(this));
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		if(GameStarted && AbilityWarSettings.getClearWeather()) e.setCancelled(true);
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		if(AbilityWarSettings.getNoHunger()) {
			e.setCancelled(true);
			
			Player p = (Player) e.getEntity();
			p.setFoodLevel(19);
		}
	}
	
	public class Participant implements EventExecutor {
	
		private Player player;

		private Participant(AbstractGame game, Player player) {
			this.player = player;

			Bukkit.getPluginManager().registerEvent(PlayerLoginEvent.class, game, EventPriority.HIGH, this, AbilityWar.getPlugin());
			Bukkit.getPluginManager().registerEvent(PlayerInteractEvent.class, game, EventPriority.HIGH, this, AbilityWar.getPlugin());
			Bukkit.getPluginManager().registerEvent(PlayerInteractAtEntityEvent.class, game, EventPriority.HIGH, this, AbilityWar.getPlugin());
		}

		private Instant lastClick = Instant.now();

		@Override
		public void execute(Listener listener, Event event) throws EventException {
			if (event instanceof PlayerLoginEvent) {
				PlayerLoginEvent e = (PlayerLoginEvent) event;
				if (e.getPlayer().getUniqueId().equals(player.getUniqueId())) {
					this.player = e.getPlayer();
				}
			} else if (event instanceof PlayerInteractEvent) {
				PlayerInteractEvent e = (PlayerInteractEvent) event;

				Player p = e.getPlayer();
				if (p.equals(getPlayer())) {
					MaterialType mt = parseMaterialType(VersionUtil.getItemInHand(p).getType());
					ClickType ct = e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK) ? ClickType.RightClick : ClickType.LeftClick;
					if (mt != null) {
						if (hasAbility()) {
							AbilityBase Ability = this.getAbility();
							if (!Ability.isRestricted()) {
								Instant Now = Instant.now();
								long Duration = java.time.Duration.between(lastClick, Now).toMillis();
								if (Duration >= 250) {
									this.lastClick = Now;
									ActiveSkill(Ability, mt, ct);
								}
							}
						}
					}
				}
			} else if (event instanceof PlayerInteractAtEntityEvent) {
				PlayerInteractAtEntityEvent e = (PlayerInteractAtEntityEvent) event;

				Player p = e.getPlayer();
				if (p.equals(getPlayer())) {
					MaterialType mt = parseMaterialType(VersionUtil.getItemInHand(p).getType());
					if(mt != null && !e.isCancelled() && this.hasAbility()) {
						AbilityBase Ability = this.getAbility();
						if(!Ability.isRestricted()) {
							Instant Now = Instant.now();
							long Duration = java.time.Duration.between(lastClick, Now).toMillis();
							if (Duration >= 250) {
								Entity targetEntity = e.getRightClicked();
								if(targetEntity instanceof LivingEntity) {
									if(targetEntity instanceof Player) {
										Player targetPlayer = (Player) targetEntity;
										if(AbstractGame.this.isParticipating(targetPlayer)) {
											this.lastClick = Now;
											Ability.TargetSkill(mt, targetPlayer);
										}
									} else {
										LivingEntity target = (LivingEntity) targetEntity;
										this.lastClick = Now;
										Ability.TargetSkill(mt, target);
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
			if(hasAbility()) removeAbility();
			
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
				getAbility().destroy();
				ability = null;
			}
		}
		
		public Player getPlayer() {
			return player;
		}

	}
	
	public abstract class AbilitySelect extends TimerBase {

		private final int changeCount = initChangeCount();
		
		/**
		 * �ɷ� ���� ���� Ƚ���� �����մϴ�.
		 */
		protected abstract int initChangeCount();
		
		private final Map<Participant, Integer> selectors = setupSelectors();

		private final Map<Participant, Integer> setupSelectors() {
			Map<Participant, Integer> initial = new HashMap<>();
			for(Participant p : notNull(initSelectors())) initial.put(p, changeCount);
			return initial;
		}
		
		/**
		 * �ɷ��� ������ {@link Participant} ����� �����մϴ�.
		 * @NotNull
		 */
		protected abstract Collection<Participant> initSelectors();
		
		/**
		 * �ɷ��� ������ {@link Participant} ����� ��ȯ�մϴ�.
		 */
		public final Collection<Participant> getSelectors() {
			return selectors.keySet();
		}

		/**
		 * {@link Participant}���� ���� �ɷ� ���� Ƚ���� �����մϴ�.
		 */
		private final void setRemainingChangeCount(Participant participant, int count) {
			selectors.put(participant, count);
			
			if(count == 0) {
				Player p = participant.getPlayer();

				Messager.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "&6�ɷ��� Ȯ���Ǽ̽��ϴ�. �ٸ� �÷��̾ ��ٷ��ּ���."));
				
				Messager.broadcastStringList(Messager.getStringList(
						ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + "&f���� �ɷ��� Ȯ���ϼ̽��ϴ�."),
						ChatColor.translateAlternateColorCodes('&', "&a���� �ο� &7: &f" + getLeftPlayersCount() + "��")));
			}
		}

		/**
		 * �ɷ��� ���� �������� ���� �������� ���� ��ȯ�մϴ�.
		 */
		private final int getLeftPlayersCount() {
			int count = 0;
			for(Participant p : getSelectors()) if(!hasDecided(p)) count++;
			return count;
		}
		
		/**
		 * {@link Participant}�� �ɷ� ���� ���θ� ��ȯ�մϴ�.
		 * �ɷ��� �������� {@link Participant}�� �ƴ� ��� false�� ��ȯ�մϴ�.
		 */
		public final boolean hasDecided(final Participant participant) {
			if(selectors.containsKey(participant)) {
				return selectors.get(participant) <= 0;
			} else {
				return false;
			}
		}

		/**
		 * �ɷ� ���� �� {@link Participant}�� �ɷ��� �����մϴ�.
		 */
		public final void alterAbility(Participant participant) {
			if(isSelector(participant) && !hasDecided(participant)) {
				setRemainingChangeCount(participant, selectors.get(participant) - 1);
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

		/**
		 * �����ڵ��� �ʱ� �ɷ��� �����մϴ�.
		 */
		protected abstract void drawAbility(Collection<Participant> selectors);
		
		/**
		 * �ɷ� ���� �� {@link Participant}�� �ɷ��� �����մϴ�.
		 */
		protected abstract boolean changeAbility(Participant participant);
		
		/**
		 * �ɷ� ���� �� {@link Participant}�� �ɷ��� �����մϴ�.
		 * �ɷ��� �����ϸ� �� �̻� �ɷ��� ������ �� �����ϴ�.
		 */
		public final void decideAbility(Participant participant) {
			if(isSelector(participant)) setRemainingChangeCount(participant, 0);
		}
		
		/**
		 * {@link Participant}�� �ɷ� ���ÿ� ������ ������������ ���θ� ��ȯ�մϴ�.
		 */
		public final boolean isSelector(Participant participant) {
			return selectors.containsKey(participant);
		}

		/**
		 * ��� �������� �ɷ��� ������ �����մϴ�.
		 * @param admin		����� �������� �̸�
		 */
		public final void Skip(String admin) {
			for(Participant p : getSelectors()) if(!hasDecided(p)) decideAbility(p);

			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f������ &e" + admin + "&f���� ��� �÷��̾��� �ɷ��� ������ Ȯ�����׽��ϴ�."));
			this.StopTimer(false);
		}

		protected AbilitySelect() {
			drawAbility(getSelectors());
			StartTimer();
		}
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			if(!isEveryoneSelected()) {
				if(Seconds % 20 == 0) {
					Messager.broadcastStringList(Messager.getStringList(
							ChatColor.translateAlternateColorCodes('&', "&c���� ��� ������ �ɷ��� Ȯ������ �ʾҽ��ϴ�."),
							ChatColor.translateAlternateColorCodes('&', "&c/ability yes�� /ability no ��ɾ�� �ɷ��� Ȯ�����ּ���.")));
				}
			} else {
				this.StopTimer(false);
			}
		}

		/**
		 * �ɷ��� �������� ��� �����ڰ� �ɷ��� �����ߴ����� ���θ� ��ȯ�մϴ�.
		 */
		private final boolean isEveryoneSelected() {
			for(Participant Key : getSelectors()) if(!hasDecided(Key)) return false;
			return true;
		}
		
		@Override
		public void onEnd() {
			Ended = true;
			onSelectEnd();
		}

		protected abstract void onSelectEnd();

		private boolean Ended = false;
		
		public boolean isEnded() {
			return Ended;
		}
		
	}

}
