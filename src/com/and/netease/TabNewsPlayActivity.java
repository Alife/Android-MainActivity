package com.and.netease;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import cn.buaa.myweixin.R;
import cn.buaa.myweixin.base.BaseActivity;

import com.and.netease.rss.RSSHandler;
import com.pentasoft.db.model.Article;
import com.pentasoft.db.service.ArticleServices;

public class TabNewsPlayActivity extends BaseActivity {

	ListView listView;

	List<Article> list;
	RSSHandler rssHandler;

	ArticleAdapter adapter;

	ViewSwitcher viewSwitcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_news_play);
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);

		initViews();

		rssHandler = new RSSHandler();
		requestRSSFeed();

	}

	private void initViews() {
		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher_news_play);
		listView = new ListView(this);
		listView.setCacheColorHint(Color.argb(0, 0, 0, 0));
		viewSwitcher.addView(listView);
		viewSwitcher.addView(getLayoutInflater().inflate(R.layout.layout_progress_page, null));
		viewSwitcher.showNext();
		listView.setOnItemClickListener(listener);

	}

	private void requestRSSFeed() {
		Thread t = new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// URL url = new URL(CONST.URL_NEWS_TOP);
					// URLConnection con = url.openConnection();
					// con.connect();
					//
					// InputStream input = con.getInputStream();
					//
					// SAXParserFactory fac = SAXParserFactory.newInstance();
					// SAXParser parser = fac.newSAXParser();
					// XMLReader reader = parser.getXMLReader();
					// reader.setContentHandler(rssHandler);
					// // Reader r = new InputStreamReader(input,
					// Charset.forName("GBK"));
					// Reader r = new InputStreamReader(input);
					// reader.parse(new InputSource(r));
					// list = rssHandler.getData();
					// for (RSSItem rss : list) {
					// System.out.println(rss);
					// }

					list = new ArticleServices(db).getRemoteArticle(CONST.Article_Media_ColumnId);

					if (list.size() == 0) {
						handler.sendEmptyMessage(-1);
					} else {
						handler.sendEmptyMessage(1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				adapter = new ArticleAdapter(TabNewsPlayActivity.this, list);
				listView.setOnItemClickListener(listener);
				listView.setAdapter(adapter);
				viewSwitcher.showPrevious();
			}
		};
	};

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(TabNewsPlayActivity.this, NewsContentActivity.class);
			intent.putExtra("content_url", list.get(position).getLink());
			intent.putExtra("content_title", list.get(position).getTitle());
			TabNewsPlayActivity.this.startActivityForResult(intent, position);
		}
	};

	public static class ViewHolder {
		TextView tv_date;
		TextView tv_title;
		TextView tv_Description;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("返回");
		// if (resultCode == RESULT_OK) {
		// // 确认中
		// View v = (View) listView.getItemAtPosition(requestCode);
		// TextView tv = (TextView)
		// v.findViewById(R.id.tv_title_news_top_item);
		// tv.setText("hello");
		// adapter.notifyDataSetChanged();
		// }
	}
}
