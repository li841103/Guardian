package com.szhd.guardian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedBack extends Activity{

	private ImageView  button2;
	private Button  button1;
	private TextView textView8;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		init();
	}

	private void init() {
		textView8 = (TextView) findViewById(R.id.textView8);
		textView8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		
		button2 = (ImageView) findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(FeedBack.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	
	
	
	
}
