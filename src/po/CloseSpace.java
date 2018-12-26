package po;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import Enum.Punctuation;

public class CloseSpace {

	private String from = null;
	private String to = null;

	private boolean isCloseSpace;// 闭合域不翻译

	private int sequence;
	private List<Sentense> sentenseList;

	public String getSpace() {

		String space = "";

		for (Sentense sentense : sentenseList) {

			String sentense2 = sentense.getSentense();
			space += sentense2;
		}

		return space;
	}

	public String getSpace_trans() {

		String space = "";

		for (Sentense sentense : sentenseList) {

			String sentense2 = sentense.getSentense_trans();
			space += sentense2;
		}

		return space;
	}

	public void setSpace(String space) throws Exception {

		sentenseList = splitToSentense(space);

	}

	private List<Sentense> splitToSentense(String space) throws Exception {
		// 闭合域
		if (isCloseSpace) {
			List<Sentense> sentenseList = new ArrayList<Sentense>();
			Sentense sentense = new Sentense();
			sentense.setSentense(space);
			sentenseList.add(sentense);
			return sentenseList;
		}

		List<String> puntuationsSentenseSplit_symbol = Punctuation.getPunctuationsSentenseSplit_symbol(from);
		List<JSONObject> sentenseSplit = new ArrayList<JSONObject>();

		// 断句点位置
		boolean needSplit = false;
		for (String symbol : puntuationsSentenseSplit_symbol) {

			if (!space.contains(symbol))
				continue;

			int index = space.indexOf(symbol);

			JSONObject json = new JSONObject();
			json.put("index", index);
			json.put("symbol", symbol);
			sentenseSplit.add(json);
			needSplit = true;
		}
		// 没有断句点
		if (!needSplit) {
			List<Sentense> sentenseList = new ArrayList<Sentense>();

			Sentense sentense = new Sentense();
			sentense.setSentense(space);

			sentenseList.add(sentense);

			return sentenseList;
		}

		int split = -1;
		for (JSONObject json : sentenseSplit) {
			if (-1 == split) {
				split = json.getInteger("index");
				continue;
			}
			split = split < json.getInteger("index") ? split : json.getInteger("index");
		}

		List<Sentense> sentenseList = new ArrayList<Sentense>();
		String first = space.substring(0, split + 1);
		Sentense sentense = new Sentense();
		sentense.setSentense(first);
		sentenseList.add(sentense);

		String secound = space.substring(split + 1);
		List<Sentense> splitToSentense = splitToSentense(secound);

		sentenseList.addAll(splitToSentense);

		return sentenseList;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public boolean isCloseSpace() {
		return isCloseSpace;
	}

	public void setCloseSpace(boolean isCloseSpace) {
		this.isCloseSpace = isCloseSpace;
	}

	public List<Sentense> getSentenseList() {
		return sentenseList;
	}

	public void setSentenseList(List<Sentense> sentenseList) {
		this.sentenseList = sentenseList;
	}

}
