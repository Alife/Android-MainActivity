package com.and.netease;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.buaa.myweixin.R;

import com.and.netease.TabNewsFinanceActivity.ViewHolder;
import com.pentasoft.db.model.Article;

public class ArticleAdapter extends BaseAdapter {
	Context mContext;
	List<Article> articList;

	public ArticleAdapter(Context context, List<Article> list) {
		mContext = context;
		articList = list;
	}

	@Override
	public int getCount() {
		return articList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.layout_news_top_item, null);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date_news_top_item);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title_news_top_item);
			holder.tv_Description = (TextView) convertView.findViewById(R.id.tv_description_news_top_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_date.setText(articList.get(position).getReleaseDateString());
		holder.tv_title.setText(articList.get(position).getTitle());
		holder.tv_Description.setText(articList.get(position).getSummary());

		return convertView;

	}
}
