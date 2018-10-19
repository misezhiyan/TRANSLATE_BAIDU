package util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpUtil {

	// 获取cookies, 每使用一次与目标网站建立�??次连�??, 创建�??个新的SESSIONID, 慎用
	public static String getCookiesByGet(String url) {
		// 准备返回的参�??
		String cookie = "";

		HttpClient httpClient = new HttpClient();

		// 模拟登陆，按实际服务器端要求选用 Post �?? Get 请求方式
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
	public static String htmlGetWithCookie(String url, String cookie) {

		String html = null;
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);

		try {
			httpClient.executeMethod(getMethod);
			html = getMethod.getResponseBodyAsString();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			// TODO 眼睛
		} catch (IOException e) {
			e.printStackTrace();
			// TODO 眼睛
		}

		return html;
	}

	public static void main(String[] args) throws HttpException, IOException {

		String url = "http://www.cnblogs.com/jiajinyi/archive/2013/05/24/3097088.html";

		String result = postWithParams(url);
		System.out.println(result);
	}

	public static String postWithParams(String url) throws HttpException, IOException {

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

	public static String postWithParams(String url, NameValuePair... nameValuePair_arr)
			throws HttpException, IOException {

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

	public static String postWithParams(String url, String host, Integer port, NameValuePair... nameValuePair_arr)
			throws HttpException, IOException {

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

}
