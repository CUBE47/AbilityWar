package DayBreak.AbilityWar.Ability;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import DayBreak.AbilityWar.Ability.AbilityFactory.AbilityRegisteration;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Game.Manager.PassiveManager.PassiveExecutor;
import DayBreak.AbilityWar.Utils.Thread.AbilityWarThread;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

/**
 * �ɷ��� ����� �Ǵ� Ŭ�����Դϴ�.
 */
public abstract class AbilityBase implements PassiveExecutor {
	
	private final Participant participant;
	private final String[] explain;
	private final AbilityManifest manifest;
	private final AbilityRegisteration<?> registeration;
	private final AbstractGame game;
	
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

		if(AbilityWarThread.isGameTaskRunning()) {
			this.game = AbilityWarThread.getGame();
		} else {
			throw new NullPointerException("������ �������� �� AbilityBase Ŭ������ ��üȭ�Ǿ�� �մϴ�.");
		}
		
		if(AbilityFactory.isRegistered(this.getClass())) {
			AbilityRegisteration<?> ar = AbilityFactory.getRegisteration(this.getClass());
			this.registeration = ar;
			this.manifest = ar.getManifest();
			this.eventhandlers = ar.getEventhandlers();
			
			for(Class<? extends Event> eventClass : eventhandlers.keySet()) game.getPassiveManager().register(eventClass, this);
		} else {
			throw new NullPointerException("AbilityFactory�� ��ϵ��� ���� �ɷ��Դϴ�.");
		}
	}
	
	/**
	 * ��Ƽ�� ��ų �ߵ��� ���� ���˴ϴ�.
	 * GameListener���� ȣ���մϴ�.
	 * @param mt	�÷��̾ Ŭ���� �� �տ� �־��� ������	
	 * @param ct	Ŭ���� ����
	 * @return		�ɷ� �ߵ� ����
	 */
	public abstract boolean ActiveSkill(MaterialType mt, ClickType ct);
	
	private final Map<Class<? extends Event>, Method> eventhandlers;
	
	@Override
	public void execute(Event event) {
		if(!Restricted) {
			Class<? extends Event> eventClass = event.getClass();
			if(eventhandlers.containsKey(eventClass)) {
				try {
					eventhandlers.get(eventClass).invoke(this, event);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Ÿ���� ��ų �ߵ��� ���� ���˴ϴ�.
	 * GameListener���� ȣ���մϴ�.
	 * @param mt		�÷��̾ Ÿ������ �� �� �տ� ��� �־��� ������
	 * @param entity	Ÿ������ ���, Ÿ������ ����� ���� ��� null�� ���� �� �ֽ��ϴ�.
	 * 					null üũ�� �ʿ��մϴ�.
	 */
	public abstract void TargetSkill(MaterialType mt, Entity entity);
	
	/**
	 * �ɷ� ������ ������ ��� ȣ��˴ϴ�.
	 */
	protected abstract void onRestrictClear();
	
	/**
	 * �÷��̾� �ɷ� ������ ���˴ϴ�.
	 * �÷��̾��� �ɷ��� ����� �� �ڵ����� ȣ��˴ϴ�.
	 */
	public void Remove() {
		game.getPassiveManager().unregisterAll(this);
		this.StopAllTimers();
	}

	private void StopAllTimers() {
		for(TimerBase timer : getTimers()) {
			timer.StopTimer(true);
		}
	}
	
	/**
	 * �ɷ¿� ���Ǵ� ��� Ÿ�̸Ӹ� ��ȯ�մϴ�.
	 */
	private List<TimerBase> getTimers() {
		List<TimerBase> timers = new ArrayList<>();
		for(Field f : registeration.getTimers()) {
			try {
				f.setAccessible(true);
				timers.add((TimerBase) f.get(this));
				f.setAccessible(false);
			} catch(Exception ex) {}
		}
		
		return timers;
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
	 */
	public String getName() {
		return manifest.Name();
	}

	/**
	 * �ɷ��� ����� ��ȯ�մϴ�.
	 */
	public Rank getRank() {
		return manifest.Rank();
	}

	/**
	 * �ɷ��� ������ ��ȯ�մϴ�.
	 */
	public Species getSpecies() {
		return manifest.Species();
	}

	/**
	 * �� �ɷ��� ���Ǵ� ������ ��ȯ�մϴ�.
	 */
	protected AbstractGame getGame() {
		return game;
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
