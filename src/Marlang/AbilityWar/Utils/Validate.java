package Marlang.AbilityWar.Utils;

/**
 * Validate
 * @author _Marlang ����
 */
public class Validate {

	/**
	 * null üũ
	 * @param objects						null üũ�� �� ��ü��
	 * @throws IllegalArgumentException		��ü�� �ϳ��� null�� ���
	 */
	public static void NotNull(Object... objects) throws IllegalArgumentException {
		if(objects != null) {
			for(Object o : objects) {
				if(o == null) {
					throw new IllegalArgumentException("Null");
				}
			}
		} else {
			throw new IllegalArgumentException();
		}
	}
	
}
