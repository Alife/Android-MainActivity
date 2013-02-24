package com.and.netease;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.and.netease.layout.SlideImageLayout;
import com.and.netease.parser.NewsXmlParser;
import com.and.netease.rss.RSSHandler;
import com.pentasoft.db.model.Article;
import com.pentasoft.db.service.ArticleServices;

public class TabNewsTopActivity extends BaseActivity {

	MyListView listView;

	List<Article> list;
	List<Article> listimage;
	RSSHandler rssHandler;

	MyAdapter adapter;

	ViewSwitcher viewSwitcher;
	ImageView testView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_news_top);
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		initViews();

		// load local data
		// adapter = new MyAdapter();
		// listView.setAdapter(adapter);
		List<Article> locaList = db.findAll(Article.class);
		Notification(String.valueOf(locaList.size()));

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
		viewSwitcher.addView(getLayoutInflater().inflate(R.layout.layout_progress_page, null));
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

					list = new ArticleServices(db).getRemoteArticle(CONST.Article_News_ColumnId);
					listimage = new ArticleServices(db).getRemoteArticle(CONST.Article_News_ColumnId, true);

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

				// 加载顶部图片
				String ImageUrl = listimage.get(0).getImageUrl();
				Log.v("ImageUrl", ImageUrl);
				URL picUrl;
				try {
					picUrl = new URL(ImageUrl);
					Bitmap pngBM;
					pngBM = BitmapFactory.decodeStream(picUrl.openStream());
					testView.setImageBitmap(pngBM);
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
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			if (position == 1) {
				return;
			}
			Intent intent = new Intent(TabNewsTopActivity.this, NewsContentActivity.class);
			// intent.putExtra("content_url", list.get(position - 2).getLink());
			// position 此处减 2 是因为前面动态添加了一个图片
			// 猜测 position 应该是该 Item 在全局 layout 中的位置
			intent.putExtra("content_url", list.get(position - 2).getLink());
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
				convertView = getLayoutInflater().inflate(R.layout.layout_news_top_item, null);
				holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date_news_top_item);
				holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title_news_top_item);
				holder.tv_Description = (TextView) convertView.findViewById(R.id.tv_description_news_top_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String dateString = "";
			if (list.get(position).getReleaseDate() != null)
				dateString = list.get(position).getReleaseDate().toLocaleString();
			holder.tv_date.setText(dateString);
			holder.tv_title.setText(list.get(position).getTitle());
			// TextView使用Html来处理图片显示
			// Spanned text = Html.fromHtml(list.get(position).getDescription(),
			// imgGetter, null);
			holder.tv_Description
					.setText(Html.fromHtml(list.get(position).getSummary()), TextView.BufferType.SPANNABLE);

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

	// 滑动图片的集合
	private ArrayList<View> mImagePageViewList = null;
	private ViewGroup mMainView = null;
	private ViewPager mViewPager = null;
	// 当前ViewPager索引
	// private int pageIndex = 0;

	// 包含圆点图片的View
	private ViewGroup mImageCircleView = null;
	private ImageView[] mImageCircleViews = null;

	// 滑动标题
	private TextView mSlideTitle = null;

	// 布局设置类
	private SlideImageLayout mSlideLayout = null;
	// 数据解析类
	private NewsXmlParser mParser = null;

	/**
	 * 初始化
	 */
	private void initeViews() {
		// 滑动图片区域
		mImagePageViewList = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		mMainView = (ViewGroup) inflater.inflate(R.layout.page_topic_news, null);
		mViewPager = (ViewPager) mMainView.findViewById(R.id.image_slide_page);

		// 圆点图片区域
		mParser = new NewsXmlParser();
		int length = mParser.getSlideImages().length;
		mImageCircleViews = new ImageView[length];
		mImageCircleView = (ViewGroup) mMainView.findViewById(R.id.layout_circle_images);
		mSlideLayout = new SlideImageLayout(TabNewsTopActivity.this);
		mSlideLayout.setCircleImageLayout(length);

		for (int i = 0; i < length; i++) {
			mImagePageViewList.add(mSlideLayout.getSlideImageLayout(mParser.getSlideImages()[i]));
			mImageCircleViews[i] = mSlideLayout.getCircleImageLayout(i);
			mImageCircleView.addView(mSlideLayout.getLinearLayout(mImageCircleViews[i], 10, 10));
		}

		// 设置默认的滑动标题
		mSlideTitle = (TextView) mMainView.findViewById(R.id.tvSlideTitle);
		mSlideTitle.setText(mParser.getSlideTitles()[0]);

		setContentView(mMainView);

		// 设置ViewPager
		mViewPager.setAdapter(new SlideImageAdapter());
		mViewPager.setOnPageChangeListener(new ImagePageChangeListener());
	}

	// 滑动图片数据适配器
	private class SlideImageAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return mImagePageViewList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View view, int arg1, Object arg2) {
			((ViewPager) view).removeView(mImagePageViewList.get(arg1));
		}

		@Override
		public Object instantiateItem(View view, int position) {
			((ViewPager) view).addView(mImagePageViewList.get(position));

			return mImagePageViewList.get(position);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}
	}

	// 滑动页面更改事件监听器
	private class ImagePageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int index) {
			// pageIndex = index;
			mSlideLayout.setPageIndex(index);
			mSlideTitle.setText(mParser.getSlideTitles()[index]);

			for (int i = 0; i < mImageCircleViews.length; i++) {
				mImageCircleViews[index].setBackgroundResource(R.drawable.dot_selected);

				if (index != i) {
					mImageCircleViews[i].setBackgroundResource(R.drawable.dot_none);
				}
			}
		}
	}
}
