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
import com.and.netease.CONST;
import com.pentasoft.db.model.Article;
import com.wagnerandade.coollection.query.order.Order;

public class ArticleServices {

	private FinalDb finalDb;

	public ArticleServices(FinalDb _finalDb) {
		this.finalDb = _finalDb;
	}

	public List<Article> getRemoteArticle(int columnid) {
		return getRemoteArticle(columnid, false);
	}

	public List<Article> getRemoteArticle(int columnid, boolean isImage) {
		List<Article> list = finalDb.findAllByWhere(Article.class, "", "");
		Article lastArticle = from(list).orderBy("getArticleId", Order.DESC).first();
		int maxId = 0;
		if (lastArticle != null) {
			maxId = lastArticle.getArticleId();
		}
		String url = CONST.Url_Article_List;
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("id", String.valueOf(maxId)));
		nameValuePair.add(new BasicNameValuePair("columnid", String.valueOf(columnid)));
		nameValuePair.add(new BasicNameValuePair("isImage", String.valueOf(isImage)));

		String string = HttpGet(url, nameValuePair);

		List<Article> listWebArticles = parseArticleList(string);

		// 将数据保存到本地数据库中
		for (Article article : listWebArticles) {
			finalDb.save(article);
		}

		return listWebArticles;
	}

	public List<Article> getLocalArticleList() {
		List<Article> list = finalDb.findAllByWhere(Article.class, "", "");
		return list;
	}

	private List<Article> parseArticleList(String json) {
		// Log.v("json:", json);

		List<Article> list = new ArrayList<Article>();
		if (json != null && !"".equals(json)) {
			list = JSON.parseArray(json, Article.class);

			for (Article rss : list) {
				System.out.println("ArticleId:" + rss.getArticleId());
				System.out.println("Title:" + rss.getTitle());
				// System.out.println("Content:" + rss.getContent());
				// System.out.println("Summary:" + rss.getSummary());
				System.out.println("DateCreated:" + rss.getReleaseDate());
				System.out.println("ColumnId:" + rss.getColumnId());
				System.out.println("ImageUrl:" + rss.getImageUrl());
			}
		}

		return list;
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

	private String HttpGet(String url, List<NameValuePair> paras) {
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

	private String HttpPost(String url, List<NameValuePair> paras) {
		String result = "";

		StringBuilder builder = new StringBuilder();

		DefaultHttpClient httpClient = new DefaultHttpClient();// http�ͻ���
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
			// �õ�Ӧ����ַ���Ҳ��һ�� JSON ��ʽ��������
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
