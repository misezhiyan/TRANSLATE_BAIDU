package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import po.Language;

public class PatternUtil {
	private static String REGEX_CHANESE = "[\u4e00-\u9fa5]";
	private static String REGEX_ENGLISH = "[A-Za-z]";
	
	public static void main(String[] args) {
		char a = '张';
		a = 'a';
		a = ',';
		boolean matchChinese = matchChinese(a);
		boolean matchEnglish = matchEnglish(a);
		System.out.println(matchChinese);
		System.out.println(matchEnglish);
	}
	
	public static boolean matchChinese(char Char) {
		Pattern pattern = Pattern.compile(REGEX_CHANESE);
		Matcher matcher = pattern.matcher(String.valueOf(Char));
		return matcher.find();
	}
	
	public static boolean matchEnglish(char Char) {
		Pattern pattern = Pattern.compile(REGEX_ENGLISH);
		Matcher matcher = pattern.matcher(String.valueOf(Char));
		return matcher.find();
	}
	
	public static String judgeLageuage(char Char) {
		if(matchChinese(Char))return Language.CHINESE;
		if(matchEnglish(Char))return Language.ENGLISH;
		return null;
	}
}
