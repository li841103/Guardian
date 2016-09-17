package com.szhd.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.szhd.bean.ClockBean;
import com.szhd.guardian.R;
import com.szhd.guardian.R.color;

public class ClockActivity extends Activity{

	private ImageView clockback;
	private RelativeLayout clockfinish;
	private TextView clockmonday;
	private TextView clocktuesday;
	private TextView clockwednesday;
	private TextView clockthursday;
	private TextView clockfriday;
	private TextView clocksaturday;
	private TextView clocksunday;
	private TextView clocktime;
	
	private boolean date[] = new boolean[7];
	private int	position = 0;
	private boolean operationflag;
	private final String[] s = {"周一","周二","周三","周四","周五","周六","周日"}; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clock);
		init();
	}


	private void init() {
		clockback = (ImageView) findViewById(R.id.clockback);
		clockback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		clockfinish = (RelativeLayout) findViewById(R.id.clockfinish);
		clockfinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//传递数据
				Intent intent = new Intent(ClockActivity.this, FunctionActivity.class);
				ClockBean cb = new ClockBean();
				cb.setPOSITION(position);
				cb.setOPERATIONFLAG(operationflag);
				cb.setTIME(clocktime.getText().toString());
				List<String> list = new ArrayList<String>();
				for(int i=0; i<7; i++){
					if(date[i] == true){
						list.add(s[i]);
					}
				}
				cb.setDATE(list);
				intent.putExtra("BackClockActivity", cb);
				startActivity(intent);
				finish();
			} 
		});
//		//闹钟时间
		clocktime = (TextView) findViewById(R.id.clocktime);
		clocktime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TimePickerDialog timePickerDialog = new TimePickerDialog(ClockActivity.this,
						new TimePickerDialog.OnTimeSetListener(){
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute){
						String mhourOfDay = "00";
						String mminute = "00";
						if(hourOfDay < 10){
							mhourOfDay = "0"+ hourOfDay;
						}else{
							mhourOfDay = "" + hourOfDay;
						}
						if(minute < 10){
							mminute = "0"+ minute;
						}else{
							mminute = "" + minute;
						}
						clocktime.setText(mhourOfDay+":"+mminute);
					}
				}, 0, 0, true);
				timePickerDialog.show();
			}
		});

		
		clockmonday = (TextView) findViewById(R.id.clockmonday);
		clockmonday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(date[0] == true){
					date[0] = false;
					clockmonday.setBackgroundColor(Color.WHITE);
					clockmonday.setTextColor(Color.parseColor("#808080"));
				}else{
					date[0] = true;
					clockmonday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
					clockmonday.setTextColor(Color.WHITE);
				}
			}
		});
		clocktuesday = (TextView) findViewById(R.id.clocktuesday);
		clocktuesday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(date[1] == true){
					date[1] = false;
					clocktuesday.setBackgroundColor(Color.WHITE);
					clocktuesday.setTextColor(Color.parseColor("#808080"));
				}else{
					date[1] = true;
					clocktuesday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
					clocktuesday.setTextColor(Color.WHITE);
				}
			}
		});
		clockwednesday = (TextView) findViewById(R.id.clockwednesday);
		clockwednesday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(date[2] == true){
					date[2] = false;
					clockwednesday.setBackgroundColor(Color.WHITE);
					clockwednesday.setTextColor(Color.parseColor("#808080"));
				}else{
					date[2] = true;
					clockwednesday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
					clockwednesday.setTextColor(Color.WHITE);
				}
			}
		});
		clockthursday = (TextView) findViewById(R.id.clockthursday);
		clockthursday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(date[3] == true){
					date[3] = false;
					clockthursday.setBackgroundColor(Color.WHITE);
					clockthursday.setTextColor(Color.parseColor("#808080"));
				}else{
					date[3] = true;
					clockthursday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
					clockthursday.setTextColor(Color.WHITE);
				}
			}
		});
		clockfriday = (TextView) findViewById(R.id.clockfriday);
		clockfriday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(date[4] == true){
					date[4] = false;
					clockfriday.setBackgroundColor(Color.WHITE);
					clockfriday.setTextColor(Color.parseColor("#808080"));
				}else{
					date[4] = true;
					clockfriday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
					clockfriday.setTextColor(Color.WHITE);
				}
			}
		});
		clocksaturday = (TextView) findViewById(R.id.clocksaturday);
		clocksaturday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(date[5] == true){
					date[5] = false;
					clocksaturday.setBackgroundColor(Color.WHITE);
					clocksaturday.setTextColor(Color.parseColor("#808080"));
				}else{
					date[5] = true;
					clocksaturday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
					clocksaturday.setTextColor(Color.WHITE);
				}
			}
		});
		clocksunday = (TextView) findViewById(R.id.clocksunday);
		clocksunday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(date[6] == true){
					date[6] = false;
					clocksunday.setBackgroundColor(Color.WHITE);
					clocksunday.setTextColor(Color.parseColor("#808080"));
				}else{
					date[6] = true;
					clocksunday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
					clocksunday.setTextColor(Color.WHITE);
				}
			}
		});
		
		Intent intent = getIntent();
		ClockBean cbj = (ClockBean) intent.getSerializableExtra("JumpClockBean"); 
		if(cbj != null){
			for(int i=0; i<7; i++){
				for(int j=0; j<cbj.getDATE().size(); j++){
					if(cbj.getDATE().get(j).equals(s[i])){
						date[i] = true;
						choise(i);
					}
				}
			}
			position = cbj.getPOSITION();
			operationflag = cbj.isOPERATIONFLAG();
		}
		
		ClockBean cba = (ClockBean) intent.getSerializableExtra("AddClockBean"); 
		if(cba != null){
			for(int i=0; i<7; i++){
				for(int j=0; j<cba.getDATE().size(); j++){
					if(cba.getDATE().get(j).equals(s[i])){
						date[i] = true;
						choise(i);
					}
				}
			}
			operationflag = cba.isOPERATIONFLAG();
		}
		
		
	}
	
	private void choise(int i){
		switch (i) {
		case 0:
			clockmonday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
			clockmonday.setTextColor(Color.WHITE);
			break;
		case 1:
			clocktuesday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
			clocktuesday.setTextColor(Color.WHITE);
			break;
		case 2:
			clockwednesday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
			clockwednesday.setTextColor(Color.WHITE);
			break;
		case 3:
			clockthursday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
			clockthursday.setTextColor(Color.WHITE);
			break;
		case 4:
			clockfriday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
			clockfriday.setTextColor(Color.WHITE);
			break;
		case 5:
			clocksaturday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
			clocksaturday.setTextColor(Color.WHITE);
			break;
		case 6:
			clocksunday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
			clocksunday.setTextColor(Color.WHITE);
			break;
		}
	}
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

	}
	
}
