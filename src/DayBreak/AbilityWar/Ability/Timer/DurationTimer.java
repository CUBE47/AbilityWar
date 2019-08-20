package DayBreak.AbilityWar.Ability.Timer;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Library.SoundLib;
import DayBreak.AbilityWar.Utils.Library.TItle.Actionbar;
import DayBreak.AbilityWar.Utils.Math.NumberUtil;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

/**
 * Duration Timer (���ӽð� Ÿ�̸�)
 * @author DayBreak ����
 */
abstract public class DurationTimer extends TimerBase {

	/**
	 * ���ӽð� �ʱ�ȭ
	 */
	public static void ResetDuration() {
		TimerBase.StopTasks(DurationTimer.class);
	}
	
	private final AbilityBase Ability;
	private final CooldownTimer CooldownTimer;
	private final int Duration;

	public DurationTimer(AbilityBase Ability, Integer Duration, CooldownTimer CooldownTimer) {
		super(Duration);
		this.Ability = Ability;
		this.Duration = Duration;
		this.CooldownTimer = CooldownTimer;
	}

	public DurationTimer(AbilityBase Ability, Integer Duration) {
		super(Duration);
		this.Ability = Ability;
		this.Duration = Duration;
		this.CooldownTimer = null;
	}
	
	abstract protected void onDurationStart();
	
	abstract protected void DurationProcess(Integer Seconds);
	
	abstract protected void onDurationEnd();
	
	public boolean isDuration() {
		if(isTimerRunning()) {
			Messager.sendMessage(Ability.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&6���� �ð� &f" + NumberUtil.parseTimeString(this.getFixedCount())));
		}
		
		return isTimerRunning();
	}
	
	@Override
	public DurationTimer setPeriod(int Period) {
		super.setPeriod(Period);
		return this;
	}

	@Override
	public DurationTimer setSilentNotice(boolean forcedStopNotice) {
		super.setSilentNotice(forcedStopNotice);
		return this;
	}

	@Override
	protected void onStart() {
		//Notify
		this.onDurationStart();
		
		Counted = new ArrayList<Integer>();
	}
	
	private ArrayList<Integer> Counted;
	
	@Override
	protected void TimerProcess(Integer Seconds) {
		Player target = Ability.getPlayer();
		if(target != null) {
			this.DurationProcess(Seconds);
			
			Actionbar actionbar = new Actionbar(ChatColor.translateAlternateColorCodes('&', "&6���� �ð� &f: &e" + NumberUtil.parseTimeString(this.getFixedCount())), 0, 25, 0);
			actionbar.sendTo(target);
			
			if(this.getFixedCount() == (Duration / 2) && !Counted.contains(this.getFixedCount())) {
				Counted.add(this.getFixedCount());
				Messager.sendMessage(target, ChatColor.translateAlternateColorCodes('&', "&6���� �ð� &f" + NumberUtil.parseTimeString(this.getFixedCount())));
				SoundLib.BLOCK_NOTE_BLOCK_HAT.playSound(target);
			} else if(this.getFixedCount() <= 5 && this.getFixedCount() >= 1 && !Counted.contains(this.getFixedCount())) {
				Counted.add(this.getFixedCount());
				Messager.sendMessage(target, ChatColor.translateAlternateColorCodes('&', "&6���� �ð� &f" + NumberUtil.parseTimeString(this.getFixedCount())));
				SoundLib.BLOCK_NOTE_BLOCK_HAT.playSound(target);
			}
		}
	}
	
	@Override
	protected void onEnd() {
		Player target = Ability.getPlayer();
		if(target != null) {
			//Notify
			this.onDurationEnd();
			
			if(CooldownTimer != null) {
				CooldownTimer.StartTimer();
			}
			
			Messager.sendMessage(target, ChatColor.translateAlternateColorCodes('&', "&6���� �ð�&f�� ����Ǿ����ϴ�."));
		}
	}
	
}
