package DayBreak.AbilityWar.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import DayBreak.AbilityWar.Development.Addon.Addon;
import DayBreak.AbilityWar.Development.Addon.AddonLoader;

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

	/**
	 * Field Util
	 */
	public static class FieldUtil {

		private FieldUtil() {}
		
		public static List<Field> getDeclaredInheritedFields(Class<?> clazz) {
			final List<Field> result = new ArrayList<Field>();

		    Class<?> finding = clazz;
		    while (finding != null && finding != Object.class) {
		        for (Field field : finding.getDeclaredFields()) if (!field.isSynthetic()) result.add(field);
		        finding = finding.getSuperclass();
		    }

		    return result;
		}
		
	}
	
}
