package Marlang.AbilityWar.Addon;

import org.bukkit.plugin.Plugin;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.Addon.AddonLoader.DescriptionFile;

/**
 * �ֵ��
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
