package DayBreak.AbilityWar.Utils;

/**
 * Validate
 * @author DayBreak ����
 */
public class Validate {

	private Validate() {}

	/**
	 * ��ü�� null���� �ƴ��� Ȯ���մϴ�.
	 * @param object						null ���θ� Ȯ���� ��ü
	 * @throws NullPointerException			��ü�� null�� ���
	 * @return								��ü�� null�� �ƴ� ��� �״�� ��ȯ�մϴ�.
	 */
	public static <T> T notNull(T object) throws NullPointerException {
		if(object == null) throw new NullPointerException();
		return object;
	}

	public static void MinimumConstant(Class<?> enumClass, int count) throws IllegalArgumentException {
		if(notNull(enumClass).isEnum()) {
			if(enumClass.getEnumConstants().length < count) {
				throw new IllegalArgumentException(enumClass.getName() + "�� �ּ� " + count + "���� ����� �־�� �մϴ�.");
			}
		} else {
			throw new IllegalArgumentException();
		}
	}
	
}
