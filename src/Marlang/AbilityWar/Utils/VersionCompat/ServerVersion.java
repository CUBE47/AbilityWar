package Marlang.AbilityWar.Utils.VersionCompat;

import org.bukkit.Bukkit;

/**
 * Server Version
 * @author _Marlang ����
 */
public class ServerVersion {

	private static Integer Version = getSimpleVersion();
	
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
	 * ���� ������ ������ Integer�� �޾ƿɴϴ�. Ex. 1.12.2 => 12
	 */
	private static Integer getSimpleVersion() {
		Integer Version = -1;
		String[] versionArray = getVersionString().split("_");
		if (versionArray.length >= 2) {
			try {
				Version = Integer.valueOf(versionArray[1]);
			} catch (NumberFormatException ex) {
			}
		}

		return Version;
	}
	
	public static Integer getVersion() {
		return Version;
	}
	
}
