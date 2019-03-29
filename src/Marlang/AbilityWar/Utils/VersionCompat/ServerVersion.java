package Marlang.AbilityWar.Utils.VersionCompat;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * Server Version
 * @author _Marlang ����
 */
public class ServerVersion {

	private ServerVersion() {}
	
	private static String VersionString = getVersionString();
	private static int Version = getSimpleVersion();
	
	/**
	 * ���� ������ String���� �޾ƿɴϴ�. Ex. v1_12_R1
	 */
	private static String getVersionString() {
		String[] versionArray = Bukkit.getServer().getClass().getName().replace('.', ',').split(",");
		if (versionArray.length >= 4) {
			return versionArray[3];
		} else {
			return "";
		}
	}
	
	/**
	 * ���� ������ ������ Int�� �޾ƿɴϴ�. Ex. 1.12.2 => 12
	 */
	private static int getSimpleVersion() {
		int Version = -1;
		String[] versionArray = VersionString.split("_");
		if (versionArray.length >= 2) {
			try {
				Version = Integer.valueOf(versionArray[1]);
			} catch (NumberFormatException ex) {
				//Ignore: Should Never Happen
			}
		}

		return Version;
	}
	
	public static int getVersion() {
		return Version;
	}
	
	public static String getStringVersion() {
		return VersionString;
	}
	
	/**
	 * ���� ȣȯ �۾�
	 */
	public static void VersionCompat(Plugin plugin) {
		if(getVersion() >= 13) {
			setAPIVersion(plugin, "1.13");
		}
	}
	
	private static void setAPIVersion(Plugin plugin, String Version) {
		try {
			PluginDescriptionFile desc = plugin.getDescription();
			Field apiVersion = PluginDescriptionFile.class.getDeclaredField("apiVersion");
			apiVersion.setAccessible(true);
			apiVersion.set(desc, Version);
			apiVersion.setAccessible(false);
		} catch (Exception e) {
			//Ignore: Should Never Happen
		}
	}
	
}
