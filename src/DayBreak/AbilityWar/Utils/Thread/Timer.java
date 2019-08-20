package DayBreak.AbilityWar.Utils.Thread;

import org.bukkit.Bukkit;

import DayBreak.AbilityWar.AbilityWar;

/**
 * �Ϲ� Ÿ�̸�
 * @author DayBreak ����
 */
abstract public class Timer {

	private int Task = -1;

	private boolean InfiniteTimer;
	
	private int MaxCount;
	private int Count;
	private int Period = 20;

	abstract protected void onStart();

	abstract protected void TimerProcess(Integer Seconds);

	abstract protected void onEnd();

	public boolean isTimerRunning() {
		return Task != -1;
	}

	/**
	 * Ÿ�̸Ӹ� �����մϴ�.
	 */
	public void StartTimer() {
		if(!this.isTimerRunning()) {
			Count = MaxCount;
			this.Task = Bukkit.getScheduler().scheduleSyncRepeatingTask(AbilityWar.getPlugin(), new TimerTask(), 0, Period);
			onStart();
		}
	}

	/**
	 * Ÿ�̸Ӹ� �����մϴ�.
	 */
	public void StopTimer() {
		if(this.isTimerRunning()) {
			Bukkit.getScheduler().cancelTask(Task);
			Count = MaxCount;
			this.Task = -1;
			onEnd();
		}
	}
	
	public int getMaxCount() {
		return MaxCount;
	}
	
	public int getCount() {
		return Count;
	}

	public int getFixedCount() {
		return (int) (Count / (20 / Period));
	}
	
	public Timer setPeriod(int Period) {
		this.Period = Period;
		return this;
	}
	
	/**
	 * �Ϲ� Ÿ�̸�
	 */
	public Timer(int Count) {
		InfiniteTimer = false;
		this.MaxCount = Count;
	}
	
	/**
	 * ���� Ÿ�̸�
	 */
	public Timer() {
		InfiniteTimer = true;
		this.MaxCount = 0;
	}
	
	private final class TimerTask extends Thread {

		@Override
		public void run() {
			if (InfiniteTimer) {
				TimerProcess(Count);
				Count++;
			} else {
				if (Count > 0) {
					TimerProcess(Count);

					if (Count <= 0) {
						StopTimer();
					}

					Count--;
				} else {
					StopTimer();
				}
			}
		}

	}

}