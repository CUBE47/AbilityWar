package DayBreak.AbilityWar.Utils.Thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;

import DayBreak.AbilityWar.AbilityWar;
import DayBreak.AbilityWar.Ability.List.Feather;
import DayBreak.AbilityWar.Ability.List.Gladiator;
import DayBreak.AbilityWar.Ability.List.Pumpkin;

/**
 * ���� ���� �� ����Ǵ� Ÿ�̸�
 * @author DayBreak ����
 */
abstract public class TimerBase {

	private static List<TimerBase> Tasks = new ArrayList<>();
	
	/**
	 * ���� �������� ��� {@link TimerBase}�� ��ȯ�մϴ�.
	 */
	public static Collection<TimerBase> getTasks() {
		return new ArrayList<TimerBase>(Tasks);
	}

	/**
	 * �ش� Ÿ���� {@link TimerBase}�� ��� �����մϴ�.
	 * @param timerClass
	 */
	public static void StopTasks(Class<? extends TimerBase> timerClass) {
		for(TimerBase timer : getTasks()) {
			if(timerClass.isAssignableFrom(timer.getClass())) {
				timer.StopTimer(false);
			}
		}
	}

	/**
	 * ���� �������� {@link TimerBase}�� ��� �����մϴ�.
	 */
	public static void ResetTasks() {
		for(TimerBase timer : getTasks()) timer.StopTimer(true);
		Tasks = new ArrayList<TimerBase>();
	}

	private int Task = -1;

	private boolean InfiniteTimer;
	
	private int MaxCount;
	private int Count;
	private int Period = 20;
	
	/**
	 * Ÿ�̸Ӹ� Silent ���� �����Ű���� {@link #onEnd()}�� ȣ�������� �����Դϴ�.<p>
	 * {@link Pumpkin}, {@link Feather}, {@link Gladiator}�� ���� �ɷµ鿡��
	 * {@link TimerBase}�� {@link #onEnd()}�� ȣ����� �ʰ� �Ǹ� ���� �ʱ�ȭ�� �̷������ �ʾ�
	 * �ɷ� �ߵ� ���� ���°� ��� �����Ǵ� ������ �־� �߰��� �����Դϴ�.
	 * 
	 * �ɷ��� ������ ����� ���Ŀ� �ʱ�ȭ�� �ʿ��� �ɷ¿����� ����� ���� �����մϴ�.
	 */
	private boolean SilentNotice = false;

	/**
	 * {@link TimerBase}�� ����� �� ȣ��˴ϴ�.
	 */
	abstract protected void onStart();

	/**
	 * {@link TimerBase} ���� ���� {@link #Period}ƽ���� ȣ��˴ϴ�.
	 * <pre>
	 * �Ϲ� Ÿ�̸�
	 * <pre>
	 * ī��Ʈ ���� {@link #MaxCount}���� �����Ͽ� 1���� �����մϴ�.</pre>
	 * ���� Ÿ�̸�
	 * <pre>
	 * ī��Ʈ ���� 1���� �����Ͽ� {@link Integer#MAX_VALUE}���� �����մϴ�.</pre>
	 * </pre>
	 * 
	 */
	protected abstract void TimerProcess(Integer Count);

	/**
	 * {@link TimerBase}�� ����� �� ȣ��˴ϴ�.
	 */
	protected abstract void onEnd();

	/**
	 * {@link TimerBase}�� ���� ���θ� ��ȯ�մϴ�.
	 */
	public final boolean isTimerRunning() {
		return Task != -1;
	}

	/**
	 * {@link TimerBase}�� �����մϴ�.
	 */
	public final void StartTimer() {
		if(!this.isTimerRunning()) {
			Count = MaxCount;
			this.Task = Bukkit.getScheduler().scheduleSyncRepeatingTask(AbilityWar.getPlugin(), new TimerTask(), 0, Period);
			Tasks.add(this);
			onStart();
		}
	}

	/**
	 * {@link TimerBase}�� �����մϴ�.<p>
	 * @param Silent 	true�� ��쿡 Ÿ�̸Ӹ� Silent ���� �����մϴ�.
	 * 					Silent ��忡���� {@link #onEnd()}�� ȣ����� �ʽ��ϴ�.
	 */
	public final void StopTimer(boolean Silent) {
		if(this.isTimerRunning()) {
			Bukkit.getScheduler().cancelTask(Task);
			Tasks.remove(this);
			Count = MaxCount;
			this.Task = -1;
			if(!Silent || SilentNotice) {
				onEnd();
			}
		}
	}
	
	public final int getMaxCount() {
		return MaxCount;
	}
	
	public final int getCount() {
		return Count;
	}

	public final int getFixedCount() {
		return (int) (Count / (20 / Period));
	}
	
	public TimerBase setPeriod(int Period) {
		this.Period = Period;
		return this;
	}

	public TimerBase setSilentNotice(boolean silentNotice) {
		SilentNotice = silentNotice;
		return this;
	}

	protected final boolean isSilentNotice() {
		return SilentNotice;
	}

	/**
	 * �Ϲ� {@link TimerBase}
	 */
	public TimerBase(int Count) {
		InfiniteTimer = false;
		this.MaxCount = Count;
	}
	
	/**
	 * ���� {@link TimerBase}
	 */
	public TimerBase() {
		InfiniteTimer = true;
		this.MaxCount = 1;
	}
	
	private final class TimerTask extends Thread {

		@Override
		public void run() {
			if (AbilityWarThread.isGameTaskRunning()) {
				if (InfiniteTimer) {
					TimerProcess(Count);
					if(Count <= Integer.MAX_VALUE) Count++;
				} else {
					if (Count > 0) {
						TimerProcess(Count);
						Count--;
					} else {
						StopTimer(false);
					}
				}
			} else {
				StopTimer(true);
			}
		}

	}

}