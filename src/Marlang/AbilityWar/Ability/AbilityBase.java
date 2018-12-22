package Marlang.AbilityWar.Ability;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

abstract public class AbilityBase {
	
	Player player;
	String AbilityName;
	Rank Rank;
	String[] Explain;
	
	boolean Restricted = true;
	
	public AbilityBase(String AbilityName, Rank Rank, String... Explain) {
		this.AbilityName = AbilityName;
		this.Rank = Rank;
		this.Explain = Explain;
	}
	
	abstract public void ActiveSkill(ActiveMaterialType mt, ActiveClickType ct);
	
	abstract public void PassiveSkill(Event event);
	
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
		
		S(ChatColor.translateAlternateColorCodes('&', "&dS ��ũ")),
		A(ChatColor.translateAlternateColorCodes('&', "&aA ��ũ")),
		B(ChatColor.translateAlternateColorCodes('&', "&bB ��ũ")),
		C(ChatColor.translateAlternateColorCodes('&', "&eC ��ũ")),
		D(ChatColor.translateAlternateColorCodes('&', "&7D ��ũ"));
		
		String RankName;
		
		private Rank(String RankName) {
			this.RankName = RankName;
		}
		
		public String getRankName() {
			return RankName;
		}
		
	}
	
}
