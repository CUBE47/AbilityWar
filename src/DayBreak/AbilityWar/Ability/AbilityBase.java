package DayBreak.AbilityWar.Ability;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Game.Games.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Validate;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

/**
 * �ɷ��� ����� �Ǵ� Ŭ�����Դϴ�.
 */
abstract public class AbilityBase {
	
	private Participant participant;
	private final String[] explain;
	private final String name;
	private final Rank rank;
	
	private boolean Restricted = true;
	
	/**
	 * �ɷ��� �⺻ �������Դϴ�.
	 * @param player		�ɷ��� �����ϴ� �÷��̾�
	 * @param AbilityName	�ɷ� �̸�
	 * @param Rank			�ɷ� ��ũ
	 * @param Explain		�ɷ� ����
	 */
	public AbilityBase(Participant participant, String... explain) {
		this.participant = participant;
		this.explain = explain;
		
		AbilityManifest manifest = this.getClass().getAnnotation(AbilityManifest.class);
		
		if(manifest != null) {
			this.name = manifest.Name();
			this.rank = manifest.Rank();
		} else {
			this.name = null;
			this.rank = null;
		}
	}
	
	/**
	 * ��Ƽ�� ��ų �ߵ��� ���� ���˴ϴ�.
	 * GameListener���� ȣ���մϴ�.
	 * @param mt	�÷��̾ Ŭ���� �� �տ� �־��� ������	
	 * @param ct	Ŭ���� ����
	 * @return		�ɷ� �ߵ� ����
	 */
	abstract public boolean ActiveSkill(MaterialType mt, ClickType ct);
	
	/**
	 * �нú� ��ų �ߵ��� ���� ���˴ϴ�.
	 * GameListener���� ȣ���մϴ�.
	 * �нú� �̺�Ʈ�� GameListener.registerPassive(Class<? extends Event> clazz)�� ����� �� �ֽ��ϴ�.
	 * @param event		�нú� �̺�Ʈ
	 */
	abstract public void PassiveSkill(Event event);
	
	/**
	 * Ÿ���� ��ų �ߵ��� ���� ���˴ϴ�.
	 * GameListener���� ȣ���մϴ�.
	 * @param mt		�÷��̾ Ÿ������ �� �� �տ� ��� �־��� ������
	 * @param entity	Ÿ������ ���, Ÿ������ ����� ���� ��� null�� ���� �� �ֽ��ϴ�.
	 * 					null üũ�� �ʿ��մϴ�.
	 */
	abstract public void TargetSkill(MaterialType mt, Entity entity);
	
	/**
	 * �ɷ� ������ ������ ��� ȣ��˴ϴ�.
	 */
	abstract protected void onRestrictClear();
	
	/**
	 * �÷��̾� �ɷ� ������ ���˴ϴ�.
	 * �÷��̾��� �ɷ��� ����� �� �ڵ����� ȣ��˴ϴ�.
	 */
	public void Delete() {
		this.StopAllTimers();
		
		this.participant = null;
	}

	private void StopAllTimers() {
		for(TimerBase timer : getTimers()) {
			timer.StopTimer(true);
		}
	}
	
	/**
	 * Reflection���� �ɷ¿� ���Ǵ� TimerBase �Ǵ� TimerBase�� �θ� Ŭ������ �ϴ� ��� Ÿ�̸Ӹ� ��ȯ�մϴ�.
	 * @return �ɷ¿� ���Ǵ� TimerBase ���
	 */
	private List<TimerBase> getTimers() {
		ArrayList<TimerBase> Timers = new ArrayList<TimerBase>();
		
		for(Field field : this.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				Class<?> type = field.getType();
				Class<?> superClass = type.getSuperclass();
				if(type.equals(TimerBase.class) ||(superClass != null && superClass.equals(TimerBase.class))) {
					Timers.add((TimerBase) field.get(this));
				}
				field.setAccessible(false);
			} catch (IllegalArgumentException | IllegalAccessException | NullPointerException exception) {
				Messager.sendErrorMessage("Reflection Error");
			}
		}
		
		return Timers;
	}
	
	/**
	 * �ɷ��� �����ϴ� �÷��̾ ��ȯ�մϴ�.
	 */
	public Player getPlayer() {
		return participant.getPlayer();
	}

	/**
	 * �ɷ��� �����ϴ� �����ڸ� ��ȯ�մϴ�.
	 */
	public Participant getParticipant() {
		return participant;
	}

	/**
	 * �ɷ��� ������ ��ȯ�մϴ�.
	 */
	public String[] getExplain() {
		return explain;
	}

	/**
	 * �ɷ��� �̸��� ��ȯ�մϴ�.
	 * �ɷ� Ŭ������ AbilityManifest ������̼��� �������� ���� ��� null�� ��ȯ�� �� �ֽ��ϴ�.
	 */
	public String getName() {
		return name;
	}

	/**
	 * �ɷ��� ����� ��ȯ�մϴ�.
	 * �ɷ� Ŭ������ AbilityManifest ������̼��� �������� ���� ��� null�� ��ȯ�� �� �ֽ��ϴ�.
	 */
	public Rank getRank() {
		return rank;
	}

	/**
	 * �ɷ��� ���� ���θ� ��ȯ�մϴ�.
	 */
	public boolean isRestricted() {
		return Restricted;
	}
	
	/**
	 * �ɷ��� ���� ���θ� �����մϴ�.
	 */
	public void setRestricted(boolean restricted) {
		this.Restricted = restricted;
		
		if(restricted) {
			this.StopAllTimers();
		} else {
			this.onRestrictClear();
		}
	}
	
	/**
	 * ���� �ð����� �����Ǵ� �ɷ��� ���� ���θ� �����մϴ�.
	 * restricted�� true�� ��� �ɷ��� seconds�ʰ� ���ѽ�Ű��, �Ŀ� ������ �����մϴ�.
	 * false�� ��쿡�� seconds�ʰ� ������ �����ϰ�, �Ŀ� �ɷ��� �����մϴ�.
	 */
	public void setRestricted(boolean restricted, int seconds) {
		new TimerBase(seconds) {
			
			@Override
			protected void onStart() {
				setRestricted(restricted);
			}
			
			@Override
			protected void TimerProcess(Integer Seconds) {}

			@Override
			protected void onEnd() {
				setRestricted(!restricted);
			}
			
		}.StartTimer();
	}
	
	/**
	 * �ɷ��� �����ϴ� �÷��̾ �����մϴ�.
	 * @param player						�ɷ��� ������ �÷��̾�
	 * @throws IllegalArgumentException		�÷��̾ null�� ��� �߻�
	 */
	public void updateParticipant(Participant participant) throws IllegalArgumentException {
		Validate.NotNull(participant);
		
		this.participant = participant;
	}
	
	public enum ClickType {
		/**
		 * ��Ŭ��
		 */
		RightClick,
		/**
		 * ��Ŭ��
		 */
		LeftClick;
	}
	
	public enum MaterialType {
		
		/**
		 * ö��
		 */
		Iron_Ingot(Material.IRON_INGOT),
		/**
		 * �ݱ�
		 */
		Gold_Ingot(Material.GOLD_INGOT);
		
		private Material material;
		
		private MaterialType(Material material) {
			this.material = material;
		}
		
		public Material getMaterial() {
			return material;
		}
		
	}
	
}