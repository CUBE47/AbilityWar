package Marlang.AbilityWar.GameManager.Game;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityList;
import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.GameManager.Manager.AbilitySelect;
import Marlang.AbilityWar.GameManager.Manager.Invincibility;
import Marlang.AbilityWar.GameManager.Script.Script;
import Marlang.AbilityWar.Utils.AbilityWarThread;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.TimerBase;
import Marlang.AbilityWar.Utils.Library.SoundLib;

/**
 * ���� ���� Ŭ����
 * @author _Marlang ����
 */
public class Game extends AbstractGame {
	
	public Game() {
		setRestricted(Invincible);
		registerEvent(EntityDamageEvent.class);
	}
	
	private boolean Invincible = AbilityWarSettings.getInvincibilityEnable();
	
	private Invincibility invincibility = new Invincibility(this);
	
	TimerBase NoHunger = new TimerBase() {
		
		@Override
		public void TimerStart(Data<?>... args) {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a����� �������� ����˴ϴ�."));
		}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			for(Player p : getParticipants()) {
				p.setFoodLevel(19);
			}
		}
		
		@Override
		public void TimerEnd() {}
	};
	
	@Override
	protected boolean gameCondition() {
		return getAbilitySelect() == null || (getAbilitySelect() != null && getAbilitySelect().isEnded());
	}
	
	@Override
	protected void progressGame(Integer Seconds) {
		switch(Seconds) {
			case 1:
				broadcastPlayerList();
				if(getParticipants().size() < 1) {
					AbilityWarThread.stopGame();
					Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c�ּ� �÷��̾� ���� �������� ���Ͽ� ������ �����մϴ�."));
				}
				break;
			case 5:
				broadcastPluginDescription();
				break;
			case 10:
				if(AbilityWarSettings.getDrawAbility()) {
					broadcastAbilityReady();
				} else {
					this.Seconds += 4;
				}
				break;
			case 13:
				if(AbilityWarSettings.getDrawAbility()) {
					//�ɷ� �Ҵ� ����
					this.setAbilitySelect(setupAbilitySelect());
				}
				break;
			case 15:
				if(AbilityWarSettings.getDrawAbility()) {
					Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f��� �÷��̾ �ɷ��� &bȮ��&f�߽��ϴ�."));
				} else {
					Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f�ɷ��� ���� ������ ���� &b�ɷ�&f�� ��÷���� �ʽ��ϴ�."));
				}
				break;
			case 17:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e��� �� ������ ���۵˴ϴ�."));
				break;
			case 20:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e������ &c5&e�� �Ŀ� ���۵˴ϴ�."));
				SoundLib.BLOCK_NOTE_HARP.broadcastSound();
				break;
			case 21:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e������ &c4&e�� �Ŀ� ���۵˴ϴ�."));
				SoundLib.BLOCK_NOTE_HARP.broadcastSound();
				break;
			case 22:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e������ &c3&e�� �Ŀ� ���۵˴ϴ�."));
				SoundLib.BLOCK_NOTE_HARP.broadcastSound();
				break;
			case 23:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e������ &c2&e�� �Ŀ� ���۵˴ϴ�."));
				SoundLib.BLOCK_NOTE_HARP.broadcastSound();
				break;
			case 24:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e������ &c1&e�� �Ŀ� ���۵˴ϴ�."));
				SoundLib.BLOCK_NOTE_HARP.broadcastSound();
				break;
			case 25:
				GameStart();
				break;
		}
	}
	
	@Override
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player Victim = e.getEntity();
		Player Killer = Victim.getKiller();
		if(Victim.getLastDamageCause() != null) {
			DamageCause Cause = Victim.getLastDamageCause().getCause();

			if(Killer != null) {
				e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&a" + Killer.getName() + "&f���� &c" + Victim.getName() + "&f���� �׿����ϴ�."));
			} else {
				if(Cause.equals(DamageCause.CONTACT)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� ��� �׾����ϴ�."));
				} else if(Cause.equals(DamageCause.FALL)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� ������ �׾����ϴ�."));
				} else if(Cause.equals(DamageCause.FALLING_BLOCK)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� �������� ��Ͽ� �¾� �׾����ϴ�."));
				} else if(Cause.equals(DamageCause.SUFFOCATION)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� ���� �׾����ϴ�."));
				} else if(Cause.equals(DamageCause.DROWNING)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� ���� ���� �׾����ϴ�."));
				} else if(Cause.equals(DamageCause.ENTITY_EXPLOSION)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� �����Ͽ����ϴ�."));
				} else if(Cause.equals(DamageCause.LAVA)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� ��Ͽ� ���� �׾����ϴ�."));
				} else if(Cause.equals(DamageCause.FIRE) || Cause.equals(DamageCause.FIRE_TICK)) {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� �븩�븩�ϰ� ���������ϴ�."));
				} else {
					e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� �׾����ϴ�."));
				}
			}
		} else {
			e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + Victim.getName() + "&f���� �׾����ϴ�."));
		}

		if(this.isGameStarted()) {
			if(AbilityWarSettings.getItemDrop()) {
				e.setKeepInventory(false);
			} else {
				e.setKeepInventory(true);
			}
			if(this.getParticipants().contains(Victim)) {
				if(AbilityWarSettings.getAbilityReveal()) {
					if(this.getAbilities().containsKey(Victim)) {
						AbilityBase Ability = this.getAbilities().get(Victim);
						Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&c�ɷ�&f] &c" + Victim.getName() + "&f���� &e" + Ability.getAbilityName() + " &f�ɷ��̾����ϴ�!"));
					} else {
						Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&c�ɷ�&f] &c" + Victim.getName() + "&f���� �ɷ��� �����ϴ�!"));
					}
				}
				
				if(AbilityWarSettings.getEliminate()) {
					getDeathManager().Eliminate(Victim);
				}
			}
		}
	}
	
	public void broadcastPlayerList() {
		int Count = 0;
		
        ArrayList<String> msg = new ArrayList<String>();
		
		msg.add(ChatColor.translateAlternateColorCodes('&', "&6==== &e���� ������ ��� &6===="));
		for(Player p : getParticipants()) {
			Count++;
			msg.add(ChatColor.translateAlternateColorCodes('&', "&a" + Count + ". &f" + p.getName()));
		}
		msg.add(ChatColor.translateAlternateColorCodes('&', "&e�� �ο��� : " + Count + "��"));
		msg.add(ChatColor.translateAlternateColorCodes('&', "&6==========================="));
		
		Messager.broadcastStringList(msg);
	}
	
	public void broadcastPluginDescription() {
		ArrayList<String> msg = Messager.getStringList(
				ChatColor.translateAlternateColorCodes('&', "&cAbilityWar &f- &6�ɷ��� ����"),
				ChatColor.translateAlternateColorCodes('&', "&e���� &7: &f" + AbilityWar.getPlugin().getDescription().getVersion()),
				ChatColor.translateAlternateColorCodes('&', "&b������ &7: &f_Marlang ����"),
				ChatColor.translateAlternateColorCodes('&', "&9���ڵ� &7: &f����&7#5908"));
		
		Messager.broadcastStringList(msg);
	}
	
	public void broadcastAbilityReady() {
		ArrayList<String> msg = Messager.getStringList(
				ChatColor.translateAlternateColorCodes('&', "&f�÷����ο� �� &b" + AbilityList.values().size() + "��&f�� �ɷ��� ��ϵǾ� �ֽ��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&7�ɷ��� �������� �Ҵ��մϴ�..."));
		
		Messager.broadcastStringList(msg);
	}
	
	public void GameStart() {
		Messager.broadcastStringList(Messager.getStringList(
				ChatColor.translateAlternateColorCodes('&', "&e�������������������������������������������������������������������������"),
				ChatColor.translateAlternateColorCodes('&', "&f                            &cAbilityWar &f- &6�ɷ��� ����              "),
				ChatColor.translateAlternateColorCodes('&', "&f                                   ���� ����                            "),
				ChatColor.translateAlternateColorCodes('&', "&e�������������������������������������������������������������������������")));
		
		GiveDefaultKits();
		
		for(Player p : getParticipants()) {
			if(AbilityWarSettings.getSpawnEnable()) {
				p.teleport(AbilityWarSettings.getSpawnLocation());
			}
		}
		
		if(AbilityWarSettings.getNoHunger()) {
			NoHunger.setPeriod(1);
			NoHunger.StartTimer();
		} else {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4����� ������&c�� ������� �ʽ��ϴ�."));
		}
		
		if(Invincible) {
			invincibility.StartTimer();
		} else {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4�ʹ� ����&c�� ������� �ʽ��ϴ�."));
			for(AbilityBase Ability : AbilityWarThread.getGame().getAbilities().values()) {
				Ability.setRestricted(false);
			}
		}
		
		if(AbilityWarSettings.getClearWeather()) {
			for(World w : Bukkit.getWorlds()) {
				w.setStorm(false);
			}
		}
		
		Script.RunAll();
		
		setGameStarted(true);
	}

	/**
	 * �⺻ Ŷ ��ü ����
	 */
	public void GiveDefaultKits() {
		List<ItemStack> DefaultKit = AbilityWarSettings.getDefaultKit();
		
		for(Player p : getParticipants()) {
			if(AbilityWarSettings.getInventoryClear()) {
				p.getInventory().clear();
			}
			
			for(ItemStack is : DefaultKit) {
				p.getInventory().addItem(is);
			}
			
			p.setLevel(0);
			if(AbilityWarSettings.getStartLevel() > 0) {
				p.giveExpLevels(AbilityWarSettings.getStartLevel());
				SoundLib.ENTITY_PLAYER_LEVELUP.playSound(p);
			}
		}
	}

	/**
	 * �⺻ Ŷ ���� ����
	 */
	public void GiveDefaultKits(Player p) {
		List<ItemStack> DefaultKit = AbilityWarSettings.getDefaultKit();

		if(AbilityWarSettings.getInventoryClear()) {
			p.getInventory().clear();
		}
		
		for(ItemStack is : DefaultKit) {
			p.getInventory().addItem(is);
		}
		
		p.setLevel(0);
		if(AbilityWarSettings.getStartLevel() > 0) {
			p.giveExpLevels(AbilityWarSettings.getStartLevel());
			SoundLib.ENTITY_PLAYER_LEVELUP.playSound(p);
		}
	}
	
	public Invincibility getInvincibility() {
		return invincibility;
	}
	
	@Override
	protected List<Player> setupParticipants() {
		List<Player> Participants = new ArrayList<Player>();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!getSpectators().contains(p.getName())) {
				Participants.add(p);
			}
		}
		
		return Participants;
	}
	
	@Override
	protected AbilitySelect setupAbilitySelect() {
		return new AbilitySelect() {
			
			@Override
			protected List<Player> setupPlayers() {
				return getParticipants();
			}
			
			@Override
			protected void drawAbility() {
				Abilities.clear();
				
				for(String name : AbilityList.values()) {
					if(!AbilityWarSettings.getBlackList().contains(name)) {
						Abilities.add(AbilityList.getByString(name));
					}
				}
				
				if(getMap().size() <= Abilities.size()) {
					Random random = new Random();
					
					for(Player p : getMap().keySet()) {
						try {
							Class<? extends AbilityBase> abilityClass = Abilities.get(random.nextInt(Abilities.size()));
							Abilities.remove(abilityClass);
							Constructor<? extends AbilityBase> constructor = abilityClass.getConstructor(Player.class);
							AbilityBase Ability = constructor.newInstance(p);
							Game.this.addAbility(Ability);
							
							Messager.sendStringList(p, Messager.getStringList(
									ChatColor.translateAlternateColorCodes('&', "&a��ſ��� �ɷ��� �Ҵ�Ǿ����ϴ�. &e/ability check&f�� Ȯ�� �� �� �ֽ��ϴ�."),
									ChatColor.translateAlternateColorCodes('&', "&e/ability yes &f��ɾ ����ϸ� �ɷ��� Ȯ���մϴ�."),
									ChatColor.translateAlternateColorCodes('&', "&e/ability no &f��ɾ ����ϸ� 1ȸ�� ���� �ɷ��� ������ �� �ֽ��ϴ�.")));
						} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException
								| IllegalArgumentException | InvocationTargetException e) {
							Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + "&f�Կ��� �ɷ��� �Ҵ��ϴ� ���� ������ �߻��Ͽ����ϴ�."));
						}
					}
				} else {
					Messager.broadcastErrorMessage("��� ������ �ɷ��� ���� �÷��̾��� ������ ���� ������ �����մϴ�.");
					AbilityWarThread.stopGame();
					Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7������ �ʱ�ȭ�Ǿ����ϴ�."));
				}
			}
			
			@Override
			public void changeAbility(Player p) {
				if(Abilities.size() > 0) {
					Random random = new Random();
					
					AbilityBase oldAbility = Game.this.getAbilities().get(p);
					if(oldAbility != null) {
						try {
							Class<? extends AbilityBase> abilityClass = Abilities.get(random.nextInt(Abilities.size()));
							Abilities.remove(abilityClass);
							Abilities.add(oldAbility.getClass());
							Constructor<? extends AbilityBase> constructor = abilityClass.getConstructor(Player.class);
							AbilityBase Ability = constructor.newInstance(p);
							
							Game.this.removeAbility(p);
							Game.this.addAbility(Ability);
							
							Messager.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "&a����� �ɷ��� ����Ǿ����ϴ�. &e/ability check&f�� Ȯ�� �� �� �ֽ��ϴ�."));
							
							decideAbility(p, false);
						} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException
								| IllegalArgumentException | InvocationTargetException e) {
							Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + "&f�Կ��� �ɷ��� �����ϴ� ���� ������ �߻��Ͽ����ϴ�."));
						}
					}
				} else {
					Messager.sendErrorMessage(p, "�ɷ��� ������ �� �����ϴ�.");
				}
			}

			@Override
			protected boolean endCondition() {
				for(Player Key : getMap().keySet()) {
					if(!hasDecided(Key)) {
						return false;
					}
				}
				
				return true;
			}
			
		};
	}
	
	@Override
	public void execute(Listener listener, Event event) throws EventException {
		if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;
			
			if(e.getEntity() instanceof Player) {
				if(this.getInvincibility().isTimerRunning()) {
					e.setCancelled(true);
				}
			}
		}
	}
	
}
