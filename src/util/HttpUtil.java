package util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpUtil {

	// public static void main(String[] args) throws HttpException, IOException {
	//
	// String url = "http://www.cnblogs.com/jiajinyi/archive/2013/05/24/3097088.html";
	//
	// String result = postWithParams(url);
	// System.out.println(result);
	// }

	// get 请求 转 post请求
	public static String postByGetUrl(String url) throws HttpException, IOException {

		// 获取参数
		List<NameValuePair> nameValuePair_list = new ArrayList<NameValuePair>();
		int params_index = url.indexOf("?");
		if (params_index > 0) {
			String params_str = url.substring(params_index + 1);
			String[] params = params_str.split("&");
			for (String param : params) {
				String[] param_arr = param.split("=");
				if (param_arr.length != 2)
					continue;
				NameValuePair nameValuePair = new NameValuePair();
				nameValuePair.setName(param_arr[0]);
				nameValuePair.setValue(param_arr[1]);
				if (param_arr[1].contains("We have sent")) {
					System.out.println();
				}
				nameValuePair_list.add(nameValuePair);
			}
		}

		int size = nameValuePair_list.size();
		NameValuePair[] nameValuePair_arr = new NameValuePair[size];
		for (int i = 0; i < size; i++)
			nameValuePair_arr[i] = nameValuePair_list.get(i);
		url = "http://api.fanyi.baidu.com/api/trans/vip/translate";
		String result = postWithParams(url, nameValuePair_arr);
		return result;
	}

	// post 请求
	public static String postWithParams(String url, NameValuePair... nameValuePair_arr) throws HttpException, IOException {

		// 获取主机
		String sub_url = url.substring(url.indexOf("http://") + 7);
		int hostEnd = sub_url.indexOf("/");
		String host;
		if (hostEnd > 0)
			host = sub_url.substring(0, hostEnd);
		else
			host = sub_url.substring(0);

		return postWithParams(url, host, 80, nameValuePair_arr);
	}

	// post 请求 , 设置主机,端口
	public static String postWithParams(String url, String host, Integer port, NameValuePair... nameValuePair_arr) throws HttpException, IOException {

		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		post.setRequestBody(nameValuePair_arr);

		client.getHostConfiguration().setHost(host, port, "http");
		client.executeMethod(post);
		StatusLine statusLine = post.getStatusLine();// 服务器返回的状态

		// String response = new
		// String(post.getResponseBodyAsString().getBytes("UTF-8"));
		InputStream inputStream = post.getResponseBodyAsStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

		String temp;
		StringBuffer html = new StringBuffer(100);
		while ((temp = bufferedReader.readLine()) != null) {
			html.append(temp);
		}
		inputStream.close();
		post.releaseConnection();

		return html.toString();
	}

	// 获取cookies, 每使用一次与目标网站建立一次连接, 创建一个新的SESSIONID, 慎用
	public static String cookiesByGet(String url) {
		// 准备返回的 cookie
		String cookie = "";

		HttpClient httpClient = new HttpClient();

		// 模拟登陆，按实际服务器端要求选用 post 或 get 请求方式
		GetMethod postMethod = new GetMethod(url);

		try {
			// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
			httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			int statusCode = httpClient.executeMethod(postMethod);
			// 获得登陆后的 Cookie
			Cookie[] cookies = httpClient.getState().getCookies();
			for (Cookie c : cookies) {
				cookie += c.toString() + ";";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cookie;
	}

	// 获取html页面
	public static String postWithCookie(String url, String cookie) {

		String html = null;
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestHeader("cookie", cookie);
		postMethod.setRequestHeader("Referer", url);
		postMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");

		try {
			httpClient.executeMethod(postMethod);
			html = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return html;
	}

	// 获取文件流, 保存文件
	public static void fileWithCookie(String url, String cookie) throws Exception {

		InputStream in = streamByGet(url, cookie);
		FileOutputStream out = null;
		try {
			// BufferedImage bi = ImageIO.read(in);
			File safeCodeImg = new File("C:\\Users\\Administrator\\Desktop\\pic\\safeCodeImg.png");
			if (!safeCodeImg.exists())
				safeCodeImg.createNewFile();
			/*
			 * ImageIO.write(bi, "png", safeCodeImg); ImageIO.
			 */

			out = new FileOutputStream(safeCodeImg);
			byte[] buffer = new byte[1024];
			int readLength = 0;
			while ((readLength = in.read(buffer)) > 0) {
				byte[] bytes = new byte[readLength];
				System.arraycopy(buffer, 0, bytes, 0, readLength);
				out.write(bytes);
			}

			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (null != out)
				out.close();
			if (null != in)
				in.close();
		}

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 获取文件流
	private static InputStream streamByGet(String url, String cookie) throws IOException {

		URL serverUrl = new URL(url);
		HttpURLConnection conn = null;
		InputStream result = null;
		try {
			conn = (HttpURLConnection) serverUrl.openConnection();
			conn.setRequestMethod("GET");// "POST" ,"GET"
			conn.addRequestProperty("Cookie", cookie);
			conn.addRequestProperty("Accept-Charset", "UTF-8;");// GB2312,
			conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");

			// 响应头查看
			// if (((HttpURLConnection) conn).getResponseCode() == 200) {
			// // 获取响应头字段
			// Map<String, List<String>> map = conn.getHeaderFields();
			// // 遍历响应头字段
			// for (String key : map.keySet()) {
			// System.out.println(key + "--->" + map.get(key));
			// }
			// // 返回的cookie
			// String cookie2 = null;
			// if (conn.getHeaderFields().get("Set-Cookie") != null) {
			// for (String s : conn.getHeaderFields().get("Set-Cookie")) {
			// cookie2 += s;
			// }
			// }
			// System.out.println(cookie2);
			// }

			InputStream inputStream = conn.getInputStream();
			result = streamCopy(inputStream);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != conn)
				conn.disconnect();
		}

		return result;
	}

	// 获取网络返回流
	public static InputStream streamByPost(String urlStr, String cookie) throws IOException {

		URL url = new URL(urlStr);
		InputStream in = null;
		URLConnection urlConnection = null;
		try {
			urlConnection = url.openConnection();
		} catch (IOException e) {
			// TODO 眼睛
			e.printStackTrace();
		}
		HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

		// true -- will setting parameters
		httpURLConnection.setDoOutput(true);
		// true--will allow read in from
		httpURLConnection.setDoInput(true);
		// will not use caches
		httpURLConnection.setUseCaches(false);
		// setting serialized
		httpURLConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");
		// default is GET
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("connection", "Keep-Alive");
		httpURLConnection.setRequestProperty("Charsert", "UTF-8");
		// 1 min
		httpURLConnection.setConnectTimeout(60000);
		// 1 min
		httpURLConnection.setReadTimeout(60000);

		httpURLConnection.addRequestProperty("Cookie", cookie);

		// connect to server (tcp)
		httpURLConnection.connect();

		in = httpURLConnection.getInputStream();

		return in;
	}

	// 流复制对象
	private static InputStream streamCopy(InputStream inStream) throws IOException {

		byte[] btArr = streamToByte(inStream);
		return new ByteArrayInputStream(btArr);
	}

	// 流读取为 byte 数组
	private static byte[] streamToByte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}

}
