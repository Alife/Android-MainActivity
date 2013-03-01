package cn.buaa.myweixin.base;

import net.tsz.afinal.FinalDb;
import android.app.ActivityGroup;
import android.os.Bundle;

import com.mobilenpsite.configs.Config;

public class BaseActivityGroup extends ActivityGroup {

	public FinalDb db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = FinalDb.create(this, Config.DB_FILE_NAME, false);
	}
}