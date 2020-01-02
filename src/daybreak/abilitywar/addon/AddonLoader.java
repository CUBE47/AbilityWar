package daybreak.abilitywar.addon;

import daybreak.abilitywar.addon.exception.InvalidAddonException;
import daybreak.abilitywar.utils.database.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * 애드온에 직접적으로 엑세스하여 처리하는 로더입니다.
 *
 * @author Daybreak 새벽
 */
public class AddonLoader {

	private AddonLoader() {
	}

	private static final Logger logger = Logger.getLogger(AddonLoader.class.getName());
	private static final HashMap<String, Addon> addons = new HashMap<>();

	public static Collection<ClassLoader> getClassLoaders() {
		return addons.values().stream().map(Addon::getClassLoader).collect(Collectors.toList());
	}

	/**
	 * 애드온 디렉토리에 있는 모든 애드온을 불러옵니다.
	 */
	public static void loadAll() {
		for (File file : FileUtil.newDirectory("Addon").listFiles()) {
			load(file);
		}
	}

	/**
	 * 애드온을 불러옵니다.
	 *
	 * @param file 애드온 파일
	 * @return 불러온 애드온, 불러오는 도중 오류가 발생하였을 경우 null
	 */
	public static Addon load(File file) {
		try {
			Addon instance = new Addon.Builder(file).build();
			String name = instance.getDescription().getName();
			if (checkAddon(name)) {
				throw new InvalidAddonException(name + ": 중복되는 이름의 애드온이 존재하거나 이미 등록된 애드온입니다.");
			}
			addons.put(name, instance);
			return instance;
		} catch (IOException e) {
			logger.log(Level.SEVERE, "애드온을 불러오는 동안 오류가 발생하였습니다.");
		} catch (InvalidAddonException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}

	public static void enableAll() {
		for (Addon addon : addons.values()) {
			try {
				addon.onEnable();
			} catch (Exception e) {
				logger.log(Level.SEVERE, addon.getDescription().getName() + ": 애드온을 활성화하는 도중 오류가 발생하였습니다", e);
			}
		}
	}

	public static void disableAll() {
		for (Addon addon : addons.values()) {
			try {
				addon.onDisable();
			} catch (Exception e) {
				logger.log(Level.SEVERE, addon.getDescription().getName() + ": 애드온을 비활성화하는 도중 오류가 발생하였습니다", e);
			}
		}
	}

	/**
	 * 해당 이름의 애드온이 존재하는지 확인합니다.
	 *
	 * @param name 확인할 이름
	 * @return 존재 여부
	 */
	public static boolean checkAddon(String name) {
		return addons.containsKey(name);
	}

	/**
	 * 해당 이름의 애드온을 반환합니다.
	 *
	 * @param name 확인할 이름
	 * @return 존재할 경우 애드온 인스턴스 반환, 존재하지 않을 경우 null 반환
	 */
	public static Addon getAddon(String name) {
		return addons.get(name);
	}

}
