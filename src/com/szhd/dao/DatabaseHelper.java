package com.szhd.dao;

import com.szhd.constant.CreatTableSql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	
	
	private static final String DB_NAME = "GUARDIAN.db"; // 数据库名称
	private static final int VERSION = 1; // 数据库版本,软件升级是要改变数据库版本
	

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//执行建表语句
		db.execSQL(CreatTableSql.BD_APPLYAUDIT);
	}

	// 更新数据库版本,发现版本改变后会执行此方法
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
