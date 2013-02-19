package cn.buaa.myweixin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Appstart extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appstart);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN); //全屏显示
		// Toast.makeText(getApplicationContext(), "孩子！好好背诵！",
		// Toast.LENGTH_LONG).show();
		// overridePendingTransition(R.anim.hyperspace_in,
		// R.anim.hyperspace_out);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// 打开直接进入主页
				// Intent intent = new Intent (Appstart.this,Welcome.class);
				// startActivity(intent);

				// 进入主页判断是否是第一次,进入,是:显示新版介绍.否:直接进入主页
				boolean isFirst = false;
				if (isFirst) {
					Intent intent = new Intent(Appstart.this, Whatsnew.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(Appstart.this, MainWeixin.class);
					startActivity(intent);
				}
				Appstart.this.finish();
			}
		}, 2000);
	}
}