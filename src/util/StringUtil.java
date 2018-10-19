package util;

public class StringUtil {

	public static boolean isEmpty(String str) {
		return null == str || "".equals(str);
	}
	
	public static boolean isBlankEmpty(String str) {
		str = str.trim();
		return null == str || "".equals(str);
	}
	
}
