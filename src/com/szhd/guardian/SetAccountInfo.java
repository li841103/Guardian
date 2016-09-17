package com.szhd.guardian;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.szhd.util.MatchString;
import com.szhd.webservice.Communicate;

public class SetAccountInfo extends Activity {

	private Button requfinish;
	private ImageView regquback;
	private TextView textView4;
	private Spinner requtwo;
	private String[] s = new String[] { "0" };

	private EditText requone;
	private Handler myhandler;
	private boolean loginthread = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setaccountinfo);
		init();
		myhandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 6:
					List<String> l = new ArrayList<String>();
					l = (List<String>) msg.obj;
					Intent intent = new Intent(SetAccountInfo.this,
							SetSecurityActivity.class);
					intent.putExtra("classname", "SetAccountInfo");// 表明自己是修改密保的身份进入填写密保页面的
					intent.putExtra("answer1", l.get(0));
					intent.putExtra("answer2", l.get(1));
					intent.putExtra("answer3", l.get(2));
					intent.putExtra("question1", l.get(4));
					intent.putExtra("question2", l.get(5));
					intent.putExtra("question3", l.get(6));
					// intent.putExtra("id", id);
					intent.putExtra("username", requone.getText().toString());// 把输入的账号发过去
					intent.putExtra("phonenum", Communicate.IMSI);// 把Imsi号码发过去
					startActivity(intent);
					break;
				case 7:
					Toast.makeText(SetAccountInfo.this, "帐号或本机号码未注册",
							Toast.LENGTH_LONG).show();
					break;
				}
			}
		};

	}

	private void init() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (loginthread) {
						if (Communicate.STATE == true) {
							if (Communicate.RESULT.get(0).equals("999999999")) {// 状态码
								List<String> list = new ArrayList<String>();
								list.add(Communicate.RESULT.get(1));// 密保答案1
																	// list[0]
								list.add(Communicate.RESULT.get(2));// 密保答案2
								list.add(Communicate.RESULT.get(3));// 密保答案3
								list.add(Communicate.RESULT.get(4));// 用户id
								list.add(Communicate.RESULT.get(5));// 密保问题1
								list.add(Communicate.RESULT.get(6));// 密保问题2
								list.add(Communicate.RESULT.get(7));// 密保问题3
								Message message = myhandler.obtainMessage();
								message.obj = list;
								message.what = 6;
								myhandler.sendMessage(message);
								// 关闭发送状态
								Communicate.STATE = false;
							} else if (Communicate.RESULT.get(0).equals("0")) {
								Message message = myhandler.obtainMessage();
								message.what = 7;
								myhandler.sendMessage(message);
								// 关闭发送状态
								Communicate.STATE = false;
							}

						}
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		requone = (EditText) findViewById(R.id.requone);

		s[0] = Communicate.IMSI;
		requfinish = (Button) findViewById(R.id.requfinish);
		requfinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (requone.getText().toString() != null
						&& requone.getText().toString().length() != 0) {
					// 向服务器发送帐号和注册的手机号码，获取密保问题和答案
					List<String> sc = new ArrayList<String>();
					sc.add(requone.getText().toString());
					sc.add(s[0]);

					List<String> starttags = new ArrayList<String>();
					starttags.add("<username>");
					starttags.add("<phonenum>");

					Communicate.sss.add("<id>");
					Communicate.sss.add("</id>");
					Communicate.sss.add("<answer1>");
					Communicate.sss.add("</answer1>");
					Communicate.sss.add("<answer2>");
					Communicate.sss.add("</answer2>");
					Communicate.sss.add("<answer3>");
					Communicate.sss.add("</answer3>");
					Communicate.sss.add("<user_id>");
					Communicate.sss.add("</user_id>");
					Communicate.sss.add("<question1>");
					Communicate.sss.add("</question1>");
					Communicate.sss.add("<question2>");
					Communicate.sss.add("</question2>");
					Communicate.sss.add("<question3>");
					Communicate.sss.add("</question3>");
					Communicate.xmlclassdata = MatchString.addString(
							SetAccountInfo.this, "QueryQuestion.xml", sc,
							starttags);
					Communicate.mark = true;

				} else {
					Toast.makeText(SetAccountInfo.this, "帐号不能为空",
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

		textView4 = (TextView) findViewById(R.id.textView4);
		textView4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		requtwo = (Spinner) findViewById(R.id.requtwo);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, s);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		requtwo.setAdapter(adapter);
		requtwo.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	protected void onDestroy() {
		loginthread = false;
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

}
