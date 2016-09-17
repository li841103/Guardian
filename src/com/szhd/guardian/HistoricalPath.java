package com.szhd.guardian;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.UiSettings;
import com.szhd.util.GetNowDate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HistoricalPath extends Activity{

	private BaiduMap mbaidumap;
	private MapView hpbmapView;
	private UiSettings mUiSettings;
	private ImageView calendar;
	private TextView textView2;
	private String[] chdate;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.historicalpath);
		init();
	}

	private void init() {
		//显示当前日期	
		final String date = GetNowDate.getTodayData();
		final String backdate1 = GetNowDate.getBackData();
		final String backdate2 = GetNowDate.getBackData();
		final String backdate3 = GetNowDate.getBackData();
		final String backdate4 = GetNowDate.getBackData();
		chdate = new String[]{date,backdate1,backdate2,backdate3,backdate4};
		new AlertDialog.Builder(HistoricalPath.this)
		.setItems(chdate, new DialogInterface.OnClickListener(){  
			public void onClick(DialogInterface dialog, int which){  
				//chdate[which];
				
				
				Toast.makeText(HistoricalPath.this, "您已经选择了: " 
						+ which + ":" + chdate[which],Toast.LENGTH_LONG).show();  
				dialog.dismiss();  
			}  
		}).show(); 
		calendar = (ImageView) findViewById(R.id.calendar);
		calendar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(HistoricalPath.this)
				.setItems(chdate, new DialogInterface.OnClickListener(){  
					public void onClick(DialogInterface dialog, int which){  
						//chdate[which];
						
						
						Toast.makeText(HistoricalPath.this, "您已经选择了: " 
								+ which + ":" + chdate[which],Toast.LENGTH_LONG).show();  
						dialog.dismiss();  
					}  
				}).show(); 
			}
		});
		
		textView2 = (TextView) findViewById(R.id.textView2);
		textView2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		// 初始化地图
		hpbmapView = (MapView) findViewById(R.id.hpbmapView);
        //隐藏地图上百度地图logo图标
		hpbmapView.removeViewAt(1);
        //隐藏自带的地图缩放控件
		hpbmapView.showZoomControls(false);
        mbaidumap = hpbmapView.getMap();
        //获得UI设置
        mUiSettings = mbaidumap.getUiSettings();
        //是否启用旋转手势
        mUiSettings.setRotateGesturesEnabled(false);
        //是否启用俯视手势
        mUiSettings.setOverlookingGesturesEnabled(false);
        //是否启用指南针图层
        mUiSettings.setCompassEnabled(false);
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	
	
	
	
}
