package DayBreak.AbilityWar.Ability;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.MainHand;

import DayBreak.AbilityWar.AbilityWar;
import DayBreak.AbilityWar.Ability.AbilityFactory.AbilityRegisteration;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Game.Games.ChangeAbility.ChangeAbilityWar;
import DayBreak.AbilityWar.Game.Games.Default.DefaultGame;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Game.Manager.AbilityList;
import DayBreak.AbilityWar.Game.Manager.PassiveManager.PassiveExecutor;
import DayBreak.AbilityWar.Utils.Thread.AbilityWarThread;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

/**
 * {@link AbilityWar} �÷����ο��� ����ϴ� <strong>��� �ɷ�</strong>�� ����� �Ǵ� Ŭ�����Դϴ�.
 * <p>
 * ������� <strong>��� �ɷ��� �ݵ�� {@link AbilityFactory}�� ��ϵǾ�� �մϴ�.</strong>
 * <p>
 * <ul>
 * {@link AbilityFactory#registerAbility(clazz)}
 * </ul>
 * {@link DefaultGame}, {@link ChangeAbilityWar} ��� ����� �ɷ��� �߰�������
 * {@link AbilityList}�� ����ؾ� �մϴ�.
 * <p>
 * <ul>
 * {@link AbilityList#registerAbility(clazz)}
 * </ul>
 * 
 * @author DayBreak ����
 */
public abstract class AbilityBase implements PassiveExecutor {

	private final Participant participant;
	private final String[] explain;
	private final AbilityManifest manifest;
	private final AbilityRegisteration<?> registeration;
	private final AbstractGame game;

	private boolean Restricted = true;

	/**
	 * {@link AbilityBase}�� �⺻ �������Դϴ�.
	 * 
	 * @param participant �ɷ��� �����ϴ� ������
	 * @param explain     �ɷ� ����
	 * 
	 * @throws IllegalStateException ������ ���������� ���� ���, {@link AbilityFactory}�� ��ϵ���
	 *                               ���� �ɷ��� ���
	 */
	public AbilityBase(Participant participant, String... explain) {
		this.participant = participant;
		this.explain = explain;

		if (AbilityWarThread.isGameTaskRunning()) {
			this.game = AbilityWarThread.getGame();
		} else {
			throw new IllegalStateException("������ �������� �� AbilityBase Ŭ������ ��üȭ�Ǿ�� �մϴ�.");
		}

		if (AbilityFactory.isRegistered(this.getClass())) {
			AbilityRegisteration<?> ar = AbilityFactory.getRegisteration(this.getClass());
			this.registeration = ar;
			this.manifest = ar.getManifest();
			this.eventhandlers = ar.getEventhandlers();

			for (Class<? extends Event> eventClass : eventhandlers.keySet())
				game.getPassiveManager().register(eventClass, this);
		} else {
			throw new IllegalStateException("AbilityFactory�� ��ϵ��� ���� �ɷ��Դϴ�.");
		}
	}

	private final Map<Class<? extends Event>, Method> eventhandlers;

	@Override
	public void execute(Event event) {
		if (!Restricted) {
			Class<? extends Event> eventClass = event.getClass();
			if (eventhandlers.containsKey(eventClass)) {
				try {
					eventhandlers.get(eventClass).invoke(this, event);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * ��Ƽ�� ��ų �ߵ��� ���� ���˴ϴ�.
	 * 
	 * @param materialType �÷��̾ Ŭ���� �� {@link MainHand}�� ��� �־��� ������
	 * @param clickType    Ŭ���� ����
	 * @return �ɷ� �ߵ� ����
	 */
	public abstract boolean ActiveSkill(MaterialType mt, ClickType ct);

	/**
	 * Ÿ���� ��ų �ߵ��� ���� ���˴ϴ�.
	 * 
	 * @param materialType �÷��̾ Ŭ���� �� {@link MainHand}�� ��� �־��� ������
	 * @param entity       Ÿ������ ���, Ÿ������ ����� ���� ��� null�� �� �� �ֽ��ϴ�. null üũ�� �ʿ��մϴ�.
	 */
	public abstract void TargetSkill(MaterialType mt, LivingEntity entity);

	/**
	 * �ɷ� ������ ������ ��� ȣ��˴ϴ�.
	 */
	protected abstract void onRestrictClear();

	/**
	 * �� �̻� ������ �ʴ� {@link AbilityBase}�� ������ �� ���˴ϴ�.<p>
	 * {@link Participant#removeAbility()}�� ���� {@link Participant}�� �ɷ��� ������ �� ȣ��˴ϴ�.
	 * ���� ���Ƿ� ȣ������ ���ʽÿ�.
	 */
	public final void destroy() {
		game.getPassiveManager().unregisterAll(this);
		stopTimers();
	}

	private final void stopTimers() {
		for (TimerBase timer : getTimers()) {
			timer.StopTimer(true);
		}
	}

	/**
	 * �ɷ¿� ���Ǵ� ��� Ÿ�̸Ӹ� ��ȯ�մϴ�.
	 */
	private final List<TimerBase> getTimers() {
		List<TimerBase> timers = new ArrayList<>();
		for (Field f : registeration.getTimers()) {
			try {
				f.setAccessible(true);
				timers.add((TimerBase) f.get(this));
				f.setAccessible(false);
			} catch (Exception ex) {}
		}

		return timers;
	}

	/**
	 * �ɷ��� �����ϴ� �÷��̾ ��ȯ�մϴ�.
	 */
	public final Player getPlayer() {
		return participant.getPlayer();
	}

	/**
	 * �ɷ��� �����ϴ� �����ڸ� ��ȯ�մϴ�.
	 */
	public final Participant getParticipant() {
		return participant;
	}

	/**
	 * �ɷ��� ������ ��ȯ�մϴ�.
	 */
	public final String[] getExplain() {
		return explain;
	}

	/**
	 * �ɷ��� �̸��� ��ȯ�մϴ�.
	 */
	public final String getName() {
		return manifest.Name();
	}

	/**
	 * �ɷ��� ����� ��ȯ�մϴ�.
	 */
	public final Rank getRank() {
		return manifest.Rank();
	}

	/**
	 * �ɷ��� ������ ��ȯ�մϴ�.
	 */
	public final Species getSpecies() {
		return manifest.Species();
	}

	/**
	 * �� �ɷ��� ���Ǵ� ������ ��ȯ�մϴ�.
	 */
	protected final AbstractGame getGame() {
		return game;
	}

	/**
	 * �ɷ��� ���� ���θ� ��ȯ�մϴ�.
	 */
	public final boolean isRestricted() {
		return Restricted;
	}

	/**
	 * �ɷ��� ���� ���θ� �����մϴ�.
	 */
	public final void setRestricted(boolean restricted) {
		this.Restricted = restricted;

		if (restricted) {
			this.stopTimers();
		} else {
			this.onRestrictClear();
		}
	}

	public enum ClickType {
		LeftClick, RightClick;
	}

	public enum MaterialType {

		Iron_Ingot(Material.IRON_INGOT), Gold_Ingot(Material.GOLD_INGOT);

		private Material material;

		private MaterialType(Material material) {
			this.material = material;
		}

		public Material getMaterial() {
			return material;
		}

	}

}
