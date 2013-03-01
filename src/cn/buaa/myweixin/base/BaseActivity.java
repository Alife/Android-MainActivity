package cn.buaa.myweixin.base;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import cn.buaa.myweixin.R;
import cn.buaa.myweixin.Whatsnew;

import com.mobilenpsite.configs.Config;
import com.mobilenpsite.utility.NetHelper;

public class BaseActivity extends Activity {

	public FinalDb db;

	String tag = "ActivityState";
	protected boolean isNetworkAvailable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(tag, "onCreate");
		Log.i(tag, String.valueOf(this.equals(getApplicationContext())));
		db = FinalDb.create(this, Config.DB_FILE_NAME, false);
		isNetworkAvailable = NetHelper.networkIsAvailable(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(tag, "onStart");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(tag, "onPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(tag, "onResume");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(tag, "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(tag, "onDestroy");
	}

	protected void Notification(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		Notification1(msg);
	}

	protected void Notification1(String msg) {
		// 消息通知栏
		// 定义NotificationManager
		String ns = this.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		// 定义通知栏展现的内容信息
		int icon = R.drawable.icon;
		CharSequence tickerText = "我的通知栏标题";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		// 定义下拉通知栏时要展现的内容信息
		Context context = getApplicationContext();
		CharSequence contentTitle = "通知";
		CharSequence contentText = msg;
		Intent notificationIntent = new Intent(this, Whatsnew.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		// 用mNotificationManager的notify方法通知用户生成标题栏消息通知
		mNotificationManager.notify(1, notification);
	}
}
