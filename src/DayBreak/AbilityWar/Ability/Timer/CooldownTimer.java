package DayBreak.AbilityWar.Ability.Timer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Library.SoundLib;
import DayBreak.AbilityWar.Utils.Library.Packet.ActionbarPacket;
import DayBreak.AbilityWar.Utils.Math.NumberUtil;
import DayBreak.AbilityWar.Utils.Thread.AbilityWarThread;
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
	private String AbilityName = "";
	private boolean actionbarNotice = true;

	public CooldownTimer(AbilityBase Ability, Integer Cool) {
		super((AbilityWarThread.isGameTaskRunning() && AbilityWarThread.getGame().getWRECK().isEnabled()) ? (int)(Cool / 10) : Cool);
		this.Ability = Ability;
	}

	public CooldownTimer(AbilityBase Ability, Integer Cool, String AbilityName) {
		this(Ability, Cool);
		this.AbilityName = AbilityName;
	}

	public CooldownTimer setActionbarNotice(boolean bool) {
		this.actionbarNotice = bool;
		return this;
	}
	
	public boolean isCooldown() {
		if(isTimerRunning()) {
			Player target = Ability.getPlayer();
			if(target != null) {
				if(!AbilityName.isEmpty()) {
					Messager.sendMessage(target, ChatColor.translateAlternateColorCodes('&', "&c" + AbilityName + " ��Ÿ�� &f" + NumberUtil.parseTimeString(this.getCount())));
				} else {
					Messager.sendMessage(target, ChatColor.translateAlternateColorCodes('&', "&c��Ÿ�� &f" + NumberUtil.parseTimeString(this.getCount())));
				}
			}
		}
		
		return isTimerRunning();
	}

	@Override
	public void onStart() {}
	
	@Override
	public void TimerProcess(Integer Seconds) {
		Player target = Ability.getPlayer();
		if(target != null) {
			ActionbarPacket actionbar;
			if(!AbilityName.isEmpty()) {
				actionbar = new ActionbarPacket(ChatColor.translateAlternateColorCodes('&', "&c" + AbilityName + " ��Ÿ�� &f: &6" + NumberUtil.parseTimeString(this.getCount())), 0, 25, 0);
				
				if(Seconds == (getMaxCount() / 2)) {
					SoundLib.BLOCK_NOTE_BLOCK_HAT.playSound(target);
					Messager.sendMessage(target, ChatColor.translateAlternateColorCodes('&', "&c" + AbilityName + " ��Ÿ�� &f" + NumberUtil.parseTimeString(this.getCount())));
				} else if(Seconds <= 5 && Seconds >= 1) {
					SoundLib.BLOCK_NOTE_BLOCK_HAT.playSound(target);
					Messager.sendMessage(target, ChatColor.translateAlternateColorCodes('&', "&c" + AbilityName + " ��Ÿ�� &f" + NumberUtil.parseTimeString(this.getCount())));
				}
			} else {
				actionbar = new ActionbarPacket(ChatColor.translateAlternateColorCodes('&', "&c��Ÿ�� &f: &6" + NumberUtil.parseTimeString(this.getCount())), 0, 25, 0);
				
				if(Seconds == (getMaxCount() / 2)) {
					SoundLib.BLOCK_NOTE_BLOCK_HAT.playSound(target);
					Messager.sendMessage(target, ChatColor.translateAlternateColorCodes('&', "&c��Ÿ�� &f" + NumberUtil.parseTimeString(this.getCount())));
				} else if(Seconds <= 5 && Seconds >= 1) {
					SoundLib.BLOCK_NOTE_BLOCK_HAT.playSound(target);
					Messager.sendMessage(target, ChatColor.translateAlternateColorCodes('&', "&c��Ÿ�� &f" + NumberUtil.parseTimeString(this.getCount())));
				}
			}

			if(actionbarNotice) actionbar.Send(target);
		}
	}
	
	@Override
	public void onEnd() {
		Player target = Ability.getPlayer();
		if(target != null) {
			ActionbarPacket actionbar = new ActionbarPacket(ChatColor.translateAlternateColorCodes('&', "&a�ɷ��� �ٽ� ����� �� �ֽ��ϴ�."), 0, 50, 0);
			if(actionbarNotice) actionbar.Send(target);
			Messager.sendMessage(target, ChatColor.translateAlternateColorCodes('&', "&a�ɷ��� �ٽ� ����� �� �ֽ��ϴ�."));
		}
	}
	
}
