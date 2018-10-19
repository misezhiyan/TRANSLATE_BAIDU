package Business;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import po.Language;
import util.FileUtil;
import util.PatternUtil;

public class TranslateBusiness2 {

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

	public static void translateFromFileToFile(String fromFile, String toFile, String from, String to)
			throws Exception {

		String content_source = FileUtil.fileReadToString(fromFile);

		String content = formmatContent(content_source);

		String transResult = Language.translate1(content, from, to);

		FileUtil.writeIntoFileWithDir(toFile, transResult);
	}

	private static String formmatContent(String content_source) {

		content_source = content_source.replace("’", "'");
		content_source = content_source.replace("“", "\"").replaceAll("”", "\"");
		content_source = content_source.replace("。", ".");
		content_source = content_source.replace("，", ",");
		content_source = content_source.replace("？", "?");
		content_source = content_source.replace("！", "!");

		return content_source;
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
