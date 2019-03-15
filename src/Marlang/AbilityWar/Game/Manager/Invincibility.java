package Marlang.AbilityWar.Game.Manager;

import org.bukkit.ChatColor;

import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.Game.Games.Game;
import Marlang.AbilityWar.Game.Games.AbstractGame.Participant;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Library.SoundLib;
import Marlang.AbilityWar.Utils.Library.Packet.TitlePacket;
import Marlang.AbilityWar.Utils.Math.NumberUtil;
import Marlang.AbilityWar.Utils.Thread.TimerBase;

/**
 * �ʹ� ����
 * @author _Marlang ����
 */
public class Invincibility extends TimerBase {
	
	private Integer Duration = AbilityWarSettings.getInvincibilityDuration();
	private Game game;
	
	public Invincibility(Game game) {
		super(AbilityWarSettings.getInvincibilityDuration() * 60);
		this.game = game;
	}
	
	@Override
	public void onStart() {
		Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ &f" + NumberUtil.parseTimeString(Duration * 60) + "&a���� ����˴ϴ�."));
	}
	
	@Override
	public void TimerProcess(Integer Seconds) {
		if(Seconds == (Duration * 60) / 2) {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ &f" + NumberUtil.parseTimeString(Seconds) + " &a�Ŀ� �����˴ϴ�."));
		}
		

		if(Seconds <= 5 && Seconds >= 1) {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ &f" + NumberUtil.parseTimeString(Seconds) + " &a�Ŀ� �����˴ϴ�."));
			SoundLib.BLOCK_NOTE_BLOCK_HARP.broadcastSound();
		}
	}
	
	@Override
	public void onEnd() {
		game.setRestricted(false);
		Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ �����Ǿ����ϴ�."));
		SoundLib.ENTITY_ENDER_DRAGON_AMBIENT.broadcastSound();
		
		TitlePacket title = new TitlePacket(ChatColor.translateAlternateColorCodes('&', "&c&lWarning"),
				ChatColor.translateAlternateColorCodes('&', "&f�ʹ� ������ �����Ǿ����ϴ�."), 20, 60, 20);
		title.Broadcast();
		
		for(Participant participant : game.getParticipants()) {
			if(participant.hasAbility()) {
				participant.getAbility().setRestricted(false);
			}
		}
	}
	
}
