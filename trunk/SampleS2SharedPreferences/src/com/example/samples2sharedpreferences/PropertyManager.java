package com.example.samples2sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
	private static final String PREF_NAME = "prefs";
	
	private PropertyManager() {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
		//MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		mEditor = mPrefs.edit();
	}
	
	private static final String FIELD_ID = "id";
	public void setId(String id) {
		mEditor.putString(FIELD_ID, id);
		mEditor.commit();
	}
	public String getId() {
		return mPrefs.getString(FIELD_ID, "");
	}
	
	private static final String FIELD_PASSWORD = "password";
	public void setPassword(String password) {
		mEditor.putString(FIELD_PASSWORD, password);
		mEditor.commit();
	}
	
	public String getPassword() {
		return mPrefs.getString(FIELD_PASSWORD, "");
	}
}
