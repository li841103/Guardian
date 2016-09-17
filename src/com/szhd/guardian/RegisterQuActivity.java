package com.szhd.guardian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterQuActivity extends Activity {

	private Button requfinish;
	private ImageView regquback;
	private TextView textView8;

	private Spinner q1;
	private Spinner q2;
	private Spinner q3;

	private EditText answer1;
	private EditText answer2;
	private EditText answer3;

	private String choicequeson1;
	private String choicequeson2;
	private String choicequeson3;

	private String[] s = new String[] { "您父亲的姓名是？", "您的姓名是？", "您的宠物是？",
			"您最喜欢的颜色是？", "您的故居是？", "您爱人的姓名是？" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerqu);
		init();
	}

	private void init() {
		q1 = (Spinner) findViewById(R.id.requonechoicesp);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, s);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		q1.setAdapter(adapter1);
		q1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				choicequeson1 = s[pos];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		q2 = (Spinner) findViewById(R.id.requthreechoicesp);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, s);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		q2.setAdapter(adapter2);
		q2.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				choicequeson2 = s[pos];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		q3 = (Spinner) findViewById(R.id.requfivechoicesp);
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, s);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		q3.setAdapter(adapter3);
		q3.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				choicequeson3 = s[pos];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		answer1 = (EditText) findViewById(R.id.requtwo);
		answer2 = (EditText) findViewById(R.id.requfour);
		answer3 = (EditText) findViewById(R.id.requsix);

		requfinish = (Button) findViewById(R.id.requfinish);
		requfinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String tag = getIntent().getStringExtra("tag");
				if (answer1.getText().toString() != null
						&& answer1.getText().toString().length() != 0) {
					if (answer2.getText().toString() != null
							&& answer2.getText().toString().length() != 0) {
						if (answer3.getText().toString() != null
								&& answer3.getText().toString().length() != 0) {
							if (tag.equals("AccountActivity")) {
								Intent intent1 = new Intent(
										RegisterQuActivity.this,
										SetSecurityActivity.class);
								intent1.putExtra("classname", "AccountActivity");
								intent1.putExtra("id", getIntent()
										.getStringExtra("id"));
								intent1.putExtra("question1", choicequeson1);
								intent1.putExtra("question2", choicequeson2);
								intent1.putExtra("question3", choicequeson3);
								intent1.putExtra("answer1", answer1.getText()
										.toString());
								intent1.putExtra("answer2", answer2.getText()
										.toString());
								intent1.putExtra("answer3", answer3.getText()
										.toString());
								startActivity(intent1);
							} else {
								Intent intent2 = new Intent(
										RegisterQuActivity.this,
										SetSecurityActivity.class);
								intent2.putExtra("imageurl", getIntent()
										.getStringExtra("imageurl"));
								intent2.putExtra("reusername", getIntent()
										.getStringExtra("reusername"));
								intent2.putExtra("repassword", getIntent()
										.getStringExtra("repassword"));
								intent2.putExtra("regname", getIntent()
										.getStringExtra("regname"));
								intent2.putExtra("regaccount", getIntent()
										.getStringExtra("regaccount"));
								intent2.putExtra("mynumber", getIntent()
										.getStringExtra("mynumber"));
								intent2.putExtra("deviceid", getIntent()
										.getStringExtra("deviceid"));
								intent2.putExtra("classname",
										"RegisterQuActivity");
								intent2.putExtra("question1", choicequeson1);
								intent2.putExtra("question2", choicequeson2);
								intent2.putExtra("question3", choicequeson3);
								intent2.putExtra("answer1", answer1.getText()
										.toString());
								intent2.putExtra("answer2", answer2.getText()
										.toString());
								intent2.putExtra("answer3", answer3.getText()
										.toString());
								startActivity(intent2);
							}

						} else {
							Toast.makeText(RegisterQuActivity.this, "密保不能为空！",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(RegisterQuActivity.this, "密保不能为空！",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(RegisterQuActivity.this, "密保不能为空！",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		regquback = (ImageView) findViewById(R.id.regquback);
		regquback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		textView8 = (TextView) findViewById(R.id.textView8);
		textView8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
