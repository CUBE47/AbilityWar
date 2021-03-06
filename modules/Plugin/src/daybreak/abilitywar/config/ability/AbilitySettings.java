package daybreak.abilitywar.config.ability;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.config.Cache;
import daybreak.abilitywar.config.CommentedConfiguration;
import daybreak.abilitywar.config.Configuration;
import daybreak.abilitywar.utils.base.io.FileUtil;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 능력 콘피그
 *
 * @author Daybreak 새벽
 */
public class AbilitySettings {

	private static final Logger logger = Logger.getLogger(Configuration.class.getName());
	private static final Set<SettingObject<?>> settings = new HashSet<>();

	private static void registerSetting(SettingObject<?> object) {
		settings.add(object);
	}

	private static File file = null;
	private static long lastModified;
	private static CommentedConfiguration config = null;

	public static boolean isLoaded() {
		return file != null && config != null;
	}

	public static void load() throws IOException, InvalidConfigurationException {
		if (!isLoaded()) {
			file = FileUtil.newFile("abilitysettings.yml");
			lastModified = file.lastModified();
			config = new CommentedConfiguration(file);
			_update();
		}
	}

	private static final HashMap<SettingObject<?>, Cache> cache = new HashMap<>();

	public static void update() {
		try {
			_update();
		} catch (IOException | InvalidConfigurationException e) {
			logger.log(Level.SEVERE, "콘피그를 업데이트하는 도중 오류가 발생하였습니다.");
		}
	}

	private static void _update() throws IOException, InvalidConfigurationException {
		if (!isLoaded()) {
			file = FileUtil.newFile("abilitysettings.yml");
			lastModified = file.lastModified();
			config = new CommentedConfiguration(file);
		}

		config.load();

		for (Entry<SettingObject<?>, Cache> entry : cache.entrySet()) {
			Cache cache = entry.getValue();
			if (cache.isModifiedValue()) {
				config.set(entry.getKey().getPath(), cache.getValue());
			}
		}

		cache.clear();
		lastModified = file.lastModified();
		for (SettingObject<?> setting : settings) {
			Object value = config.get(setting.getPath());
			if (value != null) {
				cache.put(setting, new Cache(false, value));
			} else {
				config.set(setting.getPath(), setting.getDefault());
				cache.put(setting, new Cache(false, setting.getDefault()));
			}
		}
		config.save();
	}

	public abstract static class SettingObject<T> {

		private final String key;
		private final String path;
		private final T defaultValue;
		private final String[] comments;

		public SettingObject(Class<? extends AbilityBase> abilityClass, String key, T defaultValue, String... comments) {
			AbilityManifest manifest = checkNotNull(abilityClass).getAnnotation(AbilityManifest.class);
			if (manifest != null) {
				this.path = "능력." + manifest.name() + "." + checkNotNull(key);
			} else {
				throw new IllegalArgumentException(abilityClass.getName() + " 클래스에 AbilityManifest 어노테이션이 존재하지 않습니다.");
			}

			this.key = key;
			this.defaultValue = checkNotNull(defaultValue);
			this.comments = comments;
			registerSetting(this);
		}

		public String getKey() {
			return key;
		}

		public String getPath() {
			return path;
		}

		public T getDefault() {
			return defaultValue;
		}

		public String[] getComments() {
			return comments;
		}

		public Class<T> getDataType() {
			return (Class<T>) defaultValue.getClass();
		}

		public abstract boolean Condition(T value);

		@SuppressWarnings("unchecked")
		public T getValue() {
			if (!isLoaded()) {
				logger.log(Level.SEVERE, "콘피그가 불러와지지 않은 상태에서 접근을 시도하였습니다.");
				throw new IllegalStateException("콘피그가 아직 불러와지지 않았습니다.");
			}
			if (lastModified != file.lastModified()) {
				try {
					_update();
				} catch (IOException | InvalidConfigurationException e) {
					logger.log(Level.SEVERE, "콘피그를 다시 불러오는 도중 오류가 발생하였습니다.");
				}
			}

			Object value = cache.get(this).getValue();

			if (value != null && value.getClass().isAssignableFrom(defaultValue.getClass())) {
				T castedValue = (T) value;
				if (Condition(castedValue)) {
					return castedValue;
				} else {
					return defaultValue;
				}
			} else {
				return defaultValue;
			}
		}

		public boolean setValue(T value) {
			if (Condition(value)) {
				cache.put(this, new Cache(true, value));
				return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return String.valueOf(getValue());
		}

	}

}
