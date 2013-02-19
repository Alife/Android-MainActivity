package com.pentasoft.db.service;

import static com.wagnerandade.coollection.Coollection.from;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalDb;

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

import com.alibaba.fastjson.JSON;
import com.pentasoft.db.model.Article;
import com.wagnerandade.coollection.query.order.Order;

public class ArticleServices {

	private FinalDb finalDb;

	public ArticleServices(FinalDb _finalDb) {
		this.finalDb = _finalDb;
	}

	public List<Article> getRemoteArticle() {
		List<Article> list = finalDb.findAllByWhere(Article.class, "", "");
		Article lastArticle = from(list).orderBy("getArticleId", Order.DESC)
				.first();
		int maxId = 0;
		if (lastArticle != null) {
			maxId = lastArticle.getArticleId();
		}
		// String url =
		// "http://fuwaitest.91health.net/android/GetArticleList?id=" + maxId;
		String url = "http://10.0.2.2:8001/android/GetArticleList?id=" + maxId;
		String string = HttpGet(url);

		return parseArticleList(string);
	}

	private List<Article> parseArticleList(String json) {
		List<Article> result = null;

		Log.v("json:", json);
		System.out.println(json);

		List<Article> list = JSON.parseArray(json, Article.class);
		for (Article rss : list) {
			System.out.println("ArticleId:" + rss.getArticleId());
			System.out.println("Title:" + rss.getTitle());
			System.out.println("Content:" + rss.getContent());
			System.out.println("Summary:" + rss.getSummary());
			System.out.println("DateCreated:" + rss.getDateCreated());
			System.out.println("ColumnId:" + rss.getColumnId());
		}
		result = list;

		return result;
	}

	public ArrayList<HashMap<String, String>> getArticleArrayList() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 30; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ItemTitle", "This is Title.....");
			map.put("ItemText", "This is text.....");
			list.add(map);
		}
		return list;
	}

	private String HttpGet(String string) {
		return HttpGet(string, null);
	}

	private String HttpGet(String url, List<NameValuePair> paras) {
		String result = "";

		HttpClient client = new DefaultHttpClient();
		StringBuilder builder = new StringBuilder();

		if (paras != null && paras.size() > 0) {
			if (url.indexOf("?") == -1)
				url += "?";
			for (NameValuePair nameValuePair : paras) {
				result += nameValuePair.getName() + "="
						+ nameValuePair.getValue() + "&";
			}
		}

		Log.v("url response:", url + result);

		try {
			HttpGet myget = new HttpGet(url + result);
			HttpResponse response = client.execute(myget);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			result = builder.toString();
			// Log.v("url response:", "true. result:" + result);
			Log.v("url response:", "true. result:");
		} catch (ConnectTimeoutException e) {
			Log.v("url response:", "false. message:time out");
			e.printStackTrace();
		} catch (Exception e) {
			Log.v("url response:", "false. message:" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	private String HttpPost(String url, List<NameValuePair> paras) {
		String result = "";

		StringBuilder builder = new StringBuilder();

		DefaultHttpClient httpClient = new DefaultHttpClient();// http客户端
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if (paras != null && paras.size() > 0) {
			for (NameValuePair nameValuePair : paras) {
				list.add(new BasicNameValuePair(nameValuePair.getName(),
						nameValuePair.getValue()));
				result += nameValuePair.getName() + "="
						+ nameValuePair.getValue() + "&";
			}
		}

		Log.v("url response:", url + result);

		try {
			httppost.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(httppost);
			// 得到应答的字符串，这也是一个 JSON 格式保存的数据
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
