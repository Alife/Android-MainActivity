package com.and.netease;

import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import cn.buaa.myweixin.R;
import cn.buaa.myweixin.base.BaseListActivity;

import com.and.netease.rss.RSSHandler;
import com.miloisbadboy.view.PullToRefreshView;
import com.miloisbadboy.view.PullToRefreshView.OnFooterRefreshListener;
import com.miloisbadboy.view.PullToRefreshView.OnHeaderRefreshListener;
import com.pentasoft.db.model.Article;
import com.pentasoft.db.service.ArticleServices;

public class TabNewsPlayActivity extends BaseListActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {

	PullToRefreshView mPullToRefreshView;
	ListView listView;
	ArticleServices _ArticleServices = new ArticleServices(db);
	List<Article> list;
	RSSHandler rssHandler;

	Integer page = 0;

	ArticleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_news_play);
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		_ArticleServices = new ArticleServices(db);
		list = _ArticleServices.getLocalArticleListPage(
				CONST.Article_News_ColumnId, false, page);
		// 如果本地没有数据,则自动从网上获取
		if (!(list != null && list.size() > 0)) {
			list = _ArticleServices.getRemoteArticle(
					CONST.Article_News_ColumnId, false);
			Notification("没有本地数据,从网络更新 " + list.size() + "条");

		}

		setListAdapter(new ArticleAdapter(this, list));
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Toast.makeText(this, "positon = " + position, 1000).show();
	}

	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				List<Article> articles = _ArticleServices
						.getLocalArticleListPage(CONST.Article_News_ColumnId,
								false, page++);
				for (Article article : articles) {
					list.add(article);
				}
				Notification("更新 " + articles.size() + " 条数据");
				mPullToRefreshView.onFooterRefreshComplete();
			}
		}, 1000);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 设置更新时间
				// mPullToRefreshView.onHeaderRefreshComplete("最近更新:01-23 12:01");
				List<Article> articles = _ArticleServices.getRemoteArticle(
						CONST.Article_News_ColumnId, false);
				for (Article article : articles) {
					list.add(article);
				}
				Notification("更新 " + articles.size() + " 条数据");
				Date date = new Date();
				mPullToRefreshView.onHeaderRefreshComplete(date
						.toLocaleString());
				// mPullToRefreshView.onHeaderRefreshComplete();
			}
		}, 1000);

	}
}
