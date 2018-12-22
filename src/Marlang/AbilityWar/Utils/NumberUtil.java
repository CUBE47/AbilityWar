package Marlang.AbilityWar.Utils;

/**
 * Math Util
 * @author _Marlang ����
 */
public class NumberUtil {
	
	public static boolean isInt(String s) {
		boolean isInt = true;
		try {
			Integer.parseInt(s);
		} catch(Exception e) {
			isInt = false;
		}
		return isInt;
	}
	
	public static String parseTimeString(Integer Second) {
		Integer Minute = Second / 60;
		Second -= Minute * 60;
		
		return (Minute != 0 ? Minute + "�� " : "") + (Second != 0 ? Second + "��" : "");
	}
	
}
