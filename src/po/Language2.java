package po;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import Enum.Punctuation;
import util.PatternUtil;
import util.TranslateUtil;

public class Language2 {

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

	public static String translate(List<String> sentence_list, String from, String to) throws Exception {
		String transResult_sentense = "";

		for (String sentence : sentence_list) {
			analyzeSentence(sentence);
			String trans_lan = toFormmatLan(from);
			for (JSONObject sentenseJson : list) {
				String lan = sentenseJson.getString("lan");
				String sentence_temp = sentenseJson.getString("content");
				if (lan.equals(trans_lan)) {
					String trans_result = TranslateUtil.translate(sentence_temp, from, to);
					transResult_sentense += trans_result;
				} else {
					transResult_sentense += sentence_temp;
				}
			}
		}

		return transResult_sentense;
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

	public static String translate1(String content, String from, String to) throws Exception {

		Content content1 = new Content();
		content1.setFrom(from);
		content1.setTo(to);
		content1.setContent(content);

		String content2 = content1.getContent();
		String content_trans = content1.getContent_trans();

		System.out.println(content_trans.length());
		List<String> limitNum = limitNum(content_trans, from);
//		List<String> exchangePuctuation = exchangePuctuation(limitNum, from);
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

			List<String> resultList = TranslateUtil.translate2(src, from, to);
			for (String str : resultList) {
				System.out.println("译文");
				System.out.println(str);
			}
		}

		return null;

		// // 分段
		// List<String> paragraph_list = fenkai(content, "\r\n");
		// // List<JSONObject> needToTranslateList = new
		// ArrayList<JSONObject>();
		// String neetToTranslate = "";
		// // 用于限制字数
		// Integer length_limit = TranslateUtil.getLimitNum(from);
		//
		// List<String> needToTranslateLimit = new ArrayList<String>();
		// int count = 1;
		// for (String paragraph : paragraph_list) {
		// init();
		// analyzeSentence(paragraph);
		// String trans_lan = matchLan(from);
		// for (JSONObject contentJson : list) {
		// String lan = contentJson.getString("lan");
		// String content_temp = contentJson.getString("content");
		// if (lan.equals(trans_lan)) {
		// // needToTranslateList.add(contentJson);
		// if ((neetToTranslate + content_temp).length() >= length_limit) {
		// if (1 == count)
		// throw new Exception("出现绝对错误, 请联系开发者:15092250419");
		// needToTranslateLimit.add(neetToTranslate);
		// neetToTranslate = content_temp;
		// count = 1;
		// continue;
		// }
		// neetToTranslate += content_temp + "\\n";
		// count++;
		// } else {
		//
		// }
		// }
		// if(!StringUtil.isEmpty(neetToTranslate))neetToTranslate =
		// neetToTranslate.substring(0, neetToTranslate.length() - 3);
		// needToTranslateLimit.add(neetToTranslate);
		// neetToTranslate = "";
		// }
		//
		// List<String> trans_result = new ArrayList<String>();
		// for (String will_translate : needToTranslateLimit) {
		// List<String> trans_result_temp =
		// TranslateUtil.translate2(will_translate, from, to);
		// trans_result.addAll(trans_result_temp);
		// }
		//
		// String translate_content = "";
		// int num = 0;
		// for (String paragraph : paragraph_list) {
		// init();
		// analyzeSentence(paragraph);
		// String trans_lan = matchLan(from);
		// for (JSONObject contentJson : list) {
		// String lan = contentJson.getString("lan");
		// // String content_temp = contentJson.getString("content");
		// // Integer start = contentJson.getInteger("start");
		// // Integer end = contentJson.getInteger("end");
		// if (lan.equals(trans_lan)) {
		// translate_content += trans_result.get(num);
		// num++;
		// } else {
		// translate_content += paragraph;
		// }
		// }
		// translate_content += "\r\n";
		// }
		//
		// return translate_content;
	}

//	private static List<String> exchangePuctuation(List<String> limitNum, String from) {
//		
//		List<String> punctuationsByLanguage = Punctuation.getSymbolsByLanguage(from);
//		
//		for(String src:limitNum){
//			
//		}
//		
//		return null;
//	}

	private static void init() {

		lastLanguage = "lan";
		currentLanguage = "lan";
		languageChanged = false;
		list = new ArrayList<JSONObject>();
	}

	// private static String decodeReturnFlag(String content) {
	//
	// return content.replace("@#$%^&&~`", "\\r\\n");
	// }
	//
	// private static String encodeReturnFlag(String content) {
	//
	// return content.replace("\r\n", "@#$%^&&~`");
	// }

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