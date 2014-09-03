package com.taobao.guangjie.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 搜索引擎访问类
 */
public class SearchEngineInterface {

	private static final String queryUrlBase = "http://10.97.252.37:12345/config=cluster:daogou,hit:500,format:json&&query=";

	private static String queryUrl(String urlTarget) {
		URL url;
		HttpURLConnection conn = null;
		try {
			url = new URL(urlTarget);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			InputStream is = conn.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (conn != null)
				conn.disconnect();
		}
	}

	public static String query(String q) {
		q += " OR tag:\"" + q + "\"";
		try {
			q = URLEncoder.encode(q, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return queryUrl(queryUrlBase + q);
	}

}
