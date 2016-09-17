package com.szhd.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import com.szhd.guardian.R;

public class Alert extends Activity{

	private EditText alert;
	public static Alert instance = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert);
		instance = this;
		alert = (EditText) findViewById(R.id.alert);
		Intent intent = getIntent(); //用于激活它的意图对象
		String lujing = intent.getStringExtra("lujing");
		alert.setText(lujing);
	}
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		instance.finish();
	}
	
	
}
