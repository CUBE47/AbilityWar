package DayBreak.AbilityWar.Utils;

import DayBreak.AbilityWar.Addon.Addon;
import DayBreak.AbilityWar.Addon.AddonLoader;

/**
 * Reflection Util
 * @author DayBreak ����
 */
public class ReflectionUtil {

	private ReflectionUtil() {}
	
	/**
	 * Class Util
	 */
	public static class ClassUtil {

		private ClassUtil() {}
		
		public static Class<?> forName(String name) throws ClassNotFoundException {
			try {
				return Class.forName(name);
			} catch (ClassNotFoundException exceptionOne) {
				for(Addon addon : AddonLoader.getAddons()) {
					try {
						return Class.forName(name, true, addon.getClassLoader());
					} catch (ClassNotFoundException exceptionTwo) {}
				}
			}
			
			throw new ClassNotFoundException(name + " Ŭ������ ã�� ���Ͽ����ϴ�.");
		}
		
	}
	
}
