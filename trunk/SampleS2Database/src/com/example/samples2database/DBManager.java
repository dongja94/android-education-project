package com.example.samples2database;

import java.util.ArrayList;
import java.util.List;

import com.example.samples2database.DBConstant.PersonTable;

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
		String sql = "CREATE TABLE "+PersonTable.TABLE_NAME+"(" +
				PersonTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				PersonTable.NAME + " TEXT," +
				PersonTable.AGE + " INTEGER," +
				PersonTable.ADDRESS + " TEXT);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	
	public void addPerson(Person p) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(PersonTable.NAME,p.name);
		values.put(PersonTable.AGE,p.age);
		values.put(PersonTable.ADDRESS, p.address);
		
		db.insert(PersonTable.TABLE_NAME, null, values);
		
	}
	
	public List<Person> getPerson(String keyword) {
		List<Person> list = new ArrayList<Person>();
		
		Cursor c = getPersonCursor(keyword);
		
		while(c.moveToNext()) {
			Person p = new Person();
			p._id = c.getLong(c.getColumnIndex(PersonTable._ID));
			p.name = c.getString(c.getColumnIndex(PersonTable.NAME));
			p.age = c.getInt(c.getColumnIndex(PersonTable.AGE));
			p.address = c.getString(c.getColumnIndex(PersonTable.ADDRESS));
			list.add(p);
		}
		
		c.close();
		
		return list;
	}
	
	public Cursor getPersonCursor(String keyword) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = null;
		String[] columns = { PersonTable._ID, PersonTable.NAME, PersonTable.AGE, PersonTable.ADDRESS };
		String selection = null;
		String[] selectionArgs = null;
		if (!TextUtils.isEmpty(keyword)) {
			selection = PersonTable.NAME + " = ?";
			selectionArgs = new String[] {keyword};
		} 
		c = db.query(PersonTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
		
		return c;
	}
}
