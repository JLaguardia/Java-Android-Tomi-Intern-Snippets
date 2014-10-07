package com.tomitg.www;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDatabaseHelper extends SQLiteOpenHelper{
	
	public static String DATABASE_ADDRESS = "xx.xx.xx.x:xxxx/path.accdb";
	
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "HR Database.accdb";
	public static final String TABLE_NAME = "Login";
	public static final String DB_ID = "ID";
	public static final String USERNAME = "UserName";
	public static final String PASSWORD = "Password";
	public static final String CLOCKEDIN = "ClockedIn";
	public static final String TIMEIN = "TimeIn";
	public static final String TIMEOUT = "TimeOut";
	public static final String TOTALHOURS = "TotalHours";
	public static int HOURSMAXVALUE = 40;

	public static Credentials cr;
	public static SharedPreferences pref;
	private static SQLiteDatabase db;
	private static Cursor cursor;
	
	public LocalDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}


	
	/**
	 * @author <strong>Laguardia</strong> - author note </br>first time app runs, this method will be called.	
	 *	Local database is created.</br>
	 *	This is for testing purposes and the final product will connect to a remote </br>
	 *  cloud database via tcp(?)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLE_NAME + "(" 
			+ DB_ID + " integer primary key autoincrement not null,"
			+ USERNAME + " varchar(255) not null,"
			+ PASSWORD + " varchar(255) not null,"
			+ CLOCKEDIN + " boolean,"
			+ TIMEIN + " real,"
			+ TIMEOUT + " real,"
			+ TOTALHOURS +" real"
		    + ")";
		db.execSQL(sql);

		//test data
		sql = "insert into " + TABLE_NAME + " (" + USERNAME + ", " + PASSWORD + ", " + CLOCKEDIN + ", " + TIMEIN + ")" 
				+ " values ('Testing123', 'password', 0, 450000);";
		db.execSQL(sql);
		sql = "insert into " + TABLE_NAME + " (" + USERNAME + ", " + PASSWORD + ", " + TIMEIN + ", " + TOTALHOURS + ")" 
				+ " values ('Johnny', 'PassWord', 869000.0925, 36.0);";
		db.execSQL(sql);
		sql = "insert into " + TABLE_NAME + " (" + USERNAME + ", " + PASSWORD + ")" 
				+ " values ('Sally', 'PASSWORD');";
		db.execSQL(sql);
	}
	
	//this will only be called upon clocking out
	public static void updateHours(LocalDatabaseHelper dbh){
		db = dbh.getWritableDatabase();
		double total, res;
		String sql = "select " + TIMEIN + ", " + TIMEOUT + ", " + TOTALHOURS + " from " + TABLE_NAME + " where " + USERNAME + " = ?";
		cursor = db.rawQuery(sql, new String[] {cr.getUsername()});
		cursor.moveToFirst();
		res = ((cursor.getDouble(1) / 60) / 60) - ((cursor.getDouble(0) / 60) / 60);
		total = cursor.getDouble(2);
		res += total;
		sql = "update " + TABLE_NAME + " set " + TOTALHOURS +" = " + res + " where " + USERNAME + " = ?";
		db.execSQL(sql);
		cursor.close();
		db.close();
	}
	
	public static String getHours(LocalDatabaseHelper dbh){
    	//get 'total hours' by getting an accumulator which will be reset
    	//to 0 on server side every 2 weeks -- php code probable.
		db = dbh.getWritableDatabase();
		String sql = "select " + TOTALHOURS + " from " + TABLE_NAME + " where " 
    				  + USERNAME + " = ?";
    	cursor = db.rawQuery(sql, new String[] {cr.getUsername()});
    	cursor.moveToFirst();
    	sql = "" + String.format("%.2f", cursor.getDouble(0));
    	cursor.close();
    	db.close();
    	return sql;
    }
	
	public static void clockIn(LocalDatabaseHelper dbh){
		//update login set clockedin to true and timein to current time using calendar vals
        db = dbh.getWritableDatabase();
		String sql = "update " + TABLE_NAME + " set " + CLOCKEDIN + " = 1, " + TIMEIN + " = " + cr.getNowEpochSeconds()
					+ " where " + USERNAME + " = ?";
		db.execSQL(sql, new String[] {cr.getUsername()});
		cr.setClockedIn(true);
		cr.setClockTime(cr.getNowDateString());
		db.close();
	}
	
	public static void clockOut(LocalDatabaseHelper dbh){
		//update login set clockedin to false and timeout to current time using calendar vals		
		db = dbh.getWritableDatabase();
		String sql = "update " + TABLE_NAME + " set " + CLOCKEDIN + " = 0, " + TIMEOUT + " = " + cr.getNowEpochSeconds()
					+" where " + USERNAME + " = ?";
		db.execSQL(sql, new String[] {cr.getUsername()});
		cr.setClockedIn(false);
		updateTotalHours();
		db.close();
		cursor.close();
	}
	
	public static void updateTotalHours(){		
		double timeOut, timeIn, res;
		String sql = "select " + TIMEIN + ", " + TIMEOUT + ", " + TOTALHOURS + " from " + TABLE_NAME + " where "
					+ USERNAME + " = ?";
		cursor = db.rawQuery(sql, new String[] {cr.getUsername()});
		cursor.moveToFirst();
		timeIn = cursor.getDouble(0);
		timeOut = cursor.getDouble(1);
		res = cursor.getDouble(2);
		res += (((timeOut / 60) / 60) - ((timeIn / 60 / 60)));
		sql = "update " + TABLE_NAME + " set " + TOTALHOURS + " = " + res + " where "
				+ USERNAME + " = ?";
		db.execSQL(sql, new String[] {cr.getUsername()});
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//this is if we change the local database on a new release.
	/*	cr = new Credentials();
		String sql = "create table " + TABLE_NAME + "(" 
			+ DB_ID + " integer primary key autoincrement not null,"
			+ USERNAME + " varchar(255) not null,"
			+ PASSWORD + " varchar(255) not null,"
			+ CLOCKEDIN + " boolean,"
			+ TIMEIN + " real,"
			+ TIMEOUT + " real,"
			+ TOTALHOURS +" real"
		    + ")";
		db.execSQL(sql);

		//test data
		sql = "insert into " + TABLE_NAME + " (" + USERNAME + ", " + PASSWORD + ", " + CLOCKEDIN + ", " + TIMEIN + ")" 
				+ " values ('Testing123', 'password', 0, 450000);";
		db.execSQL(sql);
		sql = "insert into " + TABLE_NAME + " (" + USERNAME + ", " + PASSWORD + ", " + TIMEIN + ", " + TOTALHOURS + ")" 
				+ " values ('Johnny', 'PassWord', 869000.0925, 36.0);";
		db.execSQL(sql);
		sql = "insert into " + TABLE_NAME + " (" + USERNAME + ", " + PASSWORD + ")" 
				+ " values ('Sally', 'PASSWORD');";
		db.execSQL(sql);*/
	}
}
