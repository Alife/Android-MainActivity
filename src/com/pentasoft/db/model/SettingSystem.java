package com.pentasoft.db.model;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "SettingSystem")
public class SettingSystem {
	@Id(column = "SettingSystemId")
	public int SettingSystemId;
	public String IsFirst;

	public int getSettingSystemId() {
		return SettingSystemId;
	}

	public void setSettingSystemId(int settingSystemId) {
		SettingSystemId = settingSystemId;
	}

	public String getIsFirst() {
		return IsFirst;
	}

	public void setIsFirst(String isFirst) {
		IsFirst = isFirst;
	}

}