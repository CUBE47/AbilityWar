package Marlang.AbilityWar.Ability;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Utils.TimerBase;

abstract public class AbilityBase {
	
	Player player;
	String AbilityName;
	Rank Rank;
	String[] Explain;
	
	ArrayList<TimerBase> timers = new ArrayList<TimerBase>();
	
	boolean Restricted = true;
	
	public AbilityBase(String AbilityName, Rank Rank, String... Explain) {
		this.AbilityName = AbilityName;
		this.Rank = Rank;
		this.Explain = Explain;
	}
	
	abstract public void ActiveSkill(ActiveMaterialType mt, ActiveClickType ct);
	
	abstract public void PassiveSkill(Event event);
	
	/**
	 * �÷��̾� �ɷ� ������ ���
	 */
	public void DeleteAbility() {
		for(TimerBase timer : timers) {
			timer.StopTimer(true);
		}
		
		this.setPlayer(null);
	}
	
	public void registerTimer(TimerBase timer) {
		if(!timers.contains(timer)) {
			timers.add(timer);
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getAbilityName() {
		return AbilityName;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Rank getRank() {
		return Rank;
	}
	
	public String[] getExplain() {
		return Explain;
	}
	
	public void setExplain(String... Explain) {
		this.Explain = Explain;
	}
	
	public boolean isRestricted() {
		return Restricted;
	}
	
	public void setRestricted(boolean restricted) {
		Restricted = restricted;
	}
	
	public static enum ActiveClickType {
		RightClick, LeftClick;
	}
	
	public static enum ActiveMaterialType {
		
		Iron_Ingot(Material.IRON_INGOT),
		Gold_Ingot(Material.GOLD_INGOT);
		
		Material material;
		
		private ActiveMaterialType(Material material) {
			this.material = material;
		}
		
		public Material getMaterial() {
			return material;
		}
		
	}
	
	public static enum Rank {

		God(ChatColor.translateAlternateColorCodes('&', "&c�� ���")),
		S(ChatColor.translateAlternateColorCodes('&', "&dS ���")),
		A(ChatColor.translateAlternateColorCodes('&', "&aA ���")),
		B(ChatColor.translateAlternateColorCodes('&', "&bB ���")),
		C(ChatColor.translateAlternateColorCodes('&', "&eC ���")),
		D(ChatColor.translateAlternateColorCodes('&', "&7D ���"));
		
		String RankName;
		
		private Rank(String RankName) {
			this.RankName = RankName;
		}
		
		public String getRankName() {
			return RankName;
		}
		
	}
	
}
