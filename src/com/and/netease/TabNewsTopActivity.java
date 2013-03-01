package com.and.netease;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import cn.buaa.myweixin.R;
import cn.buaa.myweixin.base.BaseActivity;

import com.and.netease.MyListView.OnRefreshListener;
import com.and.netease.rss.RSSHandler;
import com.mobilenpsite.configs.Config;
import com.mobilenpsite.dal.ArticleServices;
import com.mobilenpsite.db.model.Article;

public class TabNewsTopActivity extends BaseActivity {

	MyListView listView;

	List<Article> list;
	List<Article> listimage;
	RSSHandler rssHandler;

	MyAdapter adapter;

	ViewSwitcher viewSwitcher;
	ImageView testView;

	ArticleServices dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_news_top);
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		dbHelper = new ArticleServices(db);
		initViews();
		// 优先读取本地数据
		list = dbHelper.getLocalList(Config.Article_News_ColumnId, 0,
				Config.Article_PageSize);

		if (isNetworkAvailable) {// 有网络情况
			list = dbHelper
					.GetRemoteList(Config.Article_News_ColumnId, null, 0);
		}
		// get data from network asynchronous
		rssHandler = new RSSHandler();
		requestRSSFeed();

		// 初始化滑动图片
		// initeViews();

	}

	@Override
	protected void onResume() {
		super.onResume();
		// int count = listView.getAdapter().getCount();
		// Notification(String.valueOf("current item count of listview is " +
		// count));
	}

	private void initViews() {

		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher_news_top);
		listView = new MyListView(this);
		listView.setCacheColorHint(Color.argb(0, 0, 0, 0));
		listView.setonRefreshListener(refreshListener);
		listView.setOnItemClickListener(itemClickListener);

		// 加载顶部图片
		testView = new ImageView(this);
		// testView.setImageResource(R.drawable.temp);
		// listView.addHeaderView(testView);

		viewSwitcher.addView(listView);
		viewSwitcher.addView(getLayoutInflater().inflate(
				R.layout.layout_progress_page, null));
		viewSwitcher.showNext();

	}

	private OnRefreshListener refreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			new AsyncTask<Void, Void, Void>() {
				protected Void doInBackground(Void... params) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}

					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					adapter.notifyDataSetChanged();
					listView.onRefreshComplete();
				}

			}.execute(null);
		}
	};

	private void requestRSSFeed() {
		Thread t = new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					list = new ArticleServices(db).GetRemoteList(
							Config.Article_News_ColumnId, null, 0);
					listimage = new ArticleServices(db).GetRemoteList(
							Config.Article_News_ColumnId, true, 0);

					if (list.size() == 0 && listimage.size() == 0) {
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

				try {
					if (listimage.size() > 0) {
						// 加载顶部图片
						String ImageUrl = listimage.get(0).getRealImageUrl();
						Log.v("ImageUrl", ImageUrl);
						URL picUrl;
						picUrl = new URL(ImageUrl);
						Bitmap pngBM;
						pngBM = BitmapFactory.decodeStream(picUrl.openStream());
						testView.setImageBitmap(pngBM);
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listView.addHeaderView(testView);

				adapter = new MyAdapter();
				listView.setOnItemClickListener(itemClickListener);
				listView.setAdapter(adapter);
				viewSwitcher.showPrevious();

				listView.onRefreshComplete();
			}
		};
	};

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			if (position == 1) {
				return;
			}
			Intent intent = new Intent(TabNewsTopActivity.this,
					NewsContentActivity.class);
			// intent.putExtra("content_url", list.get(position - 2).getLink());
			// position 此处减 2 是因为前面动态添加了一个图片
			// 猜测 position 应该是该 Item 在全局 layout 中的位置
			intent.putExtra("content_url", list.get(position - 2).getRealLink());
			intent.putExtra("content_title", list.get(position - 2).getTitle());
			TabNewsTopActivity.this.startActivityForResult(intent, position);
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
			// TextView使用Html来处理图片显示
			// Spanned text = Html.fromHtml(list.get(position).getDescription(),
			// imgGetter, null);
			holder.tv_Description.setText(
					Html.fromHtml(list.get(position).getSummary()),
					TextView.BufferType.SPANNABLE);

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
		System.out.println("返回");
		super.onActivityResult(requestCode, resultCode, data);

	}

	// ImageGetter imgGetter = new Html.ImageGetter() {
	// @Override
	// public Drawable getDrawable(String source) {
	// Drawable drawable = null;
	// drawable = Drawable.createFromPath(source); // Or fetch it from the URL
	// // Important
	// drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
	// .getIntrinsicHeight());
	// return drawable;
	// }
	// };

}
