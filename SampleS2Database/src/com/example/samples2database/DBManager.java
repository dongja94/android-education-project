package com.example.samples2database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

public class DBManager extends SQLiteOpenHelper {
	private static DBManager instance;
	public static DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}
	private static final String DB_NAME = "mydb";
	private static final int DB_VERSION = 1;
	
	private DBManager() {
		super(MyApplication.getContext(), DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE persontbl(" +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"name TEXT," +
				"age INTEGER," +
				"address TEXT);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	
	public void addPerson(Person p) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name",p.name);
		values.put("age",p.age);
		values.put("address", p.address);
		
		db.insert("persontbl", null, values);
		
	}
	
	public List<Person> getPerson(String keyword) {
		List<Person> list = new ArrayList<Person>();
		
		Cursor c = getPersonCursor(keyword);
		
		while(c.moveToNext()) {
			Person p = new Person();
			p._id = c.getLong(c.getColumnIndex("_id"));
			p.name = c.getString(c.getColumnIndex("name"));
			p.age = c.getInt(c.getColumnIndex("age"));
			p.address = c.getString(c.getColumnIndex("address"));
			list.add(p);
		}
		
		c.close();
		
		return list;
	}
	
	public Cursor getPersonCursor(String keyword) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = null;
		if (!TextUtils.isEmpty(keyword)) {
			String sql = "SELECT _id,name,age,address FROM persontbl WHERE name = ?";
			c = db.rawQuery(sql, new String[] {keyword});
			
		} else {
			String sql = "SELECT _id,name,age,address FROM persontbl";
			c = db.rawQuery(sql, null);
		}
		return c;
	}
}
