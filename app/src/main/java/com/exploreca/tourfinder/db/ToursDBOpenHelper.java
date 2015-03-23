package com.exploreca.tourfinder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ToursDBOpenHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = "EXPLORECA";

	private static final String DATABASE_NAME = "tours.db";
	private static final int DATABASE_VERSION = 2;

	public static final String TABLE_TOURS = "tours";
	public static final String COLUMN_ID = "tourId";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DESC = "description";
	public static final String COLUMN_PRICE = "price";
	public static final String COLUMN_IMAGE = "image";

	private static final String TABLE_CREATE = 
			"CREATE TABLE " + TABLE_TOURS + " (" +
					COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COLUMN_TITLE + " TEXT, " +
					COLUMN_DESC + " TEXT, " +
					COLUMN_IMAGE + " TEXT, " +
					COLUMN_PRICE + " NUMERIC " +
					")";

	public static final String TABLE_MYTOURS = "mytours";
	private static final String TABLE_MYTOURS_CREATE = 
			"CREATE TABLE " + TABLE_MYTOURS + " (" +
			COLUMN_ID + " INTEGER PRIMARY KEY)";
	
	public ToursDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
		db.execSQL(TABLE_MYTOURS_CREATE);

		Log.i(LOGTAG, "Table has been created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOURS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYTOURS);
		onCreate(db);

		Log.i(LOGTAG, "Database has been upgraded from " + 
				oldVersion + " to " + newVersion);
	}

}
