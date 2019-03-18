package Marlang.AbilityWar.Ability;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.ChatColor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AbilityManifest {

	/**
	 * �ɷ� �̸�
	 */
	public String Name();
	
	/**
	 * �ɷ� ��ũ
	 */
	public Rank Rank();

	public enum Rank {
		
		/**
		 * Special ���
		 */
		SPECIAL(ChatColor.translateAlternateColorCodes('&', "&5Special ���")),
		/**
		 * �� ���
		 */
		GOD(ChatColor.translateAlternateColorCodes('&', "&c�� ���")),
		/**
		 * S ���
		 */
		S(ChatColor.translateAlternateColorCodes('&', "&dS ���")),
		/**
		 * A ���
		 */
		A(ChatColor.translateAlternateColorCodes('&', "&aA ���")),
		/**
		 * B ���
		 */
		B(ChatColor.translateAlternateColorCodes('&', "&bB ���")),
		/**
		 * C ���
		 */
		C(ChatColor.translateAlternateColorCodes('&', "&eC ���")),
		/**
		 * D ���
		 */
		D(ChatColor.translateAlternateColorCodes('&', "&7D ���"));
		
		private String RankName;
		
		private Rank(String RankName) {
			this.RankName = RankName;
		}
		
		public String getRankName() {
			return RankName;
		}
		
	}

}
