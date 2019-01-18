package Marlang.AbilityWar.GameManager;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.API.GameAPI;
import Marlang.AbilityWar.API.Events.AbilityWarProgressEvent;
import Marlang.AbilityWar.API.Events.AbilityWarProgressEvent.Progress;
import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityList;
import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.GameManager.Manager.DeathManager;
import Marlang.AbilityWar.GameManager.Manager.Invincibility;
import Marlang.AbilityWar.Utils.AbilityWarThread;
import Marlang.AbilityWar.Utils.EffectUtil;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.TimerBase;

/**
 * ���� ���� Ŭ����
 * @author _Marlang ����
 */
public class Game extends Thread {
	
	int Seconds = 0;

	private ArrayList<Player> Players = new ArrayList<Player>();
	
	private static ArrayList<String> Spectators = new ArrayList<String>();
	
	private HashMap<Player, AbilityBase> Abilities = new HashMap<Player, AbilityBase>();
	
	private Invincibility invincibility = new Invincibility();
	
	private DeathManager deathManager = new DeathManager();
	
	private GameAPI gameAPI = new GameAPI(this);
	
	private boolean GameStarted = false;
	
	TimerBase NoHunger = new TimerBase() {
		
		@Override
		public void TimerStart() {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a����� �������� ����˴ϴ�."));
		}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			for(Player p : getPlayers()) {
				p.setFoodLevel(19);
			}
		}
		
		@Override
		public void TimerEnd() {}
	};
	
	@Override
	public void run() {
		if(AbilityWarThread.getAbilitySelect() == null) {
			Seconds++;
			GameProgress(Seconds);
		}
	}
	
	public void GameProgress(Integer Seconds) {
		switch(Seconds) {
			case 1:
				SetupPlayers();
				broadcastPlayerList();
				if(Players.size() < 1) {
					AbilityWarThread.toggleGameTask(false);
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
					readyAbility();
					
					AbilityWarProgressEvent event = new AbilityWarProgressEvent(Progress.AbilitySelect_STARTED, getGameAPI());
					Bukkit.getPluginManager().callEvent(event);
				}
				break;
			case 15:
				if(AbilityWarSettings.getDrawAbility()) {
					Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "��� �÷��̾ �ɷ��� &bȮ��&f�߽��ϴ�."));
					
					AbilityWarProgressEvent event = new AbilityWarProgressEvent(Progress.AbilitySelect_FINISHED, getGameAPI());
					Bukkit.getPluginManager().callEvent(event);
				} else {
					Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f�ɷ��� ���� ������ ���� &b�ɷ�&f�� ��÷���� �ʽ��ϴ�."));
				}
				break;
			case 17:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e��� �� ������ ���۵˴ϴ�."));
				break;
			case 20:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e������ &c5&e�� �Ŀ� ���۵˴ϴ�."));
				EffectUtil.broadcastSound(Sound.BLOCK_NOTE_HARP);
				break;
			case 21:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e������ &c4&e�� �Ŀ� ���۵˴ϴ�."));
				EffectUtil.broadcastSound(Sound.BLOCK_NOTE_HARP);
				break;
			case 22:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e������ &c3&e�� �Ŀ� ���۵˴ϴ�."));
				EffectUtil.broadcastSound(Sound.BLOCK_NOTE_HARP);
				break;
			case 23:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e������ &c2&e�� �Ŀ� ���۵˴ϴ�."));
				EffectUtil.broadcastSound(Sound.BLOCK_NOTE_HARP);
				break;
			case 24:
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e������ &c1&e�� �Ŀ� ���۵˴ϴ�."));
				EffectUtil.broadcastSound(Sound.BLOCK_NOTE_HARP);
				break;
			case 25:
				GameStart();
				
				AbilityWarProgressEvent event = new AbilityWarProgressEvent(Progress.Game_STARTED, getGameAPI());
				Bukkit.getPluginManager().callEvent(event);
				break;
		}
	}
	
	public void broadcastPlayerList() {
		int Count = 0;
		
        ArrayList<String> msg = new ArrayList<String>();
		
		msg.add(ChatColor.translateAlternateColorCodes('&', "&6==== &e���� ������ ��� &6===="));
		for(Player p : Players) {
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
		Bukkit.getPluginManager().registerEvents(getDeathManager(), AbilityWar.getPlugin());
		
		GiveDefaultKits();
		
		for(Player p : Players) {
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
		
		if(AbilityWarSettings.getInvincibilityEnable()) {
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
		
		setGameStarted(true);
		
		Messager.broadcastStringList(Messager.getStringList(
				ChatColor.translateAlternateColorCodes('&', "&e�������������������������������������������������������������������������"),
				ChatColor.translateAlternateColorCodes('&', "&f                            &cAbilityWar &f- &6�ɷ��� ����              "),
				ChatColor.translateAlternateColorCodes('&', "&f                                   ���� ����                            "),
				ChatColor.translateAlternateColorCodes('&', "&e�������������������������������������������������������������������������")));
	}

	/**
	 * �⺻ Ŷ ��ü ����
	 */
	public void GiveDefaultKits() {
		ArrayList<ItemStack> DefaultKit = AbilityWarSettings.getDefaultKit();
		
		for(Player p : Players) {
			if(AbilityWarSettings.getInventoryClear()) {
				p.getInventory().clear();
			}
			
			for(ItemStack is : DefaultKit) {
				p.getInventory().addItem(is);
			}
			
			p.setLevel(0);
			if(AbilityWarSettings.getStartLevel() > 0) {
				p.giveExpLevels(AbilityWarSettings.getStartLevel());
				EffectUtil.sendSound(p, Sound.ENTITY_PLAYER_LEVELUP);
			}
		}
	}

	/**
	 * �⺻ Ŷ ���� ����
	 */
	public void GiveDefaultKits(Player p) {
		ArrayList<ItemStack> DefaultKit = AbilityWarSettings.getDefaultKit();

		if(AbilityWarSettings.getInventoryClear()) {
			p.getInventory().clear();
		}
		
		for(ItemStack is : DefaultKit) {
			p.getInventory().addItem(is);
		}
		
		p.setLevel(0);
		if(AbilityWarSettings.getStartLevel() > 0) {
			p.giveExpLevels(AbilityWarSettings.getStartLevel());
			EffectUtil.sendSound(p, Sound.ENTITY_PLAYER_LEVELUP);
		}
	}
	
	public void readyAbility() {
		AbilityWarThread.toggleAbilitySelectTask(true);
		AbilityWarThread.getAbilitySelect().randomAbilityToAll();
	}
	
	public Invincibility getInvincibility() {
		return invincibility;
	}
	
	public DeathManager getDeathManager() {
		return deathManager;
	}
	
	public ArrayList<Player> getPlayers() {
		return Players;
	}
	
	public HashMap<Player, AbilityBase> getAbilities() {
		return Abilities;
	}
	
	public void addAbility(AbilityBase Ability) {
		Abilities.put(Ability.getPlayer(), Ability);
	}
	
	public void removeAbility(Player p) {
		if(Abilities.containsKey(p)) {
			AbilityBase Ability = Abilities.get(p);
			Ability.DeleteAbility();
			Abilities.remove(p);
		}
	}
	
	public void SetupPlayers() {
		ArrayList<Player> Players = new ArrayList<Player>();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!getSpectators().contains(p.getName())) {
				Players.add(p);
			}
		}
		
		this.Players = Players;
	}
	
	public boolean isGameStarted() {
		return GameStarted;
	}
	
	public void setGameStarted(boolean gameStarted) {
		GameStarted = gameStarted;
	}
	
	public static ArrayList<String> getSpectators() {
		return Spectators;
	}
	
	public void toggleAbilityRestrict(boolean bool) {
		for(AbilityBase a : Abilities.values()) {
			a.setRestricted(bool);
		}
	}
	
	public GameAPI getGameAPI() {
		return gameAPI;
	}
	
}
