package Marlang.AbilityWar.GameManager.Manager;

import org.bukkit.ChatColor;
import org.bukkit.Sound;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.Utils.AbilityWarThread;
import Marlang.AbilityWar.Utils.EffectUtil;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.NumberUtil;
import Marlang.AbilityWar.Utils.TimerBase;

/**
 * �ʹ� ����
 * @author _Marlang ����
 */
public class Invincibility extends TimerBase {
	
	static Integer Duration;
	
	public Invincibility() {
		super(AbilityWarSettings.getInvincibilityDuration() * 60);
		Duration = AbilityWarSettings.getInvincibilityDuration();
	}
	
	@Override
	public void TimerStart() {
		Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ &f" + NumberUtil.parseTimeString(Duration * 60) + "&a���� ����˴ϴ�."));
	}
	
	@Override
	public void TimerProcess(Integer Seconds) {
		if(Seconds == (Duration * 60) / 2) {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ &f" + NumberUtil.parseTimeString(Seconds) + " &a�Ŀ� �����˴ϴ�."));
		}
		

		if(Seconds <= 5 && Seconds >= 1) {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ &f" + NumberUtil.parseTimeString(Seconds) + " &a�Ŀ� �����˴ϴ�."));
			EffectUtil.broadcastSound(Sound.BLOCK_NOTE_HARP);
		}
	}
	
	@Override
	public void TimerEnd() {
		Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ �����Ǿ����ϴ�."));
		EffectUtil.broadcastTitle(
				ChatColor.translateAlternateColorCodes('&', "&c&lWarning"),
				ChatColor.translateAlternateColorCodes('&', "&f�ʹ� ������ �����Ǿ����ϴ�."));
		EffectUtil.broadcastSound(Sound.ENTITY_ENDERDRAGON_AMBIENT);
		
		for(AbilityBase Ability : AbilityWarThread.getGame().getAbilities().values()) {
			Ability.setRestricted(false);
		}
	}
	
}
