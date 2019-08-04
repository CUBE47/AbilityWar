package DayBreak.AbilityWar.Utils;

/**
 * Validate
 * @author DayBreak ����
 */
public class Validate {

	private Validate() {}
	
	/**
	 * null üũ
	 * @param objects						null üũ�� �� ��ü��
	 * @throws IllegalArgumentException		��ü�� �ϳ��� null�� ���
	 */
	public static void NotNull(Object... objects) throws IllegalArgumentException {
		if(objects == null) throw new IllegalArgumentException();
		for(Object o : objects) if(o == null) throw new IllegalArgumentException("Null�� �Ǿ�� �ȵ˴ϴ�.");
	}
	
	public static void MinimumConstant(Class<?> enumClass, int count) throws IllegalArgumentException {
		NotNull(enumClass);
		if(enumClass.isEnum()) {
			if(enumClass.getEnumConstants().length < count) {
				throw new IllegalArgumentException(enumClass.getName() + "�� �ּ� " + count + "���� ����� �־�� �մϴ�.");
			}
		} else {
			throw new IllegalArgumentException();
		}
	}
	
}
