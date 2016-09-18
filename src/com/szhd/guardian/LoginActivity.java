package com.szhd.guardian;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.szhd.util.MatchString;
import com.szhd.webservice.Communicate;

public class LoginActivity extends Activity {

	private Handler myhandler;
	private Button applogin;
	private TextView registernow;
	private TextView forgetpassword;
	private EditText username;
	private EditText password;
	private String musername;
	private String mpassword;
	public static LoginActivity instance = null;
	private boolean loginthread = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginactivity);
		init();
		Communicate cc = new Communicate();
		cc.start();
		Communicate.IMSI = getIMSI();
		myhandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 3:
					Toast.makeText(LoginActivity.this, "登录失败，请检查帐号或密码是否正确！",
							Toast.LENGTH_LONG).show();
					break;
				case 4:
					Toast.makeText(LoginActivity.this, "您的SIM卡未注册",
							Toast.LENGTH_LONG).show();
					break;
				case 5:
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(intent);
					Toast.makeText(LoginActivity.this, "登录成功",
							Toast.LENGTH_LONG).show();
					break;
				}
			}
		};

	}

	private String getIMSI() {
		TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String imsi_t = telemamanger.getSubscriberId();
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(imsi_t);
		} else {
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			android.content.ClipData clip = android.content.ClipData
					.newPlainText("Clip", imsi_t);
			clipboard.setPrimaryClip(clip);
		}
		return imsi_t;
	}

	private void init() {
		instance = this;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (loginthread) {
						if (Communicate.STATE == true) {
							if (Communicate.RESULT.get(0).equals("LoginFailed")) {
								Message message = myhandler.obtainMessage();
								message.what = 3;
								myhandler.sendMessage(message);
								// 关闭发送状态
								Communicate.STATE = false;
							} else if (Communicate.RESULT.get(0).equals(
									"NoRegist")) {
								Message message = myhandler.obtainMessage();
								message.what = 4;
								myhandler.sendMessage(message);
								// 关闭发送状态
								Communicate.STATE = false;
							} else if (Communicate.RESULT.get(0).equals(
									"LoginSuccess")) {
								Message message = myhandler.obtainMessage();
								message.what = 5;
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

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		forgetpassword = (TextView) findViewById(R.id.forgetpassword);
		forgetpassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						SetAccountInfo.class);
				startActivity(intent);
			}
		});

		applogin = (Button) findViewById(R.id.applogin);
		applogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				musername = username.getText().toString();
				mpassword = password.getText().toString();

				if ((musername == null || musername.length() == 0)
						&& (mpassword == null || mpassword.length() == 0)) {
					Toast.makeText(LoginActivity.this, "请填写帐号",
							Toast.LENGTH_LONG).show();
				} else {
					if (musername == null || musername.length() == 0) {
						Toast.makeText(LoginActivity.this, "请填写帐号",
								Toast.LENGTH_LONG).show();
					} else if (mpassword == null || mpassword.length() == 0) {
						Toast.makeText(LoginActivity.this, "请填写密码",
								Toast.LENGTH_LONG).show();
					} else {

						Log.e("进入了登录验证通过！", "进入了登录验证通过！+IMSI="
								+ Communicate.IMSI.toString());

						List<String> s = new ArrayList<String>();
						s.add(username.getText().toString());
						s.add(password.getText().toString());
						s.add(Communicate.IMSI.toString());
						List<String> starttags = new ArrayList<String>();
						starttags.add("<username>");
						starttags.add("<pass>");
						starttags.add("<phonenum>");
						Communicate.sss.add("<UserLoginResult>");
						Communicate.sss.add("</UserLoginResult>");
						Communicate.xmlclassdata = MatchString.addString(
								LoginActivity.this, "UserLogin.xml", s,
								starttags);
						Communicate.mark = true;

					}
				}
			}
		});

		registernow = (TextView) findViewById(R.id.registernow);
		registernow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 跳转至注册界面
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});

		// new Thread() {
		// @Override
		// public void run() {
		// try {
		// new ImgHttpClient(LoginActivity.this)
		// .downForm("20160829020513.jpg");// 调用Post上传请求
		// } catch (IOException e) {
		// Log.e("Io异常：", e.getMessage());
		// e.printStackTrace();
		// }
		// }
		// }.start();

	}

	protected void onDestroy() {
		loginthread = false;
		super.onDestroy();
	}

}
