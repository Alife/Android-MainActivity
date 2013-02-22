package cn.buaa.myweixin.base;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.os.Bundle;

import com.pentasoft.configs.Configs.SystemConfig;

public class BaseActivity extends Activity {

	public FinalDb db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = FinalDb.create(this, SystemConfig.getDbName());
	}
}
