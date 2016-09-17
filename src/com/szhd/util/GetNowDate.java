package com.szhd.util;

import java.text.DateFormat;
import java.util.GregorianCalendar;

/**
* 
* @author 卢文王
*
* @time 2016.03.04
*
* @description 提供字符串形式的日期到毫秒的转换，和毫秒到字符串日期以及年、月、日的转换
*
*/
public class GetNowDate {

	private static GregorianCalendar calendar = new GregorianCalendar();
	private static long MM = 0;
	
	// 获得昨天日期,格式"yyyy年mm月dd日"
	public static String getBackData() {
		// 86400000为一天的毫秒数
		String d = getDate(MM - 86400000);
		MM -= 86400000;
		return d;
	}
	
	// 获得今天日期,格式"yyyy年mm月dd日"
	public static String getTodayData() {
		return getDate(getNow());
	}

	// 获得明天日期,格式"yyyy年mm月dd日"
	public static String getTomoData() {
		// 86400000为一天的毫秒数
		String d = getDate(MM + 86400000);
		MM += 86400000;
		return d;
	}
	
	// 获得当前时间的毫秒表示
	private static long getNow() {
		GregorianCalendar now = new GregorianCalendar();
		MM = now.getTimeInMillis();
		return MM;
	}
	
	// 根据输入的毫秒数，获得日期字符串
	private static String getDate(long millis) {
		calendar.setTimeInMillis(millis);
		return DateFormat.getDateInstance().format(calendar.getTime());
	}
	
}

