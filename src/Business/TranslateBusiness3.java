package Business;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import po.Language;
import po.Language2;
import util.FileUtil;
import util.PatternUtil;

public class TranslateBusiness3 {

	public static void translateFromFileToFile(String fromFile, String toFile, String from, String to)
			throws Exception {

		String content_source = FileUtil.fileReadToString(fromFile);

		// String content = formmatContent(content_source);

		String transResult = Language2.translate1(content_source, from, to);

		//FileUtil.writeIntoFileWithDir(toFile, transResult);
	}

	private static String formmatContent(String content_source) {

		content_source = content_source.replace("¡¯", "'");
		content_source = content_source.replace("¡°", "\"").replaceAll("¡±", "\"");
		content_source = content_source.replace("¡£", ".");
		content_source = content_source.replace("£¬", ",");
		content_source = content_source.replace("£¿", "?");
		content_source = content_source.replace("£¡", "!");

		return content_source;
	}

	private static List<String> analizeParagraph(String paragraph) {

		List<String> splitFlag_list = new ArrayList<String>();
		List<String> splitFlag_CHANESE_list = new ArrayList<String>();
		List<String> splitFlag_ENGLISH_list = new ArrayList<String>();
		splitFlag_CHANESE_list.add("¡£");
		splitFlag_CHANESE_list.add("£¿");
		splitFlag_CHANESE_list.add("£¡");
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
