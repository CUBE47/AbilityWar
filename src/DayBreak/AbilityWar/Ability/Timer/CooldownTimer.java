package DayBreak.AbilityWar.Ability.Timer;

import org.bukkit.ChatColor;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Library.SoundLib;
import DayBreak.AbilityWar.Utils.Library.Packet.ActionbarPacket;
import DayBreak.AbilityWar.Utils.Math.NumberUtil;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

/**
 * Cooldown Timer (��Ÿ�� Ÿ�̸�)
 * @author DayBreak ����
 */
public class CooldownTimer extends TimerBase {
	
	/**
	 * ��Ÿ�� �ʱ�ȭ
	 */
	public static void ResetCool() {
		TimerBase.StopTasks(CooldownTimer.class);
	}
	
	private final AbilityBase Ability;
	private final int Cool;
	private String AbilityName = "";

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
				Messager.sendMessage(Ability.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&c" + AbilityName + " ��Ÿ�� &f" + NumberUtil.parseTimeString(this.getCount())));
			} else {
				Messager.sendMessage(Ability.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&c��Ÿ�� &f" + NumberUtil.parseTimeString(this.getCount())));
			}
		}
		
		return isTimerRunning();
	}
	
	@Override
	public CooldownTimer setPeriod(int Period) {
		super.setPeriod(Period);
		return this;
	}
	
	@Override
	public void onStart() {}
	
	@Override
	public void TimerProcess(Integer Seconds) {
		ActionbarPacket actionbar;
		if(!AbilityName.isEmpty()) {
			actionbar = new ActionbarPacket(ChatColor.translateAlternateColorCodes('&', "&c" + AbilityName + " ��Ÿ�� &f: &6" + NumberUtil.parseTimeString(this.getCount())), 0, 25, 0);
			
			if(Seconds == (Cool / 2)) {
				SoundLib.BLOCK_NOTE_BLOCK_HAT.playSound(Ability.getPlayer());
				Messager.sendMessage(Ability.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&c" + AbilityName + " ��Ÿ�� &f" + NumberUtil.parseTimeString(this.getCount())));
			} else if(Seconds <= 5 && Seconds >= 1) {
				SoundLib.BLOCK_NOTE_BLOCK_HAT.playSound(Ability.getPlayer());
				Messager.sendMessage(Ability.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&c" + AbilityName + " ��Ÿ�� &f" + NumberUtil.parseTimeString(this.getCount())));
			}
		} else {
			actionbar = new ActionbarPacket(ChatColor.translateAlternateColorCodes('&', "&c��Ÿ�� &f: &6" + NumberUtil.parseTimeString(this.getCount())), 0, 25, 0);
			
			if(Seconds == (Cool / 2)) {
				SoundLib.BLOCK_NOTE_BLOCK_HAT.playSound(Ability.getPlayer());
				Messager.sendMessage(Ability.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&c��Ÿ�� &f" + NumberUtil.parseTimeString(this.getCount())));
			} else if(Seconds <= 5 && Seconds >= 1) {
				SoundLib.BLOCK_NOTE_BLOCK_HAT.playSound(Ability.getPlayer());
				Messager.sendMessage(Ability.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&c��Ÿ�� &f" + NumberUtil.parseTimeString(this.getCount())));
			}
		}
		
		actionbar.Send(Ability.getPlayer());
	}
	
	@Override
	public void onEnd() {
		ActionbarPacket actionbar = new ActionbarPacket(ChatColor.translateAlternateColorCodes('&', "&a�ɷ��� �ٽ� ����� �� �ֽ��ϴ�."), 0, 50, 0);
		actionbar.Send(Ability.getPlayer());
		Ability.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a�ɷ��� �ٽ� ����� �� �ֽ��ϴ�."));
	}
	
}
