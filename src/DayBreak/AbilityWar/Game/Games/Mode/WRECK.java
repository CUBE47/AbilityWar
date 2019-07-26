package DayBreak.AbilityWar.Game.Games.Mode;

import org.bukkit.ChatColor;

import DayBreak.AbilityWar.Config.AbilityWarSettings;
import DayBreak.AbilityWar.Utils.Messager;

public class WRECK {

	@SuppressWarnings("unused")
	private final AbstractGame game;
	
	public WRECK(AbstractGame game) {
		this.game = game;
	}
	
	private final boolean enabled = AbilityWarSettings.getWRECKEnable();
	
	public boolean isEnabled() {
		return enabled;
	}

	public void noticeIfEnabled() {
		if(enabled) {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cW&6R&eE&aC&bK &f��尡 Ȱ��ȭ�Ǿ����ϴ�!"));
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c��� �ɷ��� ��Ÿ���� 90% �����մϴ�."));
		}
	}
	
}
