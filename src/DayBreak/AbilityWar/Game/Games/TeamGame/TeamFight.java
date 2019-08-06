package DayBreak.AbilityWar.Game.Games.TeamGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import DayBreak.AbilityWar.AbilityWar;
import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Config.AbilityWarSettings;
import DayBreak.AbilityWar.Game.Games.GameCreditEvent;
import DayBreak.AbilityWar.Game.Games.Mode.GameManifest;
import DayBreak.AbilityWar.Game.Games.Mode.TeamGame;
import DayBreak.AbilityWar.Game.Manager.AbilityList;
import DayBreak.AbilityWar.Game.Manager.InfiniteDurability;
import DayBreak.AbilityWar.Game.Script.Script;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Library.SoundLib;
import DayBreak.AbilityWar.Utils.Thread.AbilityWarThread;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

@GameManifest(Name = "�� ����", Description = {"��f�ɷ��� ������ �� ���������� �÷����� �� �ֽ��ϴ�."})
public class TeamFight extends TeamGame {

	public TeamFight() {
		setRestricted(Invincible);
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
				if(getParticipants().size() > 0 && getParticipants().size() % 2 != 0) {
					AbilityWarThread.StopGame();
					Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c�� ������ 2�� �̻��� ¦�� �ο������� �÷����� �� �ֽ��ϴ�."));
				}
				break;
			case 5:
				broadcastPluginDescription();
				break;
			case 8:
				this.initializeTeam();
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
		
		if(this.isGameStarted()) {
			if(this.isParticipating(Victim)) {
				if(AbilityWarSettings.getEliminate()) {
					getDeathManager().Eliminate(Victim);
				}
				
				if(AbilityWarSettings.getAbilityRemoval()) {
					this.getParticipant(Victim).removeAbility();
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
				ChatColor.translateAlternateColorCodes('&', "&b������ &7: &fDayBreak ����"),
				ChatColor.translateAlternateColorCodes('&', "&9���ڵ� &7: &fDayBreak&7#5908"));
		
		GameCreditEvent event = new GameCreditEvent();
		Bukkit.getPluginManager().callEvent(event);
		
		for(String str : event.getCreditList()) {
			msg.add(str);
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
				ChatColor.translateAlternateColorCodes('&', "&e����������������������������������������������"),
				ChatColor.translateAlternateColorCodes('&', "&f             &cAbilityWar &f- &6�ɷ��� ����  "),
				ChatColor.translateAlternateColorCodes('&', "&f                    ���� ����                "),
				ChatColor.translateAlternateColorCodes('&', "&e����������������������������������������������")));
		
		this.GiveDefaultKit();
		
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
		}
		
		Script.RunAll(this);
		
		startGame();
	}
	
	/**
	 * �⺻ Ŷ ���� ����
	 */
	@Override
	public void GiveDefaultKit(Player p) {
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
				return TeamFight.this.getParticipants();
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
									ChatColor.translateAlternateColorCodes('&', "&e/ability yes &f���ɾ ����ϸ� �ɷ��� Ȯ���մϴ�."),
									ChatColor.translateAlternateColorCodes('&', "&e/ability no &f���ɾ ����ϸ� �ɷ��� ������ �� �ֽ��ϴ�.")));
						} catch (Exception e) {
							Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + "&f�Կ��� �ɷ��� �Ҵ��ϴ� ���� ������ �߻��Ͽ����ϴ�."));
							Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f������ �߻��� �ɷ�: &b" + abilityClass.getName()));
						}
					}
				} else {
					Messager.broadcastErrorMessage("��� ������ �ɷ��� ���� �������� ������ ���� ������ �����մϴ�.");
					AbilityWarThread.StopGame();
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
							e.printStackTrace();
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
	protected void onGameEnd() {
		HandlerList.unregisterAll(infiniteDurability);
	}

	@Override
	protected List<Team> setupTeams() {
		List<Participant> participants = getParticipants();
		int size = participants.size();
		
		Collections.shuffle(participants, new Random());
		
		List<Participant> blueMembers = new ArrayList<>(participants.subList(0, (size + 1) / 2));
		Team blueTeam = this.newTeam("��b�Ķ���", blueMembers);
		List<Participant> redMembers = new ArrayList<>(participants.subList((size + 1) / 2, size));
		Team redTeam = this.newTeam("��c������", redMembers);
		
		return Arrays.asList(blueTeam, redTeam);
	}
	
}