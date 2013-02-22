package com.and.netease;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.buaa.myweixin.R;

import com.and.netease.utils.MoveBg;

public class TabNewsActivity extends ActivityGroup {
	RelativeLayout layout_title_bar;
	RelativeLayout layout_news_main;
	LayoutInflater layoutInflater;
	Intent intent;
	View page;// 用来存放顶部具体分类的view

	TextView tv_front;// 需要移动的View

	TextView tv_bar_news;
	TextView tv_bar_sport;
	TextView tv_bar_play;
	TextView tv_bar_finance;
	TextView tv_bar_science;
	TextView tv_bar_more;

	int avg_width = 0;// 用于记录平均每个标签的宽度，移动的时候需要

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_news);

		initViews();
	}

	private void initViews() {
		layout_title_bar = (RelativeLayout) findViewById(R.id.layout_title_bar);
		layout_news_main = (RelativeLayout) findViewById(R.id.layout_news_main);
		layoutInflater = getLayoutInflater();

		tv_bar_news = (TextView) findViewById(R.id.tv_title_bar_news);
		tv_bar_sport = (TextView) findViewById(R.id.tv_title_bar_sport);
		tv_bar_play = (TextView) findViewById(R.id.tv_title_bar_play);
		tv_bar_finance = (TextView) findViewById(R.id.tv_title_bar_finance);
		tv_bar_science = (TextView) findViewById(R.id.tv_title_bar_science);
		tv_bar_more = (TextView) findViewById(R.id.tv_title_bar_more);

		tv_bar_news.setOnClickListener(onClickListener);
		tv_bar_sport.setOnClickListener(onClickListener);
		tv_bar_play.setOnClickListener(onClickListener);
		tv_bar_finance.setOnClickListener(onClickListener);
		tv_bar_science.setOnClickListener(onClickListener);
		tv_bar_more.setOnClickListener(onClickListener);

		tv_front = new TextView(this);
		tv_front.setBackgroundResource(R.drawable.slidebar);
		tv_front.setTextColor(Color.WHITE);
		tv_front.setText(R.string.title_news_category_tops);
		tv_front.setGravity(Gravity.CENTER);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		param.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		layout_title_bar.addView(tv_front, param);

		// 默认显示“头条”
		intent = new Intent(TabNewsActivity.this, TabNewsTopActivity.class);
		page = getLocalActivityManager().startActivity("activity1", intent).getDecorView();
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout_news_main.addView(page, params);

	}

	private OnClickListener onClickListener = new OnClickListener() {
		int startX;// 移动的起始位置

		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		@Override
		public void onClick(View v) {
			avg_width = findViewById(R.id.layout).getWidth();
			switch (v.getId()) {
			case R.id.tv_title_bar_news:
				MoveBg.moveFrontBg(tv_front, startX, 0, 0, 0);
				startX = 0;
				tv_front.setText(R.string.title_news_category_tops);
				// 准备显示“头条”
				page = layoutInflater.inflate(R.layout.layout_news_top, null);
				intent.setClass(TabNewsActivity.this, TabNewsTopActivity.class);
				page = getLocalActivityManager().startActivity("activity1", intent).getDecorView();
				break;
			case R.id.tv_title_bar_sport:
				MoveBg.moveFrontBg(tv_front, startX, avg_width, 0, 0);
				startX = avg_width;
				tv_front.setText(R.string.title_news_category_sport);
				// 准备显示“体育”
				intent.setClass(TabNewsActivity.this, TabNewsSportActivity.class);
				page = getLocalActivityManager().startActivity("activity2", intent).getDecorView();
				break;
			case R.id.tv_title_bar_play:
				MoveBg.moveFrontBg(tv_front, startX, avg_width * 2, 0, 0);
				startX = avg_width * 2;
				tv_front.setText(R.string.title_news_category_play);
				// 准备显示“娱乐”
				intent.setClass(TabNewsActivity.this, TabNewsPlayActivity.class);
				page = getLocalActivityManager().startActivity("activity3", intent).getDecorView();
				break;
			case R.id.tv_title_bar_finance:
				MoveBg.moveFrontBg(tv_front, startX, avg_width * 3, 0, 0);
				startX = avg_width * 3;
				tv_front.setText(R.string.title_news_category_finance);
				// 准备显示“财经”
				intent.setClass(TabNewsActivity.this, TabNewsFinanceActivity.class);
				page = getLocalActivityManager().startActivity("activity4", intent).getDecorView();
				break;
			case R.id.tv_title_bar_science:
				MoveBg.moveFrontBg(tv_front, startX, avg_width * 4, 0, 0);
				startX = avg_width * 4;
				tv_front.setText(R.string.title_news_category_science);
				// 准备显示“科技”
				intent.setClass(TabNewsActivity.this, TabNewsScienceActivity.class);
				page = getLocalActivityManager().startActivity("activity5", intent).getDecorView();
				break;
			case R.id.tv_title_bar_more:
				MoveBg.moveFrontBg(tv_front, startX, avg_width * 5, 0, 0);
				startX = avg_width * 5;
				tv_front.setText(R.string.title_news_category_more);
				// 准备显示“更多”
				intent.setClass(TabNewsActivity.this, TabNewsMoreActivity.class);
				page = getLocalActivityManager().startActivity("activity6", intent).getDecorView();
				break;

			default:
				break;
			}
			// 切换
			layout_news_main.removeAllViews();
			layout_news_main.addView(page, params);

		}
	};

}
