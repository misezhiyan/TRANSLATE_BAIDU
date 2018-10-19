package po;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PUCTUATION {

	private static SINGLE sinGle = new SINGLE();
	private static DOUBLE douBle = new DOUBLE();

	public static Integer getClosePosition(String match, String context) throws Exception {
		match = match.trim();
		int index = douBle.getClosePosition(match, context);
		return index;
	}

	private static List<Integer> getSituations(String match, String test) {
		List<Integer> situations = new ArrayList<Integer>();
		int index = test.indexOf(match);

		if (index < 0)
			return situations;
		situations.add(index);
		test = test.substring(index + 1);

		int index2 = test.indexOf(match);
		if (index2 < 0)
			return situations;

		List<Integer> situations2 = getSituations(match, test);
		for (Integer situation : situations2) {
			situations.add(situation + index + 1);
		}

		return situations;
	}
}

class SINGLE {
	private static final String[] CHINESE_MATCH = new String[] { "，", "。", "？", "！", "：" };
	private static final String[] ENGLISH_MATCH = new String[] { ",", ".", "?", "!", ":", };
	private static final String[] CHINESE_ONLY = new String[] { "、" };
	private static final String[] ENGLISH_ONLY = new String[] {};
}

class DOUBLE {
	private static final String[][] CHINESE_MATCH = { { "{", "}" }, { "【", "】" }, { "《", "》" }, { "（", "）" },
			{ "“", "”" }, { "‘", "’" } };
	private static final String[][] ENGLISH_MATCH = { { "{", "}" }, { "[", "]" }, { "<", ">" }, { "(", ")" },
			{ "\"", "\"" }, { "'", "'" } };

	public int getClosePosition(String match, String context_aftermatch) throws Exception {

		String start = match;
		String close = getClosePuctuation(match);

		if (!context_aftermatch.contains(close))
			return -1;

		List<Integer> situations_start = getSituations(start, context_aftermatch);
		List<Integer> situations_close = getSituations(close, context_aftermatch);

		Map<String, List<Integer>> judgeClose = new HashMap<String, List<Integer>>();
		judgeClose.put("situations_start", situations_start);
		judgeClose.put("situations_close", situations_close);

		int position = judgeClosePosition(1, judgeClose);

		return 0;
	}

	private int judgeClosePosition(int deep, Map<String, List<Integer>> judgeClose) {// deep是深入层数,
		// 从0开始

		List<Integer> situations_start = judgeClose.get("situations_start");
		List<Integer> situations_close = judgeClose.get("situations_close");

		List<Integer> judgeCloseAll = new ArrayList<Integer>();
		judgeCloseAll.addAll(situations_start);
		judgeCloseAll.addAll(situations_close);

		Collections.sort(judgeCloseAll);

		for (Integer temp : judgeCloseAll) {
			if (situations_start.contains(temp))
				deep++;
			else if (situations_close.contains(temp))
				deep--;

			if (deep == 0)
				return temp;
		}

		return -1;
	}

	private static List<Integer> getSituations(String match, String test) {
		List<Integer> situations = new ArrayList<Integer>();
		int index = test.indexOf(match);

		if (index < 0)
			return situations;
		situations.add(index);
		test = test.substring(index + 1);

		int index2 = test.indexOf(match);
		if (index2 < 0)
			return situations;

		List<Integer> situations2 = getSituations(match, test);
		for (Integer situation : situations2) {
			situations.add(situation + index + 1);
		}

		return situations;
	}

	public String getClosePuctuation(String match) throws Exception {

		for (String[] CHINESE : CHINESE_MATCH) {
			if (CHINESE[0].equals(match))
				return CHINESE[1];
		}
		throw new Exception("没有匹配到对应符号:" + match);
	}
}
