package DayBreak.AbilityWar.Config.Enums;

import org.bukkit.ChatColor;

import com.google.common.base.Enums;

public enum OnDeath {

	Ż��(ChatColor.WHITE + "�÷��̾ Ż����ŵ�ϴ�.") {
		@Override
		public OnDeath Next() {
			return OnDeath.�������;
		}
	},
	�������(ChatColor.WHITE + "�÷��̾ ���� ���� ��ȯ�մϴ�.") {
		@Override
		public OnDeath Next() {
			return OnDeath.����;
		}
	},
	����(ChatColor.WHITE + "�ƹ� �۾��� ���� �ʽ��ϴ�.") {
		@Override
		public OnDeath Next() {
			return OnDeath.Ż��;
		}
	};
	
	private final String description;
	
	private OnDeath(final String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public abstract OnDeath Next();
	
	/**
	 * �ش� �̸��� ����� ������ ��ȯ�մϴ�.
	 * �������� ���� ��� 'OnDeath.����'�� ��ȯ�մϴ�.
	 */
	public static OnDeath getIfPresent(String name) {
		return Enums.getIfPresent(OnDeath.class, name).or(OnDeath.����);
	}
	
}
