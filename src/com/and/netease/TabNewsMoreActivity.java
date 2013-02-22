package com.and.netease;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.buaa.myweixin.R;

public class TabNewsMoreActivity extends Activity {

	ListView listView;
	String array[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_news_more);

		array = getResources().getStringArray(R.array.more);
		listView = (ListView) findViewById(R.id.listView_news_more);
		listView.setAdapter(new MyAdapter());

		listView.setOnItemClickListener(listener);
	}

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(TabNewsMoreActivity.this, NewsMoreContentActivity.class);
			Bundle bundle = new Bundle();
			switch (position) {
			case 0:
				bundle.putString("more_news_list_url", CONST.URL_NEWS_DOMESTIC);
				break;
			case 1:
				bundle.putString("more_news_list_url", CONST.URL_NEWS_MILITARY);
				break;
			case 2:
				bundle.putString("more_news_list_url", CONST.URL_NEWS_INTERNATIONAL);
				break;
			case 3:
				bundle.putString("more_news_list_url", CONST.URL_NEWS_COMMUNITY);
				break;
			case 4:
				bundle.putString("more_news_list_url", CONST.URL_NEWS_DEPTH);
				break;
			case 5:
				bundle.putString("more_news_list_url", CONST.URL_NEWS_TICKET);
				break;
			case 6:
				bundle.putString("more_news_list_url", CONST.URL_NEWS_FILM);
				break;
			case 7:
				bundle.putString("more_news_list_url", CONST.URL_NEWS_MUSIC);
				break;
			case 8:
				bundle.putString("more_news_list_url", CONST.URL_NEWS_IT);
				break;
			case 9:
				bundle.putString("more_news_list_url", CONST.URL_NEWS_CAR);
				break;
			case 10:
				bundle.putString("more_news_list_url", CONST.URL_NEWS_DIGITAL);
				break;
			}

			bundle.putString("text", array[position]);
			intent.putExtras(bundle);
			TabNewsMoreActivity.this.startActivity(intent);

		}
	};

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return array.length;
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
				convertView = getLayoutInflater().inflate(R.layout.layout_news_more_item, null);
				holder = new ViewHolder();
				holder.tv = (TextView) convertView.findViewById(R.id.tv_news_more_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv.setText(array[position]);
			return convertView;
		}

	}

	public static class ViewHolder {
		TextView tv;
	}
}
