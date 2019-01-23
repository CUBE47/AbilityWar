package Marlang.AbilityWar.Ability.Timer;

import org.bukkit.ChatColor;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.NumberUtil;
import Marlang.AbilityWar.Utils.TimerBase;
import Marlang.AbilityWar.Utils.Library.SoundLib;

public class CooldownTimer extends TimerBase {
	
	AbilityBase Ability;
	Integer Cool;
	String AbilityName = "";

	public CooldownTimer(AbilityBase Ability, Integer Cool) {
		super(Cool);
		this.Ability = Ability;
		this.Cool = Cool;
	}

	public CooldownTimer(AbilityBase Ability, Integer Cool, String AbilityName) {
		super(Cool);
		this.Ability = Ability;
		this.Cool = Cool;
		this.AbilityName = AbilityName;
	}
	
	public boolean isCooldown() {
		if(isTimerRunning()) {
			if(!AbilityName.isEmpty()) {
				Messager.sendMessage(Ability.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&c" + AbilityName + " ��Ÿ�� &f" + NumberUtil.parseTimeString(this.getTempCount())));
			} else {
				Messager.sendMessage(Ability.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&c��Ÿ�� &f" + NumberUtil.parseTimeString(this.getTempCount())));
			}
		}
		
		return isTimerRunning();
	}
	
	@Override
	public void TimerStart() {
		
	}
	
	@Override
	public void TimerProcess(Integer Seconds) {
		
		boolean showed = false;
		
		if(Seconds == (Cool / 2)) {
			showed = true;
			if(!AbilityName.isEmpty()) {
				Messager.sendMessage(Ability.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&c" + AbilityName + " ��Ÿ�� &f" + NumberUtil.parseTimeString(this.getTempCount())));
			} else {
				Messager.sendMessage(Ability.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&c��Ÿ�� &f" + NumberUtil.parseTimeString(this.getTempCount())));
			}
			SoundLib.BLOCK_NOTE_HAT.playSound(Ability.getPlayer());
		}
		
		if(Seconds <= 5 && Seconds >= 1) {
			if(!showed) {
				if(!AbilityName.isEmpty()) {
					Messager.sendMessage(Ability.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&c" + AbilityName + " ��Ÿ�� &f" + NumberUtil.parseTimeString(this.getTempCount())));
				} else {
					Messager.sendMessage(Ability.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&c��Ÿ�� &f" + NumberUtil.parseTimeString(this.getTempCount())));
				}
				SoundLib.BLOCK_NOTE_HAT.playSound(Ability.getPlayer());
			}
		}
	}
	
	@Override
	public void TimerEnd() {
		Ability.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a�ɷ��� �ٽ� ����� �� �ֽ��ϴ�."));
	}
	
}
