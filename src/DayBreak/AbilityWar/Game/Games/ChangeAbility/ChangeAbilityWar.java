package DayBreak.AbilityWar.Game.Games.ChangeAbility;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import DayBreak.AbilityWar.AbilityWar;
import DayBreak.AbilityWar.Ability.AbilityList;
import DayBreak.AbilityWar.Config.AbilityWarSettings;
import DayBreak.AbilityWar.Game.Games.GameCreditEvent;
import DayBreak.AbilityWar.Game.Games.Mode.GameManifest;
import DayBreak.AbilityWar.Game.Games.Mode.WinnableGame;
import DayBreak.AbilityWar.Game.Manager.InfiniteDurability;
import DayBreak.AbilityWar.Utils.FireworkUtil;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Library.SoundLib;
import DayBreak.AbilityWar.Utils.Math.NumberUtil;
import DayBreak.AbilityWar.Utils.Thread.AbilityWarThread;
import DayBreak.AbilityWar.Utils.Thread.OverallTimer;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

/**
 * ü���� �ɷ� ����
 * @author DayBreak ����
 */
@GameManifest(Name = "ü���� �ɷ� ���� (Beta)", Description = { "��f���� �ð����� �ٲ�� �ɷ��� ������ �÷����ϴ� ���� �̱��� ����Դϴ�.", "��f��� �÷��̾�Դ� �������� ������ �־�����, ���� ������ ������ �Ҹ�˴ϴ�.", "��f������ ��� �Ҹ�Ǹ� ������ ���� ���ӿ��� Ż���մϴ�.", "��f��θ� Ż����Ű�� ������ 1������ ���� �÷��̾ �¸��մϴ�.", "", "��a�� ��f��ũ��Ʈ�� ������� �ʽ��ϴ�.",
														"��a�� ��f�Ϻ� ���Ǳװ� ���Ƿ� ����� �� �ֽ��ϴ�.", "", "��6�� ��fü���� �ɷ����� ���� ���Ǳװ� �ֽ��ϴ�. Config.yml�� Ȯ���غ�����."})
public class ChangeAbilityWar extends WinnableGame {
	
	public ChangeAbilityWar() {
		setRestricted(Invincible);
		registerEvent(EntityDamageEvent.class);
	}
	
	private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	private final Objective lifeObjective = scoreboard.registerNewObjective("����", "dummy");
	
	private final AbilityChanger changer = new AbilityChanger(this);
	
	private final boolean Invincible = AbilityWarSettings.getInvincibilityEnable();
	
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
				if(getParticipants().size() < 2) {
					AbilityWarThread.StopGame();
					Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c�ּ� ������ ���� �������� ���Ͽ� ������ �����մϴ�. &8(&72��&8)"));
				}
				break;
			case 5:
				broadcastPluginDescription();
				break;
			case 10:
				broadcastAbilityReady();
				break;
			case 13:
				scoreboardSetup();
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f��� �� ������ ���۵˴ϴ�."));
				break;
			case 16:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f������ &55&f�� �Ŀ� ���۵˴ϴ�."));
				SoundLib.BLOCK_NOTE_BLOCK_HARP.broadcastSound();
				break;
			case 17:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f������ &54&f�� �Ŀ� ���۵˴ϴ�."));
				SoundLib.BLOCK_NOTE_BLOCK_HARP.broadcastSound();
				break;
			case 18:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f������ &53&f�� �Ŀ� ���۵˴ϴ�."));
				SoundLib.BLOCK_NOTE_BLOCK_HARP.broadcastSound();
				break;
			case 19:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f������ &52&f�� �Ŀ� ���۵˴ϴ�."));
				SoundLib.BLOCK_NOTE_BLOCK_HARP.broadcastSound();
				break;
			case 20:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f������ &51&f�� �Ŀ� ���۵˴ϴ�."));
				SoundLib.BLOCK_NOTE_BLOCK_HARP.broadcastSound();
				break;
			case 21:
				GameStart();
				break;
		}
	}
	
	public void scoreboardSetup() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.setScoreboard(scoreboard);
		}
		lifeObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
		lifeObjective.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c����"));
		final int maxLife = AbilityWarSettings.ChangeAbilityWar_getLife();
		for(Participant p : getParticipants()) {
			Score score = lifeObjective.getScore(p.getPlayer().getName());
			score.setScore(maxLife);
		}
	}
	
	private final boolean Eliminate = AbilityWarSettings.ChangeAbilityWar_getEliminate();
	
	private final List<Participant> NoLife = new ArrayList<Participant>();
	
	@Override
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player Victim = e.getEntity();
		Participant VictimPart = getParticipant(Victim);
		if(VictimPart != null) {
			Score score = lifeObjective.getScore(Victim.getName());
			if(score.isScoreSet()) {
				if(score.getScore() >= 1) score.setScore(score.getScore() - 1);
				if(score.getScore() <= 0) {
					NoLife.add(VictimPart);
					if(Eliminate) getDeathManager().Eliminate(Victim);

					Participant hasLife = null;
					int count = 0;
					for(Participant p : getParticipants()) {
						if(!NoLife.contains(p)) {
							hasLife = p;
							count++;
						}
					}
					
					if(count == 1 && hasLife != null) {
						this.Victory(hasLife);
					}
				}
			}
			
			if(Victim.getKiller() != null) {
				SoundLib.ENTITY_ARROW_HIT_PLAYER.playSound(Victim.getKiller());
			}
		}
	}
	
	public void broadcastPlayerList() {
		int Count = 0;
		
        ArrayList<String> msg = new ArrayList<String>();
		
		msg.add(ChatColor.translateAlternateColorCodes('&', "&d==== &f���� ������ ��� &d===="));
		for(Participant p : getParticipants()) {
			Count++;
			msg.add(ChatColor.translateAlternateColorCodes('&', "&5" + Count + ". &f" + p.getPlayer().getName()));
		}
		msg.add(ChatColor.translateAlternateColorCodes('&', "&f�� �ο��� &5: &d" + Count + "��"));
		msg.add(ChatColor.translateAlternateColorCodes('&', "&d==========================="));
		
		Messager.broadcastStringList(msg);
	}
	
	public void broadcastPluginDescription() {
		ArrayList<String> msg = Messager.getStringList(
				ChatColor.translateAlternateColorCodes('&', "&5&lü����! &d&l�ɷ� &f&l����"),
				ChatColor.translateAlternateColorCodes('&', "&e�÷����� ���� &7: &f" + AbilityWar.getPlugin().getDescription().getVersion()),
				ChatColor.translateAlternateColorCodes('&', "&b��� ������ &7: &fDayBreak ����"),
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
				ChatColor.translateAlternateColorCodes('&', "&f�÷����ο� �� &d" + AbilityList.nameValues().size() + "��&f�� �ɷ��� ��ϵǾ� �ֽ��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&7���� ���۽� &fù��° �ɷ�&7�� �Ҵ�Ǹ�, ���� &f" + NumberUtil.parseTimeString(changer.getPeriod()) + "&7���� �ɷ��� ����˴ϴ�."));
		
		Messager.broadcastStringList(msg);
	}
	
	public void GameStart() {
		Messager.broadcastStringList(Messager.getStringList(
				ChatColor.translateAlternateColorCodes('&', "&d�������������������������������������������������������������������������"),
				ChatColor.translateAlternateColorCodes('&', "&f                              &5&lü����! &d&l�ɷ� &f&l����             "),
				ChatColor.translateAlternateColorCodes('&', "&f                                  ���� ����                             "),
				ChatColor.translateAlternateColorCodes('&', "&d�������������������������������������������������������������������������")));
		SoundLib.ENTITY_WITHER_SPAWN.broadcastSound();
		
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
		
		changer.StartTimer();
		
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
	protected void onVictory(Participant... participants) {
		Messager.clearChat();
		StringBuilder builder = new StringBuilder();
		builder.append(ChatColor.translateAlternateColorCodes('&', "&5&l�����&f: "));
		
		StringJoiner joiner = new StringJoiner("��f, ��d", "��d", "��f.");
		for(Participant participant : participants) {
			Player p = participant.getPlayer();
			SoundLib.UI_TOAST_CHALLENGE_COMPLETE.playSound(p);
			joiner.add(p.getName());
			new OverallTimer(5) {
				
				@Override
				protected void onStart() {}
				
				@Override
				protected void onEnd() {}
				
				@Override
				protected void TimerProcess(Integer Seconds) {
					FireworkUtil.spawnWinnerFirework(p.getEyeLocation().add(0, 1, 0));
				}
			}.setPeriod(8).StartTimer();
		}
		
		builder.append(joiner.toString());
		Messager.broadcastMessage(builder.toString());
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
		return null;
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
