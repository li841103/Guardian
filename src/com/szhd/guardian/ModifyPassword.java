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

public class ModifyPassword extends Activity {

	private ImageView modback;
	private Button repasswfinish;
	private TextView textView2;
	private EditText oldpassw;
	private EditText newpassw;
	private EditText spassw;

	private Handler myhandler;
	private boolean loginthread = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modifypassword);
		init();
	}

	private void init() {
		newpassw = (EditText) findViewById(R.id.newpassw);
		spassw = (EditText) findViewById(R.id.spassw);
		oldpassw = (EditText) findViewById(R.id.oldpassw);

		modback = (ImageView) findViewById(R.id.modback);
		modback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		myhandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 10:
					Toast.makeText(ModifyPassword.this, "密码修改成功,请重新登录您的账号！",
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent();
					intent.setClass(ModifyPassword.this, LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
								message.what = 10;
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

		repasswfinish = (Button) findViewById(R.id.repasswfinish);
		repasswfinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ��֤������
				if (oldpassw.getText().toString() != null
						&& oldpassw.getText().toString().length() != 0) {
					if (oldpassw.getText().toString()
							.equals(getIntent().getStringExtra("password"))) {
						if (newpassw.getText().toString() != null
								&& newpassw.getText().toString().length() != 0) {
							if (spassw.getText().toString() != null
									&& spassw.getText().toString().length() != 0) {
								if (newpassw.getText().toString()
										.equals(spassw.getText().toString())) {

									List<String> s = new ArrayList<String>();
									s.add(getIntent()
											.getStringExtra("username"));
									s.add(Communicate.IMSI);
									s.add(newpassw.getText().toString());

									List<String> starttags = new ArrayList<String>();
									starttags.add("<username>");
									starttags.add("<phonenum>");
									starttags.add("<newpass>");

									Communicate.sss.add("<UpdatePassResult>");
									Communicate.sss.add("</UpdatePassResult>");
									Communicate.xmlclassdata = MatchString
											.addString(ModifyPassword.this,
													"UpdatePass.xml", s,
													starttags);
									Communicate.mark = true;

								} else {
									Toast.makeText(ModifyPassword.this,
											"两次密码不一致", Toast.LENGTH_LONG)
											.show();
								}
							} else {
								Toast.makeText(ModifyPassword.this, "确认密码不能为空",
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(ModifyPassword.this, "密码不能为空",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(ModifyPassword.this, "密码输入错误",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(ModifyPassword.this, "密码不能为空",
							Toast.LENGTH_LONG).show();
				}
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
