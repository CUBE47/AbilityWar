package Marlang.AbilityWar.GameManager.Object;

import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.HashMap;

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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.EventExecutor;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityBase.ClickType;
import Marlang.AbilityWar.Ability.AbilityBase.MaterialType;
import Marlang.AbilityWar.GameManager.Game.AbstractGame;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Validate;
import Marlang.AbilityWar.Utils.VersionCompat.PlayerCompat;

public class Participant implements EventExecutor {

	private static HashMap<String, Participant> participants = new HashMap<String, Participant>();
	private static AbstractGame lastGame;

	/**
	 * ������ ��ü�� ��ȯ�մϴ�. �ش� ���ӿ��� �̹� ��� �÷��̾ ������� ������� ������ ��ü�� ���� ��� ��ȯ�ϰ�, ���� ��쿡�� ����
	 * ������ ��ü�� ����� ��ȯ�մϴ�.
	 * 
	 * @param game   �������� ����
	 * @param player ��� �÷��̾�
	 * @return ������ ��ü
	 */
	public static Participant Construct(AbstractGame game, Player player) {
		Validate.NotNull(game, player);

		if (lastGame != null && !lastGame.equals(game)) {
			participants.clear();
		}

		if (participants.containsKey(player.getUniqueId().toString())) {
			return participants.get(player.getUniqueId().toString());
		} else {
			Participant participant = new Participant(game, player);
			participants.put(player.getUniqueId().toString(), participant);
			return participant;
		}
	}

	/**
	 * �÷��̾ ������� ������� ������ ��ü�� �ִ��� Ȯ���մϴ�.
	 * 
	 * @param player Ȯ���� �÷��̾�
	 * @return ������ ��ü ���� ����
	 */
	public static boolean checkParticipant(Player player) {
		return participants.containsKey(player.getUniqueId().toString());
	}

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
	public void transferAbility(Player to) {
		if(hasAbility() && this.game.isParticipating(to) && Participant.checkParticipant(to)) {
			AbilityBase Ability = getAbility();
			removeAbility();
			
			Participant target = Participant.Construct(this.game, to);
			
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
