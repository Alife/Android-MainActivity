package com.and.netease;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import cn.buaa.myweixin.R;
import cn.buaa.myweixin.base.BaseActivity;

import com.and.netease.rss.RSSHandler;
import com.mobilenpsite.configs.Config;
import com.mobilenpsite.dal.ArticleServices;
import com.mobilenpsite.db.model.Article;

public class TabNewsFinanceActivity extends BaseActivity {

	ListView listView;

	List<Article> list;
	RSSHandler rssHandler;

	MyAdapter adapter;

	ViewSwitcher viewSwitcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_news_finance);
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);

		initViews();

		rssHandler = new RSSHandler();
		requestRSSFeed();

	}

	private void initViews() {
		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher_news_finance);
		listView = new ListView(this);
		listView.setCacheColorHint(Color.argb(0, 0, 0, 0));
		viewSwitcher.addView(listView);
		viewSwitcher.addView(getLayoutInflater().inflate(
				R.layout.layout_progress_page, null));
		viewSwitcher.showNext();
		listView.setOnItemClickListener(listener);

	}

	private void requestRSSFeed() {
		Thread t = new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					list = new ArticleServices(db)
							.GetRemoteList(Config.Article_research_ColumnId);

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
				adapter = new MyAdapter();
				listView.setOnItemClickListener(listener);
				listView.setAdapter(adapter);
				viewSwitcher.showPrevious();
			}
		};
	};

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(TabNewsFinanceActivity.this,
					NewsContentActivity.class);
			intent.putExtra("content_url", list.get(position).getRealLink());
			intent.putExtra("content_title", list.get(position).getTitle());
			TabNewsFinanceActivity.this
					.startActivityForResult(intent, position);
		}
	};

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(
						R.layout.layout_news_top_item, null);
				holder.tv_date = (TextView) convertView
						.findViewById(R.id.tv_date_news_top_item);
				holder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title_news_top_item);
				holder.tv_Description = (TextView) convertView
						.findViewById(R.id.tv_description_news_top_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_date.setText(list.get(position).getReleaseDateString());
			holder.tv_title.setText(list.get(position).getTitle());
			holder.tv_Description.setText(list.get(position).getSummary());

			return convertView;
		}

	}

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
		// TextView tv = (TextView) v.findViewById(R.id.tv_title_news_top_item);
		// tv.setText("hello");
		// adapter.notifyDataSetChanged();
		// }
	}
}
