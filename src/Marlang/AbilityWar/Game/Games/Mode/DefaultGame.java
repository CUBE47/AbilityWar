package Marlang.AbilityWar.Game.Games.Mode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityList;
import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.Game.Games.AbstractGame;
import Marlang.AbilityWar.Game.Games.GameManifest;
import Marlang.AbilityWar.Game.Manager.InfiniteDurability;
import Marlang.AbilityWar.Game.Script.Script;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Library.SoundLib;
import Marlang.AbilityWar.Utils.Thread.AbilityWarThread;
import Marlang.AbilityWar.Utils.Thread.TimerBase;

/**
 * ���� ���� Ŭ����
 * @author _Marlang ����
 */
@GameManifest(Name = "����", Description = { "��f�ɷ��� ���� �÷������� �⺻ �����Դϴ�." })
public class DefaultGame extends AbstractGame {
	
	private final static List<String> messages = new ArrayList<String>();
	
	public static void registerMessage(String... msg) {
		for(String m : msg) {
			messages.add(m);
		}
	}
	
	public DefaultGame() {
		setRestricted(Invincible);
		registerEvent(EntityDamageEvent.class);
	}
	
	private boolean Invincible = AbilityWarSettings.getInvincibilityEnable();
	
	private final InfiniteDurability infiniteDurability = new InfiniteDurability();
	
	private final TimerBase NoHunger = new TimerBase() {
		
		@Override
		public void onStart() {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a����� �������� ����˴ϴ�."));
		}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			for(Participant p : getParticipants()) {
				p.getPlayer().setFoodLevel(19);
			}
		}
		
		@Override
		public void onEnd() {}
	};
	
	@Override
	protected boolean gameCondition() {
		return true;
	}
	
	@Override
	protected void progressGame(Integer Seconds) {
		switch(Seconds) {
			case 1:
				broadcastPlayerList();
				if(getParticipants().size() < 1) {
					AbilityWarThread.stopGame();
					Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c�ּ� ������ ���� �������� ���Ͽ� ������ �����մϴ�."));
				}
				break;
			case 5:
				broadcastPluginDescription();
				break;
			case 10:
				if(AbilityWarSettings.getDrawAbility()) {
					broadcastAbilityReady();
				} else {
					this.setSeconds(this.getSeconds() + 4);
				}
				break;
			case 13:
				if(AbilityWarSettings.getDrawAbility()) {
					//�ɷ� �Ҵ� ����
					this.startAbilitySelect();
				}
				break;
			case 15:
				if(AbilityWarSettings.getDrawAbility()) {
					Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f��� �����ڰ� �ɷ��� &bȮ��&f�߽��ϴ�."));
				} else {
					Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f�ɷ��� ���� ������ ���� &b�ɷ�&f�� ��÷���� �ʽ��ϴ�."));
				}
				break;
			case 17:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e��� �� ������ ���۵˴ϴ�."));
				break;
			case 20:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e������ &c5&e�� �Ŀ� ���۵˴ϴ�."));
				SoundLib.BLOCK_NOTE_BLOCK_HARP.broadcastSound();
				break;
			case 21:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e������ &c4&e�� �Ŀ� ���۵˴ϴ�."));
				SoundLib.BLOCK_NOTE_BLOCK_HARP.broadcastSound();
				break;
			case 22:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e������ &c3&e�� �Ŀ� ���۵˴ϴ�."));
				SoundLib.BLOCK_NOTE_BLOCK_HARP.broadcastSound();
				break;
			case 23:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e������ &c2&e�� �Ŀ� ���۵˴ϴ�."));
				SoundLib.BLOCK_NOTE_BLOCK_HARP.broadcastSound();
				break;
			case 24:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e������ &c1&e�� �Ŀ� ���۵˴ϴ�."));
				SoundLib.BLOCK_NOTE_BLOCK_HARP.broadcastSound();
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
			if(this.isParticipating(Victim)) {
				if(AbilityWarSettings.getAbilityReveal()) {
					Participant victim = this.getParticipant(Victim);
					if(victim.hasAbility()) {
						AbilityBase Ability = victim.getAbility();
						
						String name = Ability.getName();
						if(name != null) {
							Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&c�ɷ�&f] &c" + Victim.getName() + "&f���� &e" + name + " &f�ɷ��̾����ϴ�!"));
						}
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
		for(Participant p : getParticipants()) {
			Count++;
			msg.add(ChatColor.translateAlternateColorCodes('&', "&a" + Count + ". &f" + p.getPlayer().getName()));
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
		
		for(String m : messages) {
			msg.add(m);
		}
		
		Messager.broadcastStringList(msg);
	}
	
	public void broadcastAbilityReady() {
		ArrayList<String> msg = Messager.getStringList(
				ChatColor.translateAlternateColorCodes('&', "&f�÷����ο� �� &b" + AbilityList.nameValues().size() + "��&f�� �ɷ��� ��ϵǾ� �ֽ��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&7�ɷ��� �������� �Ҵ��մϴ�..."));
		
		Messager.broadcastStringList(msg);
	}
	
	public void GameStart() {
		Messager.broadcastStringList(Messager.getStringList(
				ChatColor.translateAlternateColorCodes('&', "&e�������������������������������������������������������������������������"),
				ChatColor.translateAlternateColorCodes('&', "&f                            &cAbilityWar &f- &6�ɷ��� ����              "),
				ChatColor.translateAlternateColorCodes('&', "&f                                   ���� ����                            "),
				ChatColor.translateAlternateColorCodes('&', "&e�������������������������������������������������������������������������")));
		
		GiveDefaultKit();
		
		for(Participant p : getParticipants()) {
			if(AbilityWarSettings.getSpawnEnable()) {
				p.getPlayer().teleport(AbilityWarSettings.getSpawnLocation());
			}
		}
		
		if(AbilityWarSettings.getNoHunger()) {
			NoHunger.setPeriod(1);
			NoHunger.StartTimer();
		} else {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4����� ������&c�� ������� �ʽ��ϴ�."));
		}
		
		if(Invincible) {
			getInvincibility().Start(false);
		} else {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4�ʹ� ����&c�� ������� �ʽ��ϴ�."));
			for(Participant participant : this.getParticipants()) {
				if(participant.hasAbility()) {
					participant.getAbility().setRestricted(false);
				}
			}
		}
		
		if(AbilityWarSettings.getInfiniteDurability()) {
			Bukkit.getPluginManager().registerEvents(infiniteDurability, AbilityWar.getPlugin());
		} else {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4������ ������&c�� ������� �ʽ��ϴ�."));
		}
		
		for(World w : Bukkit.getWorlds()) {
			if(AbilityWarSettings.getClearWeather()) {
				w.setStorm(false);
			}
			
			if(AbilityWarSettings.getItemDrop()) {
				w.setGameRuleValue("keepInventory", "false");
			} else {
				w.setGameRuleValue("keepInventory", "true");
			}
		}
		
		Script.RunAll(this);
		
		setGameStarted(true);
	}
	
	/**
	 * �⺻ Ŷ ���� ����
	 */
	@Override
	public void GiveDefaultKit(Player p) {
		List<ItemStack> DefaultKit = AbilityWarSettings.getDefaultKit();

		if(AbilityWarSettings.getInventoryClear()) {
			p.getInventory().clear();
			p.updateInventory();
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
	
	@Override
	protected List<Player> setupPlayers() {
		List<Player> Players = new ArrayList<Player>();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!isSpectator(p.getName())) {
				Players.add(p);
			}
		}
		
		return Players;
	}
	
	@Override
	protected AbilitySelect setupAbilitySelect() {
		return new AbilitySelect() {
			
			@Override
			protected List<Participant> setupPlayers() {
				return DefaultGame.this.getParticipants();
			}
			
			private List<Class<? extends AbilityBase>> setupAbilities() {
				List<Class<? extends AbilityBase>> list = new ArrayList<>();
				for(String abilityName : AbilityList.nameValues()) {
					if(!AbilityWarSettings.isBlackListed(abilityName)) {
						list.add(AbilityList.getByString(abilityName));
					}
				}
				
				return list;
			}
			
			private List<Class<? extends AbilityBase>> abilities;
			
			@Override
			protected void drawAbility() {
				abilities = setupAbilities();
				
				if(getSelectors().size() <= abilities.size()) {
					Random random = new Random();
					
					for(Participant participant : getSelectors()) {
						Player p = participant.getPlayer();
						
						Class<? extends AbilityBase> abilityClass = abilities.get(random.nextInt(abilities.size()));
						try {
							participant.setAbility(abilityClass);
							abilities.remove(abilityClass);
							
							Messager.sendStringList(p, Messager.getStringList(
									ChatColor.translateAlternateColorCodes('&', "&a��ſ��� �ɷ��� �Ҵ�Ǿ����ϴ�. &e/ability check&f�� Ȯ�� �� �� �ֽ��ϴ�."),
									ChatColor.translateAlternateColorCodes('&', "&e/ability yes &f��ɾ ����ϸ� �ɷ��� Ȯ���մϴ�."),
									ChatColor.translateAlternateColorCodes('&', "&e/ability no &f��ɾ ����ϸ� �ɷ��� ������ �� �ֽ��ϴ�.")));
						} catch (Exception e) {
							Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + "&f�Կ��� �ɷ��� �Ҵ��ϴ� ���� ������ �߻��Ͽ����ϴ�."));
							Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f������ �߻��� �ɷ�: &b" + abilityClass.getName()));
						}
					}
				} else {
					Messager.broadcastErrorMessage("��� ������ �ɷ��� ���� �������� ������ ���� ������ �����մϴ�.");
					AbilityWarThread.stopGame();
					Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7������ �ʱ�ȭ�Ǿ����ϴ�."));
				}
			}
			
			@Override
			protected boolean changeAbility(Participant participant) {
				Player p = participant.getPlayer();
				
				if(abilities.size() > 0) {
					Random random = new Random();
					
					if(participant.hasAbility()) {
						Class<? extends AbilityBase> oldAbilityClass = participant.getAbility().getClass();
						Class<? extends AbilityBase> abilityClass = abilities.get(random.nextInt(abilities.size()));
						try {
							abilities.remove(abilityClass);
							abilities.add(oldAbilityClass);
							
							participant.setAbility(abilityClass);
							
							return true;
						} catch (Exception e) {
							Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + "&f���� �ɷ��� �����ϴ� ���� ������ �߻��Ͽ����ϴ�."));
							Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f������ �߻��� �ɷ�: &b" + abilityClass.getName()));
						}
					}
				} else {
					Messager.sendErrorMessage(p, "�ɷ��� ������ �� �����ϴ�.");
				}
				
				return false;
			}

			@Override
			protected void onSelectEnd() {}

			@Override
			protected int getChangeCount() {
				return 1;
			}

		};
	}
	
	@Override
	public void execute(Listener listener, Event event) throws EventException {
		if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;
			
			if(e.getEntity() instanceof Player) {
				if(this.getInvincibility().isInvincible()) {
					e.setCancelled(true);
				}
			}
		}
	}

	@Override
	protected void onEnd() {
		HandlerList.unregisterAll(infiniteDurability);
	}
	
}
