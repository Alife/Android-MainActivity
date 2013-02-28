package cn.buaa.myweixin.base;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import cn.buaa.myweixin.R;
import cn.buaa.myweixin.Whatsnew;

import com.pentasoft.configs.Configs.SystemConfig;

public class BaseActivity extends Activity {

	public FinalDb db;

	String tag = "ActivityState";
	Integer PageSize = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = FinalDb.create(this, SystemConfig.getDbName(), false);
		CheckNetwork();
		Log.i(tag, "onCreate");
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

	public void Notification(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		Notification1(msg);
	}

	public void Notification1(String msg) {
		// 消息通知栏
		// 定义NotificationManager
		String ns = this.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		// 定义通知栏展现的内容信息
		// 设置图标
		int icon = R.drawable.icon;
		CharSequence tickerText = "我的通知栏标题";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);

		// notification.defaults |= Notification.DEFAULT_SOUND;
		/*
		 * 添加声音 notification. defaults |=Notification .DEFAULT_SOUND; 或者使用以下几种方式
		 * notification .sound = Uri.parse(
		 * "file:///sdcard/notification/ringer.mp3" ); notification.sound =
		 * Uri.withAppendedPath (Audio.Media. INTERNAL_CONTENT_URI , "6");
		 * 如果想要让声音持续重复直到用户对通知做出反应 ， 则可以在notification的flags字段增加 "FLAG_INSISTENT"
		 * 如果notification的defaults字段包括了 "DEFAULT_SOUND" 属性，
		 * 则这个属性将覆盖sound字段中定义的声音
		 */
		/*
		 * 添加振动 notification.defaults |= Notification.DEFAULT_VIBRATE;
		 * 或者可以定义自己的振动模式： long[] vibrate = {0,100,200,300};
		 * //0毫秒后开始振动，振动100毫秒后停止，再过200毫秒后再次振动300毫秒 notification.vibrate =
		 * vibrate; long数组可以定义成想要的任何长度
		 * 如果notification的defaults字段包括了"DEFAULT_VIBRATE",则这个属性将覆盖vibrate字段中定义的振动
		 */
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		/*
		 * 
		 * 添加LED灯提醒 notification.defaults |= Notification.DEFAULT_LIGHTS;
		 * 或者可以自己的LED提醒模式: notification.ledARGB = 0xff00ff00;
		 * notification.ledOnMS = 300; //亮的时间 notification.ledOffMS = 1000;
		 * //灭的时间 notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		 */

		// 设置图标是否能被系统清理掉
		notification.flags = notification.flags | Notification.FLAG_AUTO_CANCEL;

		/*
		 * 更多的特征属性 notification.flags |= FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
		 * notification.flags |= FLAG_INSISTENT; //重复发出声音，直到用户响应此通知
		 * notification.flags |= FLAG_ONGOING_EVENT;
		 * //将此通知放到通知栏的"Ongoing"即"正在运行"组中 notification.flags |= FLAG_NO_CLEAR;
		 * //表明在点击了通知栏中的"清除通知"后，此通知不清除， //经常与FLAG_ONGOING_EVENT一起使用
		 * notification.number = 1; //number字段表示此通知代表的当前事件数量，它将覆盖在状态栏图标的顶部
		 * //如果要使用此字段，必须从1开始 notification.iconLevel = ; //
		 */// 定义下拉通知栏时要展现的内容信息
		Context context = getApplicationContext();
		CharSequence contentTitle = getText(R.string.app_notify_title);
		CharSequence contentText = msg;
		// 绑定intent，主要作用就是点击图标时能够进入程序
		Intent notificationIntent = new Intent(this, Whatsnew.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		// 用mNotificationManager的notify方法通知用户生成标题栏消息通知
		mNotificationManager.notify(1, notification);
	}

	private boolean CheckNetwork() {
		boolean flag = false;
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cwjManager.getActiveNetworkInfo() != null)
			flag = cwjManager.getActiveNetworkInfo().isAvailable();
		if (!flag) {
			Builder b = new AlertDialog.Builder(this).setTitle(R.string.app_setnetwork_error).setMessage(
					getResources().getString(R.string.app_setnetwork_title));

			b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					Intent mIntent = new Intent("/");
					ComponentName comp = new ComponentName("com.android.settings",
							"com.android.settings.WirelessSettings");
					mIntent.setComponent(comp);
					mIntent.setAction("android.intent.action.VIEW");
					startActivity(mIntent);
				}
			}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.cancel();
				}
			}).create();
			b.show();
		}

		return flag;
	}
}
