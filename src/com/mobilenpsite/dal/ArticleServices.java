package com.mobilenpsite.dal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalDb;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.mobilenpsite.configs.Config;
import com.mobilenpsite.db.model.Article;
import com.mobilenpsite.utility.NetHelper;

public class ArticleServices {

	// 网络操作
	public List<Article> GetRemoteList(int columnid) {
		Integer maxId = 0;
		String url = Config.Url_Article_List;
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("id", String.valueOf(maxId)));
		nameValuePair.add(new BasicNameValuePair("columnid", String
				.valueOf(columnid)));
		String string = NetHelper.GetContentFromUrl(url, nameValuePair);
		List<Article> list = parseArticleList(string);
		return list;
	}

	public List<Article> GetRemoteList(int columnid, Object isImage,
			Integer page) {
		Integer maxId = 0;
		String url = Config.Url_Article_List;
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("id", String.valueOf(maxId)));
		nameValuePair.add(new BasicNameValuePair("columnid", String
				.valueOf(columnid)));
		nameValuePair.add(new BasicNameValuePair("isImage", String
				.valueOf(isImage)));
		nameValuePair.add(new BasicNameValuePair("page", String.valueOf(page)));
		String string = NetHelper.GetContentFromUrl(url, nameValuePair);
		List<Article> list = parseArticleList(string);
		return list;
	}

	private List<Article> parseArticleList(String json) {
		// Log.v("json:", json);

		List<Article> list = new ArrayList<Article>();
		if (json != null && !"".equals(json)) {
			list = JSON.parseArray(json, Article.class);

			// for (Article rss : list) {
			// System.out.println("ArticleId:" + rss.getArticleId());
			// System.out.println("Title:" + rss.getTitle());
			// // System.out.println("Content:" + rss.getContent());
			// // System.out.println("Summary:" + rss.getSummary());
			// System.out.println("DateCreated:" + rss.getReleaseDate());
			// System.out.println("ColumnId:" + rss.getColumnId());
			// System.out.println("ImageUrl:" + rss.getImageUrl());
			// }
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

	// 本地数据库操作
	/** The final db. */
	private FinalDb finalDb;

	/**
	 * 实例化 ArticleDalServices.
	 * 
	 * @param _finalDb
	 *            sqllite 入口,需要从 Activity 传递过来.<br />
	 *            (因为 finalDb 初始化需要 Activity 中的 Content)
	 */
	public ArticleServices(FinalDb _finalDb) {
		this.finalDb = _finalDb;
	}

	/**
	 * 获取所有文章
	 * 
	 * @return 所有文章
	 */
	public List<Article> GetLocalList() {
		List<Article> list = finalDb.findAll(Article.class);
		return list;
	}

	/**
	 * 条件查询
	 * 
	 * @param a
	 *            (匹配方法类似于 Ibatis)
	 * @return the list
	 */
	public List<Article> GetLocalList(Article a) {
		StringBuilder sqlwhereSB = new StringBuilder("1=1");
		sqlwhereSB.append(getSqlWhere(a));
		List<Article> list = finalDb.findAllByWhere(Article.class,
				sqlwhereSB.toString());
		return list;
	}

	public List<Article> getLocalList(int columnId, int pageindex,
			Integer pagesize) {
		Article article = new Article();
		article.setColumnId(columnId);
		return GetLocalList(article, pageindex, pagesize);
	}

	/**
	 * 分页条件查询
	 * 
	 * @param a
	 *            条件实例
	 * @param pageIndex
	 *            the page index
	 * @param pageSize
	 *            the page size
	 * @return the local article list page
	 */
	public List<Article> GetLocalList(Article a, Integer pageIndex,
			Integer pageSize) {
		StringBuilder sqlwhereSB = new StringBuilder();
		sqlwhereSB.append(getSqlWhere(a));
		// 图片文章排在前面
		sqlwhereSB.append(" ORDER BY IsImage,ArticleId DESC ");
		sqlwhereSB.append(" Limit " + String.valueOf(pageSize) + " Offset "
				+ String.valueOf(pageIndex * pageSize));
		List<Article> list = finalDb.findAllByWhere(Article.class,
				sqlwhereSB.toString());
		return list;
	}

	/**
	 * 判断是否已经存在
	 * 
	 * @param blogId
	 * @return
	 */
	private boolean Exist(int articleId) {
		String where = "ArticleId=" + articleId;
		List<Article> list = finalDb.findAllByWhere(Article.class, where);
		boolean isExist = list != null && list.size() > 0;
		return isExist;
	}

	/**
	 * 插入
	 * 
	 * @param list
	 */
	public void SynchronyData2DB(List<Article> list) {
		for (int i = 0, len = list.size(); i < len; i++) {
			int newsId = list.get(i).getArticleId();
			boolean isExist = Exist(newsId);
			if (!isExist) {
				finalDb.update(list.get(i));
			} else {
				finalDb.save(list.get(i));
			}
		}

	}

	/**
	 * 根据实例拼接 Where
	 * 
	 * @param a
	 *            实例
	 * @return the sql where
	 */
	private String getSqlWhere(Article a) {
		StringBuilder sqlwhereSB = new StringBuilder("1=1");
		if (a.getColumnId() > 0)
			sqlwhereSB.append(" and columnid=" + a.getColumnId());
		if (a.getIsImage() != null) {
			if ((Boolean) a.getIsImage())
				sqlwhereSB.append(" and ImageUrl is not null");
			else
				sqlwhereSB.append(" and ImageUrl is null");
		}
		return sqlwhereSB.toString();
	}

}
