package daybreak.abilitywar.game.manager;

import com.google.common.base.Preconditions;
import daybreak.abilitywar.config.Configuration.Settings.DeveloperSettings;
import daybreak.abilitywar.game.AbstractGame;
import daybreak.abilitywar.game.GameManifest;
import daybreak.abilitywar.game.list.changeability.ChangeAbilityWar;
import daybreak.abilitywar.game.list.debug.DebugMode;
import daybreak.abilitywar.game.list.mixability.MixAbility;
import daybreak.abilitywar.game.list.standard.DefaultGame;
import daybreak.abilitywar.game.list.standard.WarGame;
import daybreak.abilitywar.game.list.summervacation.SummerVacation;
import daybreak.abilitywar.game.list.teamfight.TeamFight;
import daybreak.abilitywar.game.list.zerotick.ZeroTick;
import daybreak.abilitywar.game.manager.GameFactory.GameRegistration.Flag;
import daybreak.abilitywar.utils.annotations.Beta;
import daybreak.abilitywar.utils.base.logging.Logger;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameFactory {

	private GameFactory() {
	}

	private static final Logger logger = Logger.getLogger(GameFactory.class);

	private static final Map<String, Class<? extends AbstractGame>> usedNames = new LinkedHashMap<>();
	private static final Map<Class<? extends AbstractGame>, GameRegistration> registeredModes = new HashMap<>();

	static {
		registerMode(DefaultGame.class);
		registerMode(WarGame.class);
		registerMode(ChangeAbilityWar.class);
		registerMode(SummerVacation.class);
		registerMode(TeamFight.class);
		registerMode(MixAbility.class);
		registerMode(ZeroTick.class);
		if (DeveloperSettings.isEnabled()) GameFactory.registerMode(DebugMode.class);
	}

	public static GameRegistration getRegistration(Class<? extends AbstractGame> clazz) {
		return registeredModes.get(clazz);
	}

	public static boolean isRegistered(Class<? extends AbstractGame> clazz) {
		return registeredModes.containsKey(clazz);
	}

	public static void registerMode(Class<? extends AbstractGame> gameClass) {
		if (!registeredModes.containsKey(gameClass)) {
			try {
				GameRegistration registration = new GameRegistration(gameClass);
				String name = registration.getManifest().Name();
				if (!usedNames.containsKey(name)) {
					if (!registration.hasFlag(Flag.BETA) || DeveloperSettings.isEnabled()) {
						registeredModes.put(gameClass, registration);
						usedNames.put(name, gameClass);
					}
				} else {
					logger.debug("§e" + gameClass.getName() + " §f게임모드는 겹치는 이름이 있어 등록되지 않았습니다.");
				}
			} catch (NoSuchMethodException | NullPointerException e) {
				logger.error(e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : ("§e" + gameClass.getName() + " §f게임 모드 등록 중 오류가 발생하였습니다."));
			}
		}
	}

	public static List<String> nameValues() {
		return new ArrayList<>(usedNames.keySet());
	}

	public static Class<? extends AbstractGame> getByName(String name) {
		return usedNames.get(name);
	}

	public static class GameRegistration {

		private final Class<? extends AbstractGame> clazz;
		private final Constructor<? extends AbstractGame> constructor;
		private final GameManifest manifest;
		private final int flag;

		private GameRegistration(Class<? extends AbstractGame> clazz) throws NullPointerException, NoSuchMethodException, SecurityException {
			this.clazz = clazz;
			this.constructor = clazz.getConstructor();

			if (!clazz.isAnnotationPresent(GameManifest.class))
				throw new IllegalArgumentException("GameManifest가 없는 게임 모드입니다.");
			this.manifest = clazz.getAnnotation(GameManifest.class);
			Preconditions.checkNotNull(manifest.Name());
			Preconditions.checkNotNull(manifest.Description());

			int flag = 0x0;
			if (clazz.isAnnotationPresent(Beta.class)) flag |= Flag.BETA;
			this.flag = flag;
		}

		public Class<? extends AbstractGame> getGameClass() {
			return clazz;
		}

		public Constructor<? extends AbstractGame> getConstructor() {
			return constructor;
		}

		public GameManifest getManifest() {
			return manifest;
		}

		public boolean hasFlag(int flag) {
			return (this.flag & flag) == flag;
		}

		public static class Flag {
			public static final int BETA = 0x1;
		}

	}

}
