package po;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import Enum.Punctuation;

public class Paragraph {

	private String from = null;
	private String to = null;

	private CloseSpace[] closeSpace_Arr = null;
	private boolean isEmpty = false;

	public String getParagraph() {
		String paragraph = "";
		for (CloseSpace space : closeSpace_Arr) {
			String space2 = space.getSpace();
			paragraph += space2;
		}
		paragraph += "\r\n";
		return paragraph;
	}

	public String getParagraph_trans() {
		String paragraph = "";
		for (CloseSpace space : closeSpace_Arr) {
			boolean closeSpace = space.isCloseSpace();
			if (closeSpace)
				continue;
			String space2 = space.getSpace_trans();
			paragraph += space2;
		}
		return paragraph;
	}

	public void setParagraph(String paragraph) throws Exception {

		if (isEmpty) {
			CloseSpace space = new CloseSpace();
			// sentense.setSentense(paragraph);
			space.setFrom(from);
			space.setTo(to);
			space.setSpace(paragraph);
			closeSpace_Arr = new CloseSpace[] { space };
			return;
		}

		closeSpace_Arr = splitToSpace(paragraph);
	}

	private CloseSpace[] splitToSpace(String paragraph) throws Exception {

		// 1. 获取闭合区域 暂定只有一层
		// 2. 取闭合区域和非闭合区域区间
		// 3. 非闭合区域断句
		// 4. 闭合区域暂不断句
		List<JSONObject> puntuationsByCanClose_symbol_close = Punctuation.getPunctuationsByCanClose_symbol_close(from);
		List<CloseSpace> spaceList = new ArrayList<CloseSpace>();

		for (JSONObject puntuation : puntuationsByCanClose_symbol_close) {

			String symbol = puntuation.getString("symbol");
			if (!paragraph.contains(symbol))
				continue;

			String close = puntuation.getString("close");
			if (!paragraph.contains(close))
				throw new Exception("未闭合");

			int index_symbol = paragraph.indexOf(symbol);
			int index_close = paragraph.indexOf(close);

			String open1 = paragraph.substring(0, index_symbol);
			String close1 = paragraph.substring(index_symbol, index_close + 1);
			String open2 = paragraph.substring(index_close + 1);

			CloseSpace space1 = new CloseSpace();
			space1.setFrom(from);
			space1.setTo(to);
			space1.setSequence(0);
			space1.setSpace(open1);
			CloseSpace space2 = new CloseSpace();
			space2.setCloseSpace(true);
			space2.setFrom(from);
			space2.setTo(to);
			space2.setSequence(1);
			space2.setSpace(close1);
			CloseSpace space3 = new CloseSpace();
			space3.setFrom(from);
			space3.setTo(to);
			space3.setSequence(2);
			space3.setSpace(open2);

			spaceList.add(space1);
			spaceList.add(space2);
			spaceList.add(space3);
		}

		int size = spaceList.size();
		if (size > 0) {
			CloseSpace[] space_arr = new CloseSpace[size];
			for (int i = 0; i < size; i++)
				space_arr[i] = spaceList.get(i);
			return space_arr;
		}

		CloseSpace[] space_arr = new CloseSpace[1];
		CloseSpace space = new CloseSpace();
		space.setCloseSpace(false);
		space.setFrom(from);
		space.setTo(to);
		space.setSpace(paragraph);
		space_arr[0] = space;
		return space_arr;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
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

}
