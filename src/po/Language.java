package po;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import Enum.Punctuation;
import util.PatternUtil;
import util.TranslateUtil;

public class Language {

	public static final String CHINESE = "CHINESE";
	public static final String ENGLISH = "ENGLISH";
	public static final Integer LIMIT_CHINESE = 2000;
	public static final Integer LIMIT_ENGLISH = 6000;

	private static String lastLanguage = "lan";
	private static String currentLanguage = "lan";
	private static boolean languageChanged = false;

	private static List<JSONObject> list = new ArrayList<JSONObject>();

	private static List<JSONObject> analyzeSentence(String content) throws Exception {

		// 换行编码, 避免多个返回值
		// content = encodeReturnFlag(content);

		// 不同语言分区
		int start = 0;
		int length = content.length();
		for (int i = 0; i < length; i++) {
			char charAt = content.charAt(i);
			chargeChar(charAt);
			if (languageChanged) {
				JSONObject json = getPreparedJson();
				String sub_sentence = content.substring(start, i);
				json.put("lan", lastLanguage);
				json.put("content", sub_sentence);
				json.put("start", start);
				json.put("end", i);
				start = i;
			}
		}

		String last_content = content.substring(start);
		JSONObject json = getPreparedJson();
		json.put("lan", currentLanguage);
		json.put("content", last_content);
		json.put("start", start);
		json.put("end", length - 1);

		return list;
	}

	private static JSONObject getPreparedJson() {

		list.add(new JSONObject());
		return list.get(list.size() - 1);
	}

	private static void chargeChar(char Char) throws Exception {

		String language = PatternUtil.judgeLageuage(Char);
		if (null == language) {
			languageChanged = false;
			return;
		}
		language = toFormmatLan(language);
		if (!language.equals(currentLanguage)) {
			languageChanged = true;
			lastLanguage = currentLanguage;
			currentLanguage = language;
		} else {
			languageChanged = false;
		}

	}


	public static String toCommonLan(String lan) throws Exception {

		switch (lan) {
		case CHINESE:
			return "zh";
		case "zh":
			return "zh";
		case ENGLISH:
			return "en";
		case "en":
			return "en";
		case "lan":
			return "lan";
		}
		throw new Exception("没有匹配到语言类型");
	}

	public static String toFormmatLan(String lan) throws Exception {

		switch (lan) {
		case "zh":
			return CHINESE;
		case "en":
			return ENGLISH;
		case "lan":
			return "lan";
		}
		throw new Exception("没有匹配到语言类型");
	}

	public static String translate(String contentNeed, String from, String to) throws Exception {

		Content content = new Content();
		content.setFrom(from);
		content.setTo(to);
		content.setContent(contentNeed);

		String content2 = content.getContent();
		String content_trans = content.getContent_trans();

		System.out.println(content_trans.length());
		List<String> limitNum = limitNum(content_trans, from);
		// List<String> exchangePuctuation = exchangePuctuation(limitNum, from);
		String lan = from;
		switch (from) {
		case "en":
			lan = ENGLISH;
			break;
		case "zh":
			lan = CHINESE;
			break;
		}
		List<String> exchangePuctuation = Punctuation.exchangePuctuation(limitNum, lan);

		for (String src : exchangePuctuation) {
			System.out.println("原文");
			System.out.println(src);

			List<String> resultList = TranslateUtil.BaiDu(src, from, to);
			for (String str : resultList) {
				System.out.println("译文");
				System.out.println(str);
			}
		}

		return null;
	}

	private static void init() {

		lastLanguage = "lan";
		currentLanguage = "lan";
		languageChanged = false;
		list = new ArrayList<JSONObject>();
	}

	private static List<String> fenkai(String content, String splitFlag) {

		String ahead = "";
		String foot = "";
		List<String> result = new ArrayList<String>();
		if (content.contains(splitFlag)) {
			int index = content.indexOf(splitFlag);
			ahead = content.substring(0, index + 1);
			foot = content.substring(index + 1);
			result.add(ahead);

			if (foot.contains(splitFlag)) {
				List<String> list = fenkai(foot, splitFlag);
				result.addAll(list);
			} else {
				result.add(foot);
			}
		}

		return result;
	}

	private static List<String> limitNum(String content, String from) throws Exception {

		int limit = -1;

		switch (from) {
		case "en":
			limit = LIMIT_ENGLISH;
			break;
		case ENGLISH:
			limit = LIMIT_ENGLISH;
			break;
		case "zh":
			limit = LIMIT_CHINESE;
			break;
		case CHINESE:
			limit = LIMIT_CHINESE;
			break;
		}

		if (limit < 0)
			throw new Exception("未匹配到语言");

		List<String> resultList = new ArrayList<String>();
		if (content.length() < limit) {
			resultList.add(content);
			return resultList;
		}

		// 中文 2000 字 (带空格和标点)
		// 英文 6000字(带空格和标点)
		if (!content.contains("\r\n"))
			throw new Exception("单句超过字数限制");

		String first = "";
		String second = "";

		int index = content.indexOf("\r\n");
		index += 4;

		if (index + 1 > limit)
			throw new Exception("单句超过字数限制");

		first = content.substring(0, index + 1);
		second = content.substring(index + 1);

		if (second.length() > limit) {
			resultList.add(first);
			List<String> resList = limitNum(second, from);
			resultList.addAll(resList);
		}

		return resultList;
	}

}