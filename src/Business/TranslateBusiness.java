package Business;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import po.Language;
import util.FileUtil;
import util.PatternUtil;

public class TranslateBusiness {

	public static void main(String[] args) {
		String content = "张三,?。！？李四,王五.!";

		// System.out.println(index_list);
		// index_list.sort((Integer a, Integer b) -> a.compareTo(b));
		// System.out.println(index_list);

		// List<String> list = fenkai(content, ",");
		//
		// for(String str:list)
		//
		// System.out.println(str);
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

	public static void translateFromFileToFile(String fromFile, String toFile, String from , String to) throws Exception {

		String content_source = FileUtil.fileReadToString(fromFile);
		// 分段
		List<String> paragraph_list = fenkai(content_source, "\r\n");
		
		String content_transelated = "";
		for (String paragraph : paragraph_list) {
			// 分句
			List<String> sentence_list = analizeParagraph(paragraph);
			// 翻译句子们
			String paragraph_translated = Language.translate(sentence_list, from, to);
			content_transelated += paragraph_translated;
		}
		
		FileUtil.writeIntoFileWithDir(toFile, content_transelated);
	}



	private static List<String> analizeParagraph(String paragraph) {

		List<String> splitFlag_list = new ArrayList<String>();
		List<String> splitFlag_CHANESE_list = new ArrayList<String>();
		List<String> splitFlag_ENGLISH_list = new ArrayList<String>();
		splitFlag_CHANESE_list.add("。");
		splitFlag_CHANESE_list.add("？");
		splitFlag_CHANESE_list.add("！");
		splitFlag_ENGLISH_list.add(".");
		splitFlag_ENGLISH_list.add("?");
		splitFlag_ENGLISH_list.add("!");

		splitFlag_list.addAll(splitFlag_CHANESE_list);
		splitFlag_list.addAll(splitFlag_ENGLISH_list);

		List<String> split_result = new ArrayList<String>();
		int start = 0;
		for (int i = 0; i < paragraph.length(); i++) {
			char charAt = paragraph.charAt(i);
			if (splitFlag_list.contains(String.valueOf(charAt))) {
				String sub = paragraph.substring(start, i);
				split_result.add(sub);
				start = i;
			}
		}

		return split_result;
	}

}

