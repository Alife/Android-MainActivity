package com.and.netease;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import cn.buaa.myweixin.R;

import com.and.netease.rss.RSSHandler;
import com.and.netease.rss.RSSItem;

public class NewsMoreContentActivity extends Activity {

	ImageButton btn_back;
	TextView tv_title;
	ListView listView;
	String path;// url路径
	String text;// 顶部动态文字

	List<RSSItem> list;
	RSSHandler rssHandler;

	MyAdapter adapter;

	ViewSwitcher viewSwitcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_news_more_content);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			path = bundle.getString("more_news_list_url");
			text = bundle.getString("text");
		}
		initViews();

		rssHandler = new RSSHandler();
		requestRSSFeed();

	}

	private void initViews() {
		btn_back = (ImageButton) findViewById(R.id.btn_news_more_content_back);
		btn_back.setOnClickListener(onclick_listener);

		tv_title = (TextView) findViewById(R.id.news_content_title);
		tv_title.setText(text);

		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher_news_more_content);
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
					URL url = new URL(path);
					URLConnection con = url.openConnection();
					con.connect();

					InputStream input = con.getInputStream();

					SAXParserFactory fac = SAXParserFactory.newInstance();
					SAXParser parser = fac.newSAXParser();
					XMLReader reader = parser.getXMLReader();
					reader.setContentHandler(rssHandler);
					// Reader r = new InputStreamReader(input,
					// Charset.forName("GBK"));
					Reader r = new InputStreamReader(input);
					reader.parse(new InputSource(r));
					list = rssHandler.getData();
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
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(NewsMoreContentActivity.this, NewsContentActivity.class);
			intent.putExtra("content_url", list.get(position).getLink());
			intent.putExtra("content_title", list.get(position).getTitle());
			NewsMoreContentActivity.this.startActivityForResult(intent, position);
		}
	};

	private OnClickListener onclick_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
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

			holder.tv_date.setText(list.get(position).getPubDate());
			holder.tv_title.setText(list.get(position).getTitle());
			holder.tv_Description.setText(list.get(position).getDescription());

			return convertView;
		}

	}

	public static class ViewHolder {
		TextView tv_date;
		TextView tv_title;
		TextView tv_Description;
	}
}
