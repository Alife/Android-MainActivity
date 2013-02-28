package cn.buaa.myweixin.base;

import net.tsz.afinal.FinalDb;
import android.app.ActivityGroup;
import android.os.Bundle;

import com.mobilenpsite.configs.Configs.SystemConfig;

public class BaseActivityGroup extends ActivityGroup {

	public FinalDb db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = FinalDb.create(this, SystemConfig.getDbName());
	}
}