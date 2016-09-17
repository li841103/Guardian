package com.szhd.guardian;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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

import com.szhd.server.ImgHttpClient;
import com.szhd.util.MyApplication;
import com.szhd.webservice.Communicate;
import com.szhd.webservice.MatchString;

public class SetSecurityActivity extends Activity {

	private Button requfinish;
	private String tag;
	private ImageView regquback;
	private TextView textView8;

	private TextView requone;
	private TextView requthree;
	private TextView requfive;

	private EditText requtwo;
	private EditText requfour;
	private EditText requsix;

	private Handler myhandler;
	private boolean loginthread = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setsecurity);
		init();
	}

	private void init() {
		myhandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					Toast.makeText(SetSecurityActivity.this, "无此设备ID",
							Toast.LENGTH_LONG).show();
					break;
				case 1:
					Toast.makeText(SetSecurityActivity.this, "注册成功",
							Toast.LENGTH_LONG).show();
					Intent intent1 = new Intent(SetSecurityActivity.this,
							LoginActivity.class);
					startActivity(intent1);
					break;
				case 2:
					Toast.makeText(SetSecurityActivity.this, "您的SIM卡已经注册",
							Toast.LENGTH_LONG).show();
					break;
				case 11:
					Toast.makeText(SetSecurityActivity.this, "密保修改成功",
							Toast.LENGTH_LONG).show();

					Intent intent2 = new Intent(SetSecurityActivity.this,
							AccountActivity.class);
					startActivity(intent2);

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
							if (Communicate.RESULT.get(0)
									.equals("RegistFailed")) {
								Message message = myhandler.obtainMessage();
								message.what = 0;
								myhandler.sendMessage(message);
								// 关闭发送状态
								Communicate.STATE = false;
							} else if (Communicate.RESULT.get(0).equals(
									"RegistSuccess")) {
								Message message = myhandler.obtainMessage();
								message.what = 1;
								myhandler.sendMessage(message);
								// 关闭发送状态
								Communicate.STATE = false;
							} else if (Communicate.RESULT.get(0).equals(
									"RegistAllready")) {
								Message message = myhandler.obtainMessage();
								message.what = 2;
								myhandler.sendMessage(message);
								// 关闭发送状态
								Communicate.STATE = false;
							} else if (Communicate.RESULT.get(0).equals(
									"ModifySecurOK")) {
								Message message = myhandler.obtainMessage();
								message.what = 11;
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

		requone = (TextView) findViewById(R.id.requone);
		requthree = (TextView) findViewById(R.id.requthree);
		requfive = (TextView) findViewById(R.id.requfive);

		requtwo = (EditText) findViewById(R.id.requtwo);
		requfour = (EditText) findViewById(R.id.requfour);
		requsix = (EditText) findViewById(R.id.requsix);

		Intent intent = getIntent();
		tag = intent.getStringExtra("classname");
		requone.setText(intent.getStringExtra("question1"));
		requthree.setText(intent.getStringExtra("question2"));
		requfive.setText(intent.getStringExtra("question3"));

		requfinish = (Button) findViewById(R.id.requfinish);
		if (tag.equals("SetAccountInfo")) {
			requfinish.setText("下一步");
		} else if (tag.equals("RegisterQuActivity")) {
			requfinish.setText("注册");
		}

		requfinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tag.equals("SetAccountInfo")) {

					if (requtwo.getText().toString() != null
							&& requtwo.getText().toString().length() != 0) {
						if (requfour.getText().toString() != null
								&& requfour.getText().toString().length() != 0) {
							if (requsix.getText().toString() != null
									&& requsix.getText().toString().length() != 0) {
								if (getIntent().getStringExtra("answer1")// 用答案1对比输入答案1
										.equals(requtwo.getText().toString())) {
									if (getIntent().getStringExtra("answer2")
											.equals(requfour.getText()
													.toString())) {
										if (getIntent().getStringExtra(
												"answer3").equals(
												requsix.getText().toString())) {

											Intent intent = new Intent(
													SetSecurityActivity.this,
													SecurityVerification.class);
											intent.putExtra(
													"username",
													getIntent().getStringExtra(
															"username"));// 吧账号发过去
											intent.putExtra(
													"phonenum",
													getIntent().getStringExtra(
															"phonenum"));// 把Imsi发过去
											startActivity(intent);

										} else {
											Toast.makeText(
													SetSecurityActivity.this,
													"密保答案不正确",
													Toast.LENGTH_LONG).show();
										}
									} else {
										Toast.makeText(
												SetSecurityActivity.this,
												"密保答案不正确", Toast.LENGTH_LONG)
												.show();
									}
								} else {
									Toast.makeText(SetSecurityActivity.this,
											"密保答案不正确", Toast.LENGTH_LONG)
											.show();
								}
							} else {
								Toast.makeText(SetSecurityActivity.this,
										"答案不能为空", Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(SetSecurityActivity.this, "答案不能为空",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(SetSecurityActivity.this, "答案不能为空",
								Toast.LENGTH_LONG).show();
					}

				} else if (tag.equals("RegisterQuActivity")) {

					if (requtwo.getText().toString() != null
							&& requtwo.getText().toString().length() != 0) {
						if (requfour.getText().toString() != null
								&& requfour.getText().toString().length() != 0) {
							if (requsix.getText().toString() != null
									&& requsix.getText().toString().length() != 0) {
								Intent intent = getIntent();
								if (requtwo
										.getText()
										.toString()
										.equals(intent
												.getStringExtra("answer1"))) {
									if (requfour
											.getText()
											.toString()
											.equals(intent
													.getStringExtra("answer2"))) {
										if (requsix
												.getText()
												.toString()
												.equals(intent
														.getStringExtra("answer3"))) {

											String add = "A|";
											JSONObject ja = new JSONObject();
											try {

												new Thread() {
													@Override
													public void run() {
														File file = new File(
																getExternalCacheDir(),
																MyApplication.avatarPath);
														if (file.exists()) {// 如果本地存在此图片
																			// 表示此次注册带头像
																			// 需要上传头像图
															try {
																new ImgHttpClient(
																		SetSecurityActivity.this)
																		.uploadForm(
																				null,
																				"a",
																				file,
																				MyApplication.avatarPath);
															} catch (IOException e) {
																// TODO
																// Auto-generated
																// catch block
																e.printStackTrace();
															}
														}
													}
												}.start();
												ja.put("imageurl",
														intent.getStringExtra("imageurl"));
												ja.put("username",
														intent.getStringExtra("reusername"));
												ja.put("password",
														intent.getStringExtra("repassword"));
												ja.put("name",
														intent.getStringExtra("regname"));
												ja.put("relationship",
														intent.getStringExtra("regaccount"));
												ja.put("phonenum",
														intent.getStringExtra("mynumber"));
												ja.put("device_no",
														intent.getStringExtra("deviceid"));
												ja.put("SIMnum",
														Communicate.IMSI);
												ja.put("first_Guardian", "true");
												ja.put("question1",
														intent.getStringExtra("question1"));
												ja.put("question2",
														intent.getStringExtra("question2"));
												ja.put("question3",
														intent.getStringExtra("question3"));
												ja.put("security1",
														intent.getStringExtra("answer1"));
												ja.put("security2",
														intent.getStringExtra("answer2"));
												ja.put("security3",
														intent.getStringExtra("answer3"));
											} catch (JSONException e) {
												e.printStackTrace();
											}

											List<String> s = new ArrayList<String>();
											s.add(intent
													.getStringExtra("deviceid"));
											s.add(add + ja.toString());

											List<String> starttags = new ArrayList<String>();
											starttags.add("<devicenum>");
											starttags.add("<content>");

											Communicate.sss
													.add("<DealUserResult>");
											Communicate.sss
													.add("</DealUserResult>");

											Communicate.xmlclassdata = MatchString
													.addString(
															SetSecurityActivity.this,
															"DealUser.xml", s,
															starttags);
											Communicate.mark = true;

										} else {
											Toast.makeText(
													SetSecurityActivity.this,
													"答案不正确", Toast.LENGTH_LONG)
													.show();
										}
									} else {
										Toast.makeText(
												SetSecurityActivity.this,
												"答案不正确", Toast.LENGTH_LONG)
												.show();
									}
								} else {
									Toast.makeText(SetSecurityActivity.this,
											"答案不正确", Toast.LENGTH_LONG).show();
								}
							} else {
								Toast.makeText(SetSecurityActivity.this,
										"答案不能为空", Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(SetSecurityActivity.this, "答案不能为空",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(SetSecurityActivity.this, "答案不能为空",
								Toast.LENGTH_LONG).show();
					}
				} else if (tag.equals("AccountActivity")) {

					// /////////////////////////////////////////

					if (requtwo.getText().toString().length() != 0
							&& requtwo.getText() != null) {
						if (requfour.getText().toString() != null
								&& requfour.getText().toString().length() != 0) {
							if (requsix.getText().toString() != null
									&& requsix.getText().toString().length() != 0) {
								// 非空全部验证成功
								if (getIntent().getStringExtra("answer1")
										.equals(requtwo.getText().toString())) {
									// 问题1验证相同
									if (getIntent().getStringExtra("answer2")
											.equals(requfour.getText()
													.toString())) {
										// 问题2验证相同
										if (getIntent().getStringExtra(
												"answer3").equals(
												requsix.getText().toString())) {
											// 全部验证通过

											Intent intent = getIntent();
											// 修改密保
											List<String> s = new ArrayList<String>();
											s.add(intent.getStringExtra("id"));
											s.add(intent
													.getStringExtra("question1"));
											s.add(intent
													.getStringExtra("question2"));
											s.add(intent
													.getStringExtra("question3"));
											s.add(intent
													.getStringExtra("answer1"));
											s.add(intent
													.getStringExtra("answer2"));
											s.add(intent
													.getStringExtra("answer3"));

											List<String> starttags = new ArrayList<String>();
											starttags.add("<id>");
											starttags.add("<question1>");
											starttags.add("<question2>");
											starttags.add("<question3>");
											starttags.add("<answer1>");
											starttags.add("<answer2>");
											starttags.add("<answer3>");

											Communicate.sss
													.add("<ModifySecurResult>");
											Communicate.sss
													.add("</ModifySecurResult>");

											Communicate.xmlclassdata = MatchString
													.addString(
															SetSecurityActivity.this,
															"ModifySecur.xml",
															s, starttags);
											Communicate.mark = true;
										} else {
											Toast.makeText(
													SetSecurityActivity.this,
													"密保验证不一致",
													Toast.LENGTH_LONG).show();
											return;
										}
									} else {
										Toast.makeText(
												SetSecurityActivity.this,
												"密保验证不一致", Toast.LENGTH_LONG)
												.show();
										return;
									}
								} else {
									Toast.makeText(SetSecurityActivity.this,
											"密保验证不一致", Toast.LENGTH_LONG)
											.show();
									return;
								}

							} else {
								Toast.makeText(SetSecurityActivity.this,
										"验证密保不能为空！", Toast.LENGTH_LONG).show();
								return;
							}
						} else {
							Toast.makeText(SetSecurityActivity.this,
									"验证密保不能为空！", Toast.LENGTH_LONG).show();
							return;
						}
					} else {
						Toast.makeText(SetSecurityActivity.this, "验证密保不能为空！",
								Toast.LENGTH_LONG).show();
						return;
					}

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
		finish();
	}

	protected void onDestroy() {
		loginthread = false;
		super.onDestroy();
	}

}
