package daybreak.abilitywar.utils.base.minecraft.version;

import com.google.common.base.Enums;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server Version
 *
 * @author Daybreak 새벽
 */
public class ServerVersion {

	private static final Logger logger = Logger.getLogger(ServerVersion.class.getName());

	private ServerVersion() {
	}

	private static Version version;

	static {
		String[] versionArray = Bukkit.getServer().getClass().getName().replace('.', ',').split(",");
		if (versionArray.length >= 4) {
			ServerVersion.version = Enums.getIfPresent(Version.class, versionArray[3]).or(Version.UNSUPPORTED);
		} else ServerVersion.version = Version.UNSUPPORTED;
	}

	public static Version getVersion() {
		return version;
	}

	public static int getVersionNumber() {
		return version.getVersion();
	}

	public static int getReleaseNumber() {
		return version.getRelease();
	}

	public static boolean compatVersion(Plugin plugin) {
		if (version != Version.UNSUPPORTED) {
			if (getVersionNumber() >= 13) {
				setAPIVersion(plugin, "1." + getVersionNumber());
			}
			return true;
		} else {
			logger.log(Level.SEVERE, "플러그인이 지원하지 않는 버전을 이용하고 있습니다.");
			Bukkit.getPluginManager().disablePlugin(plugin);
			return false;
		}
	}

	private static void setAPIVersion(Plugin plugin, String version) {
		try {
			PluginDescriptionFile desc = plugin.getDescription();
			Field apiVersion = PluginDescriptionFile.class.getDeclaredField("apiVersion");
			apiVersion.setAccessible(true);
			apiVersion.set(desc, version);
			apiVersion.setAccessible(false);
		} catch (IllegalAccessException | NoSuchFieldException ignored) {
			//Ignore: Should Never Happen
		}
	}

	public enum Version {
		UNSUPPORTED(-1, -1),
		v1_8_R1(8, 1), v1_8_R2(8, 2), v1_8_R3(8, 3);

		private final int version;
		private final int release;

		Version(int version, int release) {
			this.version = version;
			this.release = release;
		}

		public int getVersion() {
			return version;
		}

		public int getRelease() {
			return release;
		}

		public boolean isOver(ServerVersion.Version other) {
			return version >= other.version && (version != other.version || release >= other.release);
		}

	}

}
