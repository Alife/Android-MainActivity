package cn.buaa.myweixin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.tsz.afinal.annotation.view.ViewInject;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.and.netease.CONST;
import com.and.netease.MyListView;
import com.and.netease.MyListView.OnRefreshListener;
import com.and.netease.NewsContentActivity;
import com.and.netease.rss.RSSHandler;
import com.and.netease.rss.RSSItem;

public class MainWeixin extends Activity {

	public static MainWeixin instance = null;

	private ViewPager mTabPager;
	private ImageView mTabImg;// ����ͼƬ
	private ImageView mTab1, mTab2, mTab3, mTab4;
	private int zero = 0;// ����ͼƬƫ����
	private int currIndex = 0;// ��ǰҳ�����
	private int one;// ����ˮƽ����λ��
	private int two;
	private int three;
	private LinearLayout mClose;
	private LinearLayout mCloseBtn;
	private View layout;
	private boolean menu_display = false;
	private PopupWindow menuWindow;
	private LayoutInflater inflater;
	@ViewInject(id = R.id.tabButtonGroup_layout)
	LinearLayout tabButtonGroup_layout;
	// private Button mRightBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_weixin);
		// ����activityʱ���Զ����������
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		instance = this;
		/*
		 * mRightBtn = (Button) findViewById(R.id.right_btn);
		 * mRightBtn.setOnClickListener(new Button.OnClickListener() { @Override
		 * public void onClick(View v) { showPopupWindow
		 * (MainWeixin.this,mRightBtn); } });
		 */

		mTabPager = (ViewPager) findViewById(R.id.tabpager);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());

		mTab1 = (ImageView) findViewById(R.id.img_weixin);
		mTab2 = (ImageView) findViewById(R.id.img_address);
		mTab3 = (ImageView) findViewById(R.id.img_friends);
		mTab4 = (ImageView) findViewById(R.id.img_settings);
		mTabImg = (ImageView) findViewById(R.id.img_tab_now);
		mTab1.setOnClickListener(new MyOnClickListener(0));
		mTab2.setOnClickListener(new MyOnClickListener(1));
		mTab3.setOnClickListener(new MyOnClickListener(2));
		mTab4.setOnClickListener(new MyOnClickListener(3));
		Display currDisplay = getWindowManager().getDefaultDisplay();// ��ȡ��Ļ��ǰ�ֱ���
		int displayWidth = currDisplay.getWidth();
		int displayHeight = currDisplay.getHeight();
		one = displayWidth / 4; // ����ˮƽ����ƽ�ƴ�С
		two = one * 2;
		three = one * 3;
		// Log.i("info", "��ȡ����Ļ�ֱ���Ϊ" + one + two + three + "X" + displayHeight);

		// InitImageView();//ʹ�ö���
		// ��Ҫ��ҳ��ʾ��Viewװ��������
		LayoutInflater mLi = LayoutInflater.from(this);
		// View view1 = mLi.inflate(R.layout.main_tab_weixin, null);
		View view1 = mLi.inflate(R.layout.main_tab_home, null);
		View view2 = mLi.inflate(R.layout.main_tab_address, null);
		View view3 = mLi.inflate(R.layout.main_tab_friends, null);
		View view4 = mLi.inflate(R.layout.main_tab_settings, null);

		// ÿ��ҳ���view����
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		// ���ViewPager������������
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			// @Override
			// public CharSequence getPageTitle(int position) {
			// return titles.get(position);
			// }

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		mTabPager.setAdapter(mPagerAdapter);
	}

	/**
	 * ͷ��������
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	};

	/*
	 * ҳ���л�����(ԭ����:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;

			for (int i = 0; i < tabButtonGroup_layout.getChildCount(); i++) {
				Object v = tabButtonGroup_layout.getChildAt(i);
				if (v instanceof LinearLayout) {
					Log.d("getChildAt:", ((View) v).getId() + "");
				}
			}
			switch (arg0) {
				case 0 :
					mTab1.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_weixin_pressed));
					if (currIndex == 1) {
						animation = new TranslateAnimation(one, 0, 0, 0);
						mTab2.setImageDrawable(getResources().getDrawable(
								R.drawable.tab_address_normal));
					} else if (currIndex == 2) {
						animation = new TranslateAnimation(two, 0, 0, 0);
						mTab3.setImageDrawable(getResources().getDrawable(
								R.drawable.tab_find_frd_normal));
					} else if (currIndex == 3) {
						animation = new TranslateAnimation(three, 0, 0, 0);
						mTab4.setImageDrawable(getResources().getDrawable(
								R.drawable.tab_settings_normal));
					}
					subActivuty();
					break;
				case 1 :
					mTab2.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_address_pressed));
					if (currIndex == 0) {
						animation = new TranslateAnimation(zero, one, 0, 0);
						mTab1.setImageDrawable(getResources().getDrawable(
								R.drawable.tab_weixin_normal));
					} else if (currIndex == 2) {
						animation = new TranslateAnimation(two, one, 0, 0);
						mTab3.setImageDrawable(getResources().getDrawable(
								R.drawable.tab_find_frd_normal));
					} else if (currIndex == 3) {
						animation = new TranslateAnimation(three, one, 0, 0);
						mTab4.setImageDrawable(getResources().getDrawable(
								R.drawable.tab_settings_normal));
					}
					break;
				case 2 :
					mTab3.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_find_frd_pressed));
					if (currIndex == 0) {
						animation = new TranslateAnimation(zero, two, 0, 0);
						mTab1.setImageDrawable(getResources().getDrawable(
								R.drawable.tab_weixin_normal));
					} else if (currIndex == 1) {
						animation = new TranslateAnimation(one, two, 0, 0);
						mTab2.setImageDrawable(getResources().getDrawable(
								R.drawable.tab_address_normal));
					} else if (currIndex == 3) {
						animation = new TranslateAnimation(three, two, 0, 0);
						mTab4.setImageDrawable(getResources().getDrawable(
								R.drawable.tab_settings_normal));
					}
					break;
				case 3 :
					mTab4.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_settings_pressed));
					if (currIndex == 0) {
						animation = new TranslateAnimation(zero, three, 0, 0);
						mTab1.setImageDrawable(getResources().getDrawable(
								R.drawable.tab_weixin_normal));
					} else if (currIndex == 1) {
						animation = new TranslateAnimation(one, three, 0, 0);
						mTab2.setImageDrawable(getResources().getDrawable(
								R.drawable.tab_address_normal));
					} else if (currIndex == 2) {
						animation = new TranslateAnimation(two, three, 0, 0);
						mTab3.setImageDrawable(getResources().getDrawable(
								R.drawable.tab_find_frd_normal));
					}
					break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // ��ȡ
																				// back��

			if (menu_display) { // ��� Menu�Ѿ��� ���ȹر�Menu
				menuWindow.dismiss();
				menu_display = false;
			} else {
				Intent intent = new Intent();
				intent.setClass(MainWeixin.this, Exit.class);
				startActivity(intent);
			}
		}

		else if (keyCode == KeyEvent.KEYCODE_MENU) { // ��ȡ Menu��
			if (!menu_display) {
				// ��ȡLayoutInflaterʵ��
				inflater = (LayoutInflater) this
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				// �����main��������inflate�м����Ŷ����ǰ����ֱ��this.setContentView()�İɣ��Ǻ�
				// �÷������ص���һ��View�Ķ����ǲ����еĸ�
				layout = inflater.inflate(R.layout.main_menu, null);

				// ��������Ҫ�����ˣ����������ҵ�layout���뵽PopupWindow���أ������ܼ�
				menuWindow = new PopupWindow(layout, LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT); // ������������width��height
				// menuWindow.showAsDropDown(layout); //���õ���Ч��
				// menuWindow.showAsDropDown(null, 0, layout.getHeight());
				menuWindow.showAtLocation(this.findViewById(R.id.mainweixin),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // ����layout��PopupWindow����ʾ��λ��
				// ��λ�ȡ����main�еĿؼ��أ�Ҳ�ܼ�
				mClose = (LinearLayout) layout.findViewById(R.id.menu_close);
				mCloseBtn = (LinearLayout) layout
						.findViewById(R.id.menu_close_btn);

				// �����ÿһ��Layout���е����¼���ע��ɡ�����
				// ���絥��ĳ��MenuItem��ʱ�����ı���ɫ�ı�
				// ����׼����һЩ����ͼƬ������ɫ
				mCloseBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// Toast.makeText(Main.this, "�˳�",
						// Toast.LENGTH_LONG).show();
						Intent intent = new Intent();
						intent.setClass(MainWeixin.this, Exit.class);
						startActivity(intent);
						menuWindow.dismiss(); // ��Ӧ����¼�֮��ر�Menu
					}
				});
				menu_display = true;
			} else {
				// �����ǰ�Ѿ�Ϊ��ʾ״̬������������
				menuWindow.dismiss();
				menu_display = false;
			}

			return false;
		}
		return false;
	}

	// ���ñ������Ҳఴť������
	public void btnmainright(View v) {
		Intent intent = new Intent(MainWeixin.this, MainTopRightDialog.class);
		startActivity(intent);
		// Toast.makeText(getApplicationContext(), "����˹��ܰ�ť",
		// Toast.LENGTH_LONG).show();
	}

	public void startchat(View v) { // С�� �Ի�����
		Intent intent = new Intent(MainWeixin.this, ChatActivity.class);
		startActivity(intent);
		// Toast.makeText(getApplicationContext(), "��¼�ɹ�",
		// Toast.LENGTH_LONG).show();
	}

	public void exit_settings(View v) { // �˳� α���Ի��򡱣���ʵ��һ��activity
		Intent intent = new Intent(MainWeixin.this, ExitFromSettings.class);
		startActivity(intent);
	}

	public void btn_shake(View v) { // �ֻ�ҡһҡ
		Intent intent = new Intent(MainWeixin.this, ShakeActivity.class);
		startActivity(intent);
	}

	MyListView listView;

	List<RSSItem> list;
	RSSHandler rssHandler;
	MyAdapter adapter;
	ViewSwitcher viewSwitcher;

	public void subActivuty() {
		initViews();
		rssHandler = new RSSHandler();
		requestRSSFeed();
	}

	private void initViews() {
		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher_news_top);
		listView = new MyListView(this);
		listView.setCacheColorHint(Color.argb(0, 0, 0, 0));
		ImageView testView = new ImageView(this);
		testView.setImageResource(R.drawable.temp);
		listView.addHeaderView(testView);
		listView.setonRefreshListener(refreshListener);

		viewSwitcher.addView(listView);
		viewSwitcher.addView(getLayoutInflater().inflate(
				R.layout.layout_progress_page, null));
		viewSwitcher.showNext();
		listView.setOnItemClickListener(listener);

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
					URL url = new URL(CONST.URL_NEWS_TOP);
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
					for (RSSItem rss : list) {
						System.out.println(rss);
					}
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

				listView.onRefreshComplete();
			}
		};
	};

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == 1) {
				return;
			}
			Intent intent = new Intent(MainWeixin.this,
					NewsContentActivity.class);
			intent.putExtra("content_url", list.get(position - 2).getLink());
			MainWeixin.this.startActivityForResult(intent, position);
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

			holder.tv_date.setText(list.get(position).getPubDate());
			holder.tv_title.setText(list.get(position).getTitle());
			// TextViewʹ��Html������ͼƬ��ʾ
			// Spanned text = Html.fromHtml(list.get(position).getDescription(),
			// imgGetter, null);
			holder.tv_Description.setText(
					Html.fromHtml(list.get(position).getDescription()),
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
		System.out.println("����");
		super.onActivityResult(requestCode, resultCode, data);

	}

}
