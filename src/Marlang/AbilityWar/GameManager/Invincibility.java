package Marlang.AbilityWar.GameManager;

import org.bukkit.ChatColor;
import org.bukkit.Sound;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.Utils.EffectUtil;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.NumberUtil;
import Marlang.AbilityWar.Utils.TimerBase;

/**
 * �ʹ� ����
 * @author _Marlang ����
 */
public class Invincibility extends TimerBase {
	
	static Integer Duration = AbilityWar.getSetting().getInvincibilityDuration();
	
	public Invincibility() {
		super(Duration * 60);
		Messager.sendMessage(Duration + "��");
	}
	
	public void setInvincibility() {
		this.StartTimer();
	}
	
	@Override
	public void TimerStart() {
		Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ &f" + Duration + "�� &a���� ����˴ϴ�."));
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
	}
	
}
