package com.pentasoft.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * SortUtil.java verson 1.0 Aug 5, 2011
 * 
 * @author 贾世雄
 * 
 */
public class HttpUtil {

	public static String HttpGet(String url, List<NameValuePair> paras) {
		String result = "";

		HttpClient client = new DefaultHttpClient();
		StringBuilder builder = new StringBuilder();

		if (paras != null && paras.size() > 0) {
			if (url.indexOf("?") == -1)
				url += "?";
			for (NameValuePair nameValuePair : paras) {
				String valueString = nameValuePair.getValue();
				if (valueString != null && !"".equals(valueString.trim()))
					url += nameValuePair.getName() + "=" + valueString + "&";
			}
		}

		Log.v("url response:", url);

		try {
			HttpGet myget = new HttpGet(url);
			HttpResponse response = client.execute(myget);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			result = builder.toString();
			// Log.v("url response:", "true. result:" + result);
			// Log.v("url response:", result);
		} catch (ConnectTimeoutException e) {
			Log.v("ConnectTimeoutException", "false. message:time out");
			e.printStackTrace();
		} catch (Exception e) {
			Log.v("Exception", "false. message:" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public static String HttpPost(String url, List<NameValuePair> paras) {
		String result = "";

		StringBuilder builder = new StringBuilder();

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if (paras != null && paras.size() > 0) {
			for (NameValuePair nameValuePair : paras) {
				list.add(new BasicNameValuePair(nameValuePair.getName(), nameValuePair.getValue()));
				result += nameValuePair.getName() + "=" + nameValuePair.getValue() + "&";
			}
		}

		Log.v("url response:", url + result);

		try {
			httppost.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(httppost);
			builder.append(EntityUtils.toString(response.getEntity()));
			result = builder.toString();
			Log.v("url response:", "true. result:" + result);
		} catch (Exception e) {
			Log.v("url response:", "false. message:" + e.getMessage());
			e.printStackTrace();
		}
		return builder.toString();
	}
}
