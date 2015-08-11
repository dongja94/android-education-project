package com.example.samples2database;

import android.provider.BaseColumns;

public class DBConstant {
	public interface PersonTable extends BaseColumns {
		public static final String TABLE_NAME = "persontbl";
		public static final String NAME = "name";
		public static final String AGE = "age";
		public static final String ADDRESS = "address";
	}
}
