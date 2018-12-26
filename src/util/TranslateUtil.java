package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

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
		// 百度翻译
		List<String> translateresult = BaiDu(query, "en", "zh");
		for (String result : translateresult)
			System.out.println(result);
	}

	// 百度翻译
	public static List<String> BaiDu(String query, String from, String to) throws Exception {

		String q = query;
		if ("".equals(q)) {
			List<String> resultList = new ArrayList<String>();
			resultList.add(q);
			return resultList;
		}

		Integer salt_int = (int) ((Math.random() * 100) / 1);
		String salt = String.valueOf(salt_int);
		
		String sign_source = APPID + q + salt_int + P;
		String sign = DigestUtils.md5Hex(sign_source);
		// get请求
		String result_url = TRANSLATE_URL + "?" + "q=" + q + "&from=" + from + "&to=" + to + "&appid=" + APPID
				+ "&salt=" + salt + "&sign=" + sign;

		String result = HttpUtil.postByGetUrl(result_url);

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
