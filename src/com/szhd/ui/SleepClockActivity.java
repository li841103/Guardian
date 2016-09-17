package com.szhd.ui;

import java.util.ArrayList;
import java.util.List;

import com.szhd.bean.ElectricFenceBean;
import com.szhd.guardian.R;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class SleepClockActivity extends Activity{
	
	private ImageView eleback;
	private RelativeLayout finish;
	
	private TextView oldtime;
	private TextView newtime;
	
	private TextView monday;
	private TextView tuesday;
	private TextView wednesday;
	private TextView thursday;
	private TextView friday;
	private TextView saturday;
	private TextView sunday;
	
	private boolean date[] = new boolean[7];
	private int position = 0;
	private boolean operationflag;
	private final String[] s = {"周一","周二","周三","周四","周五","周六","周日"}; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sleepclock);
		init();
	}

	private void init() {
		eleback = (ImageView) findViewById(R.id.eleback);
		eleback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		finish = (RelativeLayout) findViewById(R.id.finish);
		finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//传递数据
				Intent intent = new Intent(SleepClockActivity.this, FunctionActivity.class);
				ElectricFenceBean efb = new ElectricFenceBean();
				efb.setPOSITION(position);
				efb.setOPERATIONFLAG(operationflag);
				efb.setTIMEPARAGRAPH(oldtime.getText().toString()+"-"+newtime.getText().toString());
				List<String> list = new ArrayList<String>();
				for(int i=0; i<7; i++){
					if(date[i] == true){
						list.add(s[i]);
					}
				}
				efb.setDATE(list);
				intent.putExtra("BackSleepClockActivity", efb);
				startActivity(intent);
				finish();
			}
		});
		
		oldtime = (TextView) findViewById(R.id.oldtime);
		oldtime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(SleepClockActivity.this,
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
                    	oldtime.setText(mhourOfDay+":"+mminute);
                    }
                }, 0, 0, true);
                timePickerDialog.show();
			}
		});
		
		newtime = (TextView) findViewById(R.id.newtime);
		newtime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TimePickerDialog timePickerDialog = new TimePickerDialog(SleepClockActivity.this,
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
                    	newtime.setText(mhourOfDay+":"+mminute);
                    }
                }, 0, 0, true);
                timePickerDialog.show();
			}
		});
		
		monday = (TextView) findViewById(R.id.monday);
		monday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(date[0] == true){
					date[0] = false;
					monday.setBackgroundColor(Color.WHITE);
					monday.setTextColor(Color.parseColor("#808080"));
				}else{
					date[0] = true;
					monday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
					monday.setTextColor(Color.WHITE);
				}
			}
		});
		tuesday = (TextView) findViewById(R.id.tuesday);
		tuesday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(date[1] == true){
					date[1] = false;
					tuesday.setBackgroundColor(Color.WHITE);
					tuesday.setTextColor(Color.parseColor("#808080"));
				}else{
					date[1] = true;
					tuesday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
					tuesday.setTextColor(Color.WHITE);
				}
			}
		});
		wednesday = (TextView) findViewById(R.id.wednesday);
		wednesday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(date[2] == true){
					date[2] = false;
					wednesday.setBackgroundColor(Color.WHITE);
					wednesday.setTextColor(Color.parseColor("#808080"));
				}else{
					date[2] = true;
					wednesday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
					wednesday.setTextColor(Color.WHITE);
				}
			}
		});
		thursday = (TextView) findViewById(R.id.thursday);
		thursday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(date[3] == true){
					date[3] = false;
					thursday.setBackgroundColor(Color.WHITE);
					thursday.setTextColor(Color.parseColor("#808080"));
				}else{
					date[3] = true;
					thursday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
					thursday.setTextColor(Color.WHITE);
				}
			}
		});
		friday = (TextView) findViewById(R.id.friday);
		friday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(date[4] == true){
					date[4] = false;
					friday.setBackgroundColor(Color.WHITE);
					friday.setTextColor(Color.parseColor("#808080"));
				}else{
					date[4] = true;
					friday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
					friday.setTextColor(Color.WHITE);
				}
			}
		});
		saturday = (TextView) findViewById(R.id.saturday);
		saturday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(date[5] == true){
					date[5] = false;
					saturday.setBackgroundColor(Color.WHITE);
					saturday.setTextColor(Color.parseColor("#808080"));
				}else{
					date[5] = true;
					saturday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
					saturday.setTextColor(Color.WHITE);
				}
			}
		});
		sunday = (TextView) findViewById(R.id.sunday);
		sunday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(date[6] == true){
					date[6] = false;
					sunday.setBackgroundColor(Color.WHITE);
					sunday.setTextColor(Color.parseColor("#808080"));
				}else{
					date[6] = true;
					sunday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
					sunday.setTextColor(Color.WHITE);
				}
			}
		});
		
		
		Intent intent = getIntent();
		ElectricFenceBean efbj = (ElectricFenceBean) intent.getSerializableExtra("JumpSleepClockActivity");  
		if(efbj != null){
			oldtime.setText(efbj.getTIMEPARAGRAPH().substring(0, 5));
			newtime.setText(efbj.getTIMEPARAGRAPH().substring(6, 11));
			for(int i=0; i<7; i++){
				for(int j=0; j<efbj.getDATE().size(); j++){
					if(efbj.getDATE().get(j).equals(s[i])){
						date[i] = true;
						choise(i);
					}
				}
			}
			position = efbj.getPOSITION();
			operationflag = efbj.isOPERATIONFLAG();
		}
		
		ElectricFenceBean efba = (ElectricFenceBean) intent.getSerializableExtra("AddSleepClockBean");  
		if(efba != null){
			for(int i=0; i<7; i++){
				for(int j=0; j<efba.getDATE().size(); j++){
					if(efba.getDATE().get(j).equals(s[i])){
						date[i] = true;
						choise(i);
					}
				}
			}
			operationflag = efba.isOPERATIONFLAG();
		}
		
	}
	
	
	private void choise(int i){
		switch (i) {
		case 0:
			monday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
			monday.setTextColor(Color.WHITE);
			break;
		case 1:
			tuesday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
			tuesday.setTextColor(Color.WHITE);
			break;
		case 2:
			wednesday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
			wednesday.setTextColor(Color.WHITE);
			break;
		case 3:
			thursday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
			thursday.setTextColor(Color.WHITE);
			break;
		case 4:
			friday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
			friday.setTextColor(Color.WHITE);
			break;
		case 5:
			saturday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
			saturday.setTextColor(Color.WHITE);
			break;
		case 6:
			sunday.setBackground((getResources().getDrawable(R.drawable.desing_date)));
			sunday.setTextColor(Color.WHITE);
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
