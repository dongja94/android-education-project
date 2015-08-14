package com.example.samples2gcm;

import android.content.Context;
import android.content.SharedPreferences;

public class PropertyManager {
	private static PropertyManager instance;
	public static PropertyManager getInstance() {
		if (instance == null) {
			instance = new PropertyManager();
		}
		return instance;
	}
	
	SharedPreferences mPrefs;
	SharedPreferences.Editor mEditor;
	
	private static final String PREFS_NAME = "my_prefs";
	private PropertyManager() {
		mPrefs = MyApplication.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		mEditor = mPrefs.edit();
	}
	
	
	private String regId;
	private static final String REG_ID = "regId";
	
	public void setRegistrationId(String regId) {
		this.regId = regId;
		mEditor.putString(REG_ID, regId);
		mEditor.commit();
	}
	
	public String getRegistrationId() {
		if (regId == null) {
			regId = mPrefs.getString(REG_ID, "");
		}
		return regId;
	}
	
}
