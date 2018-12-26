package Enum;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import po.Language2;

public enum Punctuation {

	// 中文区
	DOU_CHINESE("，", "CHINESE", false, null, true, 1, false), JU_CHINESE("。", "CHINESE", false, null, true, 2,
			true), WEN_CHINESE("？", "CHINESE", false, null, true, 3, true), TAN_CHINESE("！", "CHINESE", false, null,
					true, 4, true), DUN_CHINESE("、", "CHINESE", false, null, false, null, false), ZHONGHENGXIAN_CHINESE(
							"–", "CHINESE", false, null, false, null,
							false), DAKUOHAO_CHINESE("{", "CHINESE", true, "}", true, 5, false), ZHONGKUOHAO_CHINESE(
									"【", "CHINESE", true, "】", true, 6, false), XIAOKUOHAO_CHINESE("（", "CHINESE", true,
											"）", true, 7, false), JIANKUOHAO_CHINESE("《", "CHINESE", true, "》", true, 8,
													false), YINHAO_DAN_CHINESE("‘", "CHINESE", true, "’", true, 9,
															false), YINHAO_SHUANG_CHINESE("“", "CHINESE", true, "”",
																	true, 10, false),
	// 英文区
																	DOU_ENGLISH(",", "ENGLISH", false, null, true, 1,
																			false), JU_ENGLISH(".", "ENGLISH", false,
																					null, true, 2,
																					true), WEN_ENGLISH("?", "ENGLISH",
																							false, null, true, 3,
																							true), TAN_ENGLISH("!",
																									"ENGLISH", false,
																									null, true, 4,
																									true), ZHONGHENGXIAN_ENGLISH(
																											"-",
																											"ENGLISH",
																											false, null,
																											false, null,
																											false), DAKUOHAO_ENGLISH(
																													"{",
																													"ENGLISH",
																													true,
																													"}",
																													true,
																													5,
																													false), ZHONGKUOHAO_ENGLISH(
																															"[",
																															"ENGLISH",
																															true,
																															"]",
																															true,
																															6,
																															false), XIAOKUOHAO_ENGLISH(
																																	"(",
																																	"ENGLISH",
																																	true,
																																	")",
																																	true,
																																	7,
																																	false), JIANKUOHAO_ENGLISH(
																																			"<",
																																			"ENGLISH",
																																			true,
																																			">",
																																			true,
																																			8,
																																			false), YINHAO_DAN_ENGLISH(
																																					"'",
																																					"ENGLISH",
																																					true,
																																					"'",
																																					true,
																																					9,
																																					false), YINHAO_SHUANG_ENGLISH(
																																							"\"",
																																							"ENGLISH",
																																							true,
																																							"\"",
																																							true,
																																							10,
																																							false);

	public static List<String> getPunctuationsSentenseSplit_symbol(String language) throws Exception {

		language = Language2.toFormmatLan(language);

		List<String> result = new ArrayList<String>();

		Punctuation[] punctuation_arr = Punctuation.values();
		for (Punctuation punctuation : punctuation_arr) {
			String language2 = punctuation.getLanguage();
			boolean senteseSplit = punctuation.isSenteseSplit();
			if (senteseSplit && language.equals(language2))
				result.add(punctuation.getSymbol());
		}

		return result;
	}

	public static List<Punctuation> getPunctuationsSentenseSplit(String language) {

		List<Punctuation> result = new ArrayList<Punctuation>();

		Punctuation[] punctuation_arr = Punctuation.values();
		for (Punctuation punctuation : punctuation_arr) {
			String language2 = punctuation.getLanguage();
			boolean senteseSplit = punctuation.isSenteseSplit();
			if (senteseSplit && language.equals(language2))
				result.add(punctuation);
		}
		return result;
	}

	public static List<String> getSymbolsByLanguage(String language) {

		List<String> result = new ArrayList<String>();

		Punctuation[] punctuationList = Punctuation.values();
		for (Punctuation punctuation : punctuationList) {
			String language2 = punctuation.getLanguage();
			if (language.equals(language2)) {
				String symbol = punctuation.getSymbol();
				result.add(symbol);
			}
		}
		return result;
	}

	public static List<Punctuation> getPunctuationsAll() {

		List<Punctuation> result = new ArrayList<Punctuation>();

		Punctuation[] punctuationList = Punctuation.values();
		for (Punctuation punctuation : punctuationList) {
			result.add(punctuation);
		}
		return result;
	}

	public static List<String> exchangePuctuation(List<String> limitNum, String language) {

		List<Punctuation> all = Punctuation.getPunctuationsAll();
		List<Punctuation> punctuationsByLanguage = Punctuation.getPunctuationsByLanguage(language);

		List<String> dstList = new ArrayList<String>();
		for (String src : limitNum) {
			String dst = src;
			for (Punctuation punctuation : all) {
				String symbol = punctuation.getSymbol();
				
				if (symbol.equals("–")) {
					System.out.println();
				}
				
				
				boolean canClose = punctuation.isCanClose();
				if (!canClose)
					if (!src.contains(symbol))
						continue;
				String closeFlag = "";
				if (canClose) {
					closeFlag = punctuation.getCloseFlag();
					if (!src.contains(symbol) && !src.contains(closeFlag))
						continue;
				}

				boolean canTranslate = punctuation.isCanTranslate();
				if (!canTranslate) {
					dst = dst.replace(symbol, " ");
					continue;
				}

				Integer matchNumber = punctuation.getMatchNumber();
				String matchSymbol = "";
				String matchCloseFlag = "";
				for (Punctuation byLanguage : punctuationsByLanguage) {
					Integer matchNumber2 = byLanguage.getMatchNumber();
					if (matchNumber == matchNumber2) {
						matchSymbol = byLanguage.getSymbol();
						dst = dst.replace(symbol, matchSymbol);
						if (canClose) {
							matchCloseFlag = byLanguage.getCloseFlag();
							dst = dst.replace(closeFlag, matchCloseFlag);
						}
						break;
					}
				}
			}
			dstList.add(dst);
		}

		return dstList;
	}

	public static List<Punctuation> getPunctuationsByLanguage(String language) {

		List<Punctuation> result = new ArrayList<Punctuation>();

		Punctuation[] punctuationList = Punctuation.values();
		for (Punctuation punctuation : punctuationList) {
			String language2 = punctuation.getLanguage();
			if (language.equals(language2))
				result.add(punctuation);
		}
		return result;
	}

	public static List<JSONObject> getPunctuationsByCanClose_symbol_close(String language) throws Exception {

		language = Language2.toFormmatLan(language);

		List<JSONObject> result = new ArrayList<JSONObject>();

		Punctuation[] values = Punctuation.values();
		for (Punctuation punctuation : values) {
			String language2 = punctuation.getLanguage();
			boolean canClose = punctuation.isCanClose();
			if (canClose && language2.equals(language)) {
				JSONObject json = new JSONObject();
				String closeFlag = punctuation.getCloseFlag();
				String symbol = punctuation.getSymbol();
				json.put("symbol", symbol);
				json.put("close", closeFlag);
				result.add(json);
			}
		}

		return result;
	}

	public static List<Punctuation> getPunctuationsByCanClose(String language) {

		List<Punctuation> result = new ArrayList<Punctuation>();

		Punctuation[] values = Punctuation.values();
		for (Punctuation punctuation : values) {
			String language2 = punctuation.getLanguage();
			boolean canClose = punctuation.isCanClose();
			if (canClose && language2.equals(language))
				result.add(punctuation);
		}
		return result;
	}

	private Punctuation(String symbol, String language, boolean canClose, String closeFlag, boolean canTranslate,
			Integer matchNumber, boolean senteseSplit) {

		this.symbol = symbol;
		this.language = language;
		this.canClose = canClose;
		this.closeFlag = closeFlag;
		this.canTranslate = canTranslate;
		this.matchNumber = matchNumber;
		this.senteseSplit = senteseSplit;
	}

	private String symbol;
	private String language;
	private boolean canClose;
	private String closeFlag;
	private boolean canTranslate;
	private Integer matchNumber;
	private boolean senteseSplit;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCloseFlag() {
		return closeFlag;
	}

	public void setCloseFlag(String closeFlag) {
		this.closeFlag = closeFlag;
	}

	public boolean isCanTranslate() {
		return canTranslate;
	}

	public void setCanTranslate(boolean canTranslate) {
		this.canTranslate = canTranslate;
	}

	public Integer getMatchNumber() {
		return matchNumber;
	}

	public void setMatchNumber(Integer matchNumber) {
		this.matchNumber = matchNumber;
	}

	public boolean isCanClose() {
		return canClose;
	}

	public void setCanClose(boolean canClose) {
		this.canClose = canClose;
	}

	public boolean isSenteseSplit() {
		return senteseSplit;
	}

	public void setSenteseSplit(boolean senteseSplit) {
		this.senteseSplit = senteseSplit;
	}
}
