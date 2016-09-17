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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.szhd.webservice.Communicate;
import com.szhd.webservice.MatchString;

public class SecurityVerification extends Activity {

	private Button bt1;
	private ImageView modback;
	private TextView textView2;

	private EditText oldpassw;
	private EditText newpassw;

	private Handler myhandler;
	private boolean loginthread = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.securityverification);
		init();
	}

	private void init() {

		myhandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 6:
					Toast.makeText(SecurityVerification.this, "修改成功，请重新登录账号！",
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent(SecurityVerification.this,
							LoginActivity.class);
					startActivity(intent);
					break;
				}
			}
		};

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (loginthread) {
						if (Communicate.STATE == true) {
							if (Communicate.RESULT.get(0).equals(
									"ModifySuccess")) {
								Message message = myhandler.obtainMessage();
								message.what = 6;
								myhandler.sendMessage(message);
								// �رշ���״̬
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

		oldpassw = (EditText) findViewById(R.id.oldpassw);
		newpassw = (EditText) findViewById(R.id.newpassw);

		bt1 = (Button) findViewById(R.id.repasswfinish);
		bt1.setOnClickListener(new OnClickListener() {
			// 点击确认修改密码
			@Override
			public void onClick(View v) {

				if (oldpassw.getText().toString() != null
						&& oldpassw.getText().toString().length() != 0) {
					if (newpassw.getText().toString() != null
							&& newpassw.getText().toString().length() != 0) {
						if (oldpassw.getText().toString()
								.equals(newpassw.getText().toString())) {

							List<String> s = new ArrayList<String>();
							s.add(getIntent().getStringExtra("username"));// 将账号存入
							s.add(getIntent().getStringExtra("phonenum"));// 将imsi号存入
							s.add(newpassw.getText().toString());// 将新密码存入

							List<String> starttags = new ArrayList<String>();
							starttags.add("<username>");
							starttags.add("<phonenum>");
							starttags.add("<newpass>");

							Communicate.sss.add("<UpdatePassResult>");
							Communicate.sss.add("</UpdatePassResult>");
							Communicate.xmlclassdata = MatchString.addString(
									SecurityVerification.this,
									"UpdatePass.xml", s, starttags);
							Communicate.mark = true;

						} else {
							Toast.makeText(SecurityVerification.this,
									"两次密码不一致，请重新输入", Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(SecurityVerification.this, "确认密码不能为空",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(SecurityVerification.this, "密码不能为空",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		modback = (ImageView) findViewById(R.id.modback);
		modback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		textView2 = (TextView) findViewById(R.id.textView2);
		textView2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	protected void onDestroy() {
		loginthread = false;
		super.onDestroy();
	}

}
