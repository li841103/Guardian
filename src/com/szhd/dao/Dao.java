package com.szhd.dao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Dao {

	
	private SQLiteDatabase db;
	
	public Dao(Context context) {
		db = new DatabaseHelper(context).getWritableDatabase();
	}
	
	
	public void addSZHD(String sql, Object... o) {
		db.beginTransaction(); // 开始事务
		db.execSQL(sql,o);
		db.setTransactionSuccessful(); // 设置事务成功完成
		db.endTransaction(); // 结束事务
	}

	public void deleteSZHD(String sql) {
		db.beginTransaction(); // 开始事务
		db.execSQL(sql);
		db.setTransactionSuccessful(); // 设置事务成功完成
		db.endTransaction(); // 结束事务
	}

	public void updateSZHD(String... s) {
		db.beginTransaction(); // 开始事务
		ContentValues values = new ContentValues();
		values.put("ts", s[0]);
		values.put("dr", s[1]);
		db.update(s[2], values, s[3],new String[] { s[4]});
		db.setTransactionSuccessful(); // 设置事务成功完成
		db.endTransaction(); // 结束事务
	}

	public JSONArray querySZHD(String... s) throws JSONException {
		Cursor cursor = db.rawQuery(s[0], null);
		JSONArray ja = new JSONArray();
		while (cursor.moveToNext()) {
			JSONObject jo = new JSONObject();
			for(int i=1; i<s.length; i++){
				jo.put(s[i],cursor.getString(cursor.getColumnIndex(s[i])));
			}
			ja.put(jo);
		}
		return ja;
	}
	
}
