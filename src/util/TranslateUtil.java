package util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TranslateUtil {

	private static String TRANSLATE_URL = "http://api.fanyi.baidu.com/api/trans/vip/translate";
	private static String APPID = "20180103000111543";
	private static String P = "hKhTn4umupyJxY61Beny";
	private static String FROM = "auto";
	private static String TO = "zh";
	private static Integer LIMIT_CHINESE = 2000;
	private static Integer LIMIT_ENGLISH = 6000;

	// 语言简写 名称
	// auto 自动检测
	// zh 中文
	// en 英语
	// yue 粤语
	// wyw 文言文
	// jp 日语
	// kor 韩语
	// fra 法语
	// spa 西班牙语
	// th 泰语
	// ara 阿拉伯语
	// ru 俄语
	// pt 葡萄牙语
	// de 德语
	// it 意大利语

	// 请将单次请求长度控制在 6000 bytes以内。（汉字约为2000个）

	public static void main(String[] args) throws Exception {

		// Scanner sn = new Scanner(System.in);
		// String query = sn.nextLine();
		// sn.close();
		// String query = "this is a test(这是个测试).\r\ngive you some coler to see
		// see";

		String query = "";
		query += "We have sent our feedbacks through emails to Diana Tang and Yuan Hongyu respectively when the China market first announced that the LSS and PCS would be combined. But these two people did not do any investigation. It is not until we sent the emails again to Duan Xiaoying  CEO in China, and the Legal Department that did some of the related investigations begin. Not unnaturally, we didn't receive the results that we expected. We have a feeling that Diana Tang and Yuan Hongyu were hiding back from this situation, or in other words, in China, we need a strong social relationship and money to have problems solved. True or not, your people know.";

		System.out.println(query);
		List<String> translateresult = translate2(query, "en", "zh");
		for (String result : translateresult)
			System.out.println(result);
	}

	// 默认 中文 转英文
	public static String translate(String query) throws Exception {
		return translate(query, FROM, TO);
	}

	public static String translate(String query, String from, String to) throws Exception {

		String q = query;

		Random random = new Random();
		int salt_int = random.nextInt();
		String salt = String.valueOf(salt_int);

		String sign_source = APPID + q + salt_int + P;
		String sign = DigestUtils.md5Hex(sign_source);

		q = URLEncoder.encode(q, "UTF-8");
		String result_url = TRANSLATE_URL + "?" + "q=" + q + "&from=" + from + "&to=" + to + "&appid=" + APPID
				+ "&salt=" + salt + "&sign=" + sign;

		String result = HttpUtil.htmlGetWithCookie(result_url, null);

		List<String> transelated_result = analyzeResult(result, q);
		return transelated_result.get(0);

		// return result;
	}

	public static List<String> translate2(String query, String from, String to) throws Exception {

		String q = query;
		if ("".equals(q)) {
			List<String> resultList = new ArrayList<String>();
			resultList.add(q);
			return resultList;
		}

		//q = q.replace("–", "");

		Integer salt_int = (int) ((Math.random() * 100) / 1);
		String salt = String.valueOf(salt_int);

		// q = q.replace(" ", "");
		// q = q.replace(" ", "");
		// while(q.contains("\r\n\r\n"))
		// q = q.replace("\r\n\r\n", "\r\n");

		//q = "Dear Sir/Ma'm,\r\n" + "Good Morning/Afternoon/Evening!\r\n" + "\r\n" + "\r\n" + "\r\n";//"Dear Sir/Ma’m,\r\n" + 
//		q = "";
		
		String sign_source = APPID + q + salt_int + P;
		String sign = DigestUtils.md5Hex(sign_source);
		// get请求
		// q = URLEncoder.encode(q, "UTF-8");
		String result_url = TRANSLATE_URL + "?" + "q=" + q + "&from=" + from + "&to=" + to + "&appid=" + APPID
				+ "&salt=" + salt + "&sign=" + sign;

		String result = HttpUtil.postWithParams(result_url);
		// String result = HttpUtil.htmlGetWithCookie(result_url, null);

		List<String> transelated_result = analyzeResult(result, q);
		return transelated_result;

	}

	private static List<String> analyzeResult(String result, String q) throws Exception {

		JSONObject json = JSONObject.parseObject(result);

		String error_code = json.getString("error_code");
		if (null != error_code) {
			switch (error_code) {
			case "54001":
				throw new Exception("签名错误");
			}
		}

		// https://md5jiami.51240.com/web_system/51240_com_www/system/file/md5jiami/data/?ajaxtimestamp=1526896718823

		JSONArray jsonArray = json.getJSONArray("trans_result");

		List<String> resultList = new ArrayList<String>();

		Iterator<Object> iterator = jsonArray.iterator();
		while (iterator.hasNext()) {
			JSONObject next = (JSONObject) iterator.next();
			String dst = next.getString("dst");
			resultList.add(dst);
		}

		return resultList;

	}

	public static Integer getLimitNum(String language) throws Exception {

		switch (language) {
		case "en":
			return LIMIT_ENGLISH;
		case "zh":
			return LIMIT_CHINESE;
		}

		throw new Exception("没有匹配类型");
	}

}
