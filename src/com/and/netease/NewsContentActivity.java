package com.and.netease;

import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import cn.buaa.myweixin.R;

public class NewsContentActivity extends Activity {

	ImageButton btn_back;
	WebView webView;
	String content_url;

	ViewSwitcher viewSwitcher;
	@ViewInject(id = R.id.news_content_title)
	TextView news_content_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_newscontent);
		initViews();
	}

	private void initViews() {
		btn_back = (ImageButton) findViewById(R.id.btn_newscontent_back);
		btn_back.setOnClickListener(listener);

		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);

		content_url = getIntent().getStringExtra("content_url");
		String title = getIntent().getStringExtra("content_title");
		Log.d("content_title", String.valueOf(news_content_title == null));
		Log.d("content_title", title);
		Log.d("content_url", content_url);
		news_content_title = (TextView) findViewById(R.id.news_content_title);
		Log.d("content_title", String.valueOf(news_content_title == null));
		news_content_title.setText(title);
		webView = new WebView(this);
		//
		// // 向ViewSwitcher中添加两个View，用来切换
		viewSwitcher.addView(webView);
		// viewSwitcher.addView(getLayoutInflater().inflate(R.layout.layout_progress_page,
		// null));
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		// 优先缓存 http://androiddada.iteye.com/blog/1280946
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

		// WebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		// 默认不使用缓存！
		// webView.setWebViewClient(client);
		webView.loadUrl(content_url);
	}

	private WebViewClient client = new WebViewClient() {

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			viewSwitcher.showPrevious();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			viewSwitcher.showNext();
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}
	};

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			NewsContentActivity.this.setResult(RESULT_OK);
			NewsContentActivity.this.finish();
		}
	};

	public void onBackPressed() {
		if (webView.canGoBack()) {
			webView.goBack();
		} else {
			NewsContentActivity.this.setResult(RESULT_OK);
			NewsContentActivity.this.finish();
		}
	};
}
