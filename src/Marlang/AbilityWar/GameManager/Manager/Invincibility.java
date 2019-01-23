package Marlang.AbilityWar.GameManager.Manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import Marlang.AbilityWar.API.Events.AbilityWarProgressEvent;
import Marlang.AbilityWar.API.Events.AbilityWarProgressEvent.Progress;
import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.GameManager.Game;
import Marlang.AbilityWar.Utils.AbilityWarThread;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.NumberUtil;
import Marlang.AbilityWar.Utils.TimerBase;
import Marlang.AbilityWar.Utils.Library.SoundLib;

/**
 * �ʹ� ����
 * @author _Marlang ����
 */
public class Invincibility extends TimerBase {
	
	Integer Duration = AbilityWarSettings.getInvincibilityDuration();
	Game game;
	
	public Invincibility(Game game) {
		super(AbilityWarSettings.getInvincibilityDuration() * 60);
		this.game = game;
	}
	
	@Override
	public void TimerStart() {
		Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ &f" + NumberUtil.parseTimeString(Duration * 60) + "&a���� ����˴ϴ�."));
	
		AbilityWarProgressEvent event = new AbilityWarProgressEvent(Progress.Invincibility_STARTED, game.getGameAPI());
		Bukkit.getPluginManager().callEvent(event);
	}
	
	@Override
	public void TimerProcess(Integer Seconds) {
		if(Seconds == (Duration * 60) / 2) {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ &f" + NumberUtil.parseTimeString(Seconds) + " &a�Ŀ� �����˴ϴ�."));
		}
		

		if(Seconds <= 5 && Seconds >= 1) {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ &f" + NumberUtil.parseTimeString(Seconds) + " &a�Ŀ� �����˴ϴ�."));
			SoundLib.BLOCK_NOTE_HARP.broadcastSound();
		}
	}
	
	@Override
	public void TimerEnd() {
		Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ �����Ǿ����ϴ�."));
		SoundLib.ENTITY_ENDERDRAGON_AMBIENT.broadcastSound();
		
		for(AbilityBase Ability : AbilityWarThread.getGame().getAbilities().values()) {
			Ability.setRestricted(false);
		}
		
		AbilityWarProgressEvent event = new AbilityWarProgressEvent(Progress.Invincibility_ENDED, game.getGameAPI());
		Bukkit.getPluginManager().callEvent(event);
	}
	
}
