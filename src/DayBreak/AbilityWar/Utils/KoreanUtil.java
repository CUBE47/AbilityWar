package DayBreak.AbilityWar.Utils;

public class KoreanUtil {

	/**
	 * ��ħ ���� ���ο� ���� ���ڿ��� �����Ͽ� ��ȯ�մϴ�.
	 * @param str			Ȯ���� ���ڿ�
	 * @param firstValue	��ħ�� ���� �� ����� ��
	 * @param secondValue	��ħ�� ���� �� ����� ��
	 */
	public static final String getCompleteWord(String str, String firstValue, String secondValue) {
		char lastChar = str.charAt(str.length() - 1);

		if (lastChar < 0xAC00 || lastChar > 0xD7A3) return str;
		return str + ((lastChar - 0xAC00) % 28 > 0 ? firstValue : secondValue);
	}

}
