package cn.buaa.myweixin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import cn.buaa.myweixin.base.BaseActivity;

import com.pentasoft.db.model.SettingSystem;

public class Appstart extends BaseActivity {

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
				// 直接进入主页
				// Intent intent = new Intent(Appstart.this, Welcome.class);
				// startActivity(intent);

				// 判断是否是第一次进入
				// 第一次进入显示介绍界面,否则直接进入主页
				boolean isfirst = true;
				SettingSystem settingSystem = db.findById(1, SettingSystem.class);
				if (settingSystem != null && settingSystem.getIsFirst().equals("1")) {
					isfirst = false;
				} else {
					settingSystem = new SettingSystem();
					settingSystem.SettingSystemId = 1;
					settingSystem.IsFirst = "1";
					db.save(settingSystem);
				}
				if (isfirst) {
					Intent intent = new Intent(Appstart.this, Whatsnew.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(Appstart.this, Main.class);
					startActivity(intent);
				}

				Appstart.this.finish();
			}
		}, 1000);
	}
}