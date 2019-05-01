package DayBreak.AbilityWar.Development.Addon;

import org.bukkit.plugin.Plugin;

import DayBreak.AbilityWar.AbilityWar;
import DayBreak.AbilityWar.Development.Addon.AddonLoader.DescriptionFile;

/**
 * �ֵ��
 * @author DayBreak ����
 */
abstract public class Addon {

	private DescriptionFile description;

	private ClassLoader classLoader;
	
	abstract public void onEnable();

	abstract public void onDisable();

	/**
	 * AbilityWar �÷������� �޾ƿɴϴ�.
	 */
	protected Plugin getPlugin() {
		return AbilityWar.getPlugin();
	}

	/**
	 * addon.yml�� �ۼ��� �ֵ���� ������ �޾ƿɴϴ�.
	 */
	public DescriptionFile getDescription() {
		return description;
	}

	/**
	 * �� �ֵ���� �ҷ��� �� ���� ClassLoader�� �޾ƿɴϴ�.
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}

}
