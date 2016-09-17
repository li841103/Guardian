package com.szhd.pulltorefresh;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @author 卢文王
 *
 * @time 2016.02.26
 *
 * @description	刷新时间
 *
 */
public class RefreshTime {
	
    final static String PRE_NAME = "refresh_time";
    final static String SET_FRESHTIME = "set_refresh_time";

    public static String getRefreshTime(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PRE_NAME, Context.MODE_APPEND);
        return preferences.getString(SET_FRESHTIME, "");
    }

    public static void setRefreshTime(Context context, String newPasswd) {
        SharedPreferences preferences = context.getSharedPreferences(PRE_NAME, Context.MODE_APPEND);
        Editor editor = preferences.edit();
        editor.putString(SET_FRESHTIME, newPasswd);
        editor.commit();
    }

}