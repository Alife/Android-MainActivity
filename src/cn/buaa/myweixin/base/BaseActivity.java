package cn.buaa.myweixin.base;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pentasoft.configs.Configs.SystemConfig;

public class BaseActivity extends Activity {

	public FinalDb db;

	String tag = "ActivityState";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = FinalDb.create(this, SystemConfig.getDbName());
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

	protected void Notification(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
}
