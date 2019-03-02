package Marlang.AbilityWar.Ability;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Validate;
import Marlang.AbilityWar.Utils.Thread.TimerBase;

/**
 * �ɷ��� ����� �Ǵ� Ŭ�����Դϴ�.
 */
abstract public class AbilityBase {
	
	private Player player;
	private String AbilityName;
	private Rank Rank;
	private String[] Explain;
	
	private boolean Restricted = true;
	
	/**
	 * �ɷ��� �⺻ �������Դϴ�.
	 * @param player		�ɷ��� �����ϴ� �÷��̾�
	 * @param AbilityName	�ɷ� �̸�
	 * @param Rank			�ɷ� ��ũ
	 * @param Explain		�ɷ� ����
	 */
	public AbilityBase(Player player, String AbilityName, Rank Rank, String... Explain) {
		this.player = player;
		this.AbilityName = AbilityName;
		this.Rank = Rank;
		this.Explain = Explain;
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
	public void DeleteAbility() {
		for(TimerBase timer : getTimers()) {
			timer.StopTimer(true);
		}
		
		this.player = null;
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
		return player;
	}

	/**
	 * �ɷ��� �̸��� ��ȯ�մϴ�.
	 */
	public String getAbilityName() {
		return AbilityName;
	}

	/**
	 * �ɷ��� ��ũ�� ��ȯ�մϴ�.
	 */
	public Rank getRank() {
		return Rank;
	}

	/**
	 * �ɷ��� ������ ��ȯ�մϴ�.
	 */
	public String[] getExplain() {
		return Explain;
	}

	/**
	 * �ɷ��� ������ �����մϴ�.
	 */
	protected void setExplain(String... Explain) {
		this.Explain = Explain;
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
		Restricted = restricted;
		
		if(!restricted) {
			onRestrictClear();
		}
	}
	
	/**
	 * �ɷ��� �����ϴ� �÷��̾ �����մϴ�.
	 * @param player						�ɷ��� ������ �÷��̾�
	 * @throws IllegalArgumentException		�÷��̾ null�� ��� �߻�
	 */
	public void updatePlayer(Player player) throws IllegalArgumentException {
		Validate.NotNull(player);
		
		this.player = player;
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
	
	public enum Rank {
		
		/**
		 * Special ���
		 */
		SPECIAL(ChatColor.translateAlternateColorCodes('&', "&5Special ���")),
		/**
		 * �� ���
		 */
		GOD(ChatColor.translateAlternateColorCodes('&', "&c�� ���")),
		/**
		 * S ���
		 */
		S(ChatColor.translateAlternateColorCodes('&', "&dS ���")),
		/**
		 * A ���
		 */
		A(ChatColor.translateAlternateColorCodes('&', "&aA ���")),
		/**
		 * B ���
		 */
		B(ChatColor.translateAlternateColorCodes('&', "&bB ���")),
		/**
		 * C ���
		 */
		C(ChatColor.translateAlternateColorCodes('&', "&eC ���")),
		/**
		 * D ���
		 */
		D(ChatColor.translateAlternateColorCodes('&', "&7D ���"));
		
		private String RankName;
		
		private Rank(String RankName) {
			this.RankName = RankName;
		}
		
		public String getRankName() {
			return RankName;
		}
		
	}
	
}
