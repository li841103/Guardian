package com.szhd.util;

import java.util.List;

import android.app.Application;

import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.model.LatLng;

public class MyApplication extends Application {
	// 用来当前登录用户的设备ID
	public static String device;
	// 全局的用户头像地址
	public static String avatarPath;
	// 全局的用于存放电子围栏范围的多边形对象
	public static PolygonOptions options;
	// 全局的用于存放上次设置电子围栏的坐标列表
	public static List<LatLng> pts;
}
