package com.szhd.guardian;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
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

import com.szhd.ui.RoundImageView;
import com.szhd.util.ImageTools;
import com.szhd.util.MatchString;
import com.szhd.util.MyApplication;
import com.szhd.webservice.Communicate;

public class RegisterActivity extends Activity {

	private EditText reusername;
	private EditText repassword;
	private EditText repasswordagin;
	private EditText regname;
	private EditText regaccount;
	private EditText deviceid;
	private Spinner mynumber;
	private static final String AVATAR_NAME = "user_img_";
	private TextView textView2;
	private ImageView regback;
	private Button next;
	private RoundImageView reimageview;
	private String[] s = new String[] { "0" };
	private static final int DEVICE_NOT_EXISTENCE = 50;
	private static final int PHONENUM_EXISTENCE = 51;
	private static final int USERNAME_EXISTENCE = 52;
	private static final int DEVICE_EXISTENCE = 53;
	private String[] function = new String[] { "拍照", "从相册选择" };
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int SCALE = 5;
	private int CHOISESATAE;
	private boolean loginthread = true;
	private Handler myhandler;
	private Bitmap avatarBitmap = null;
	private static final String JPG = ".jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		init();
	}

	private void init() {
		s[0] = Communicate.IMSI;
		reusername = (EditText) findViewById(R.id.reusername);
		repassword = (EditText) findViewById(R.id.repassword);
		repasswordagin = (EditText) findViewById(R.id.repasswordagin);
		regname = (EditText) findViewById(R.id.regname);
		regaccount = (EditText) findViewById(R.id.regaccount);

		deviceid = (EditText) findViewById(R.id.deviceid);
		mynumber = (Spinner) findViewById(R.id.mynumber);
		regback = (ImageView) findViewById(R.id.regback);
		next = (Button) findViewById(R.id.registernext);
		reimageview = (RoundImageView) findViewById(R.id.reimageview);

		myhandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DEVICE_NOT_EXISTENCE:
					Toast.makeText(RegisterActivity.this, "无此设备ID",
							Toast.LENGTH_LONG).show();
					break;
				case PHONENUM_EXISTENCE:
					Toast.makeText(RegisterActivity.this, "您的SIM卡已经注册",
							Toast.LENGTH_LONG).show();
					break;
				case DEVICE_EXISTENCE:
					Toast.makeText(RegisterActivity.this, "设备ID已被注册",
							Toast.LENGTH_LONG).show();
					break;
				case USERNAME_EXISTENCE:
					Toast.makeText(RegisterActivity.this, "账号已被注册，请重新输入账号",
							Toast.LENGTH_LONG).show();
					break;
				case 0:
					Toast.makeText(RegisterActivity.this, "注册失败",
							Toast.LENGTH_LONG).show();
					break;
				case 2:
					Intent intent = new Intent(RegisterActivity.this,
							RegisterQuActivity.class);
					intent.putExtra("tag", "");
					intent.putExtra("imageurl", "");
					intent.putExtra("reusername", reusername.getText()
							.toString());
					intent.putExtra("repassword", repassword.getText()
							.toString());
					intent.putExtra("regname", regname.getText().toString());
					intent.putExtra("regaccount", regaccount.getText()
							.toString());
					intent.putExtra("mynumber", s[0]);
					intent.putExtra("deviceid", deviceid.getText().toString());

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
							if (Communicate.RESULT.get(0).equals("RegisterYES")) {
								myhandler.sendEmptyMessage(2);
								Log.e("验证通过", "验证通过");
								Communicate.STATE = false;
							}
							if (Communicate.RESULT.get(0).equals(
									"DeviceNotExistence")) {
								myhandler
										.sendEmptyMessage(DEVICE_NOT_EXISTENCE);
								Communicate.STATE = false;
							}
							if (Communicate.RESULT.get(0)
									.equals("SimExistence")) {
								myhandler.sendEmptyMessage(PHONENUM_EXISTENCE);
								Communicate.STATE = false;
							}
							if (Communicate.RESULT.get(0).equals(
									"SetSecurePathOK")) {
								Log.e("发送安全路径成功", "发送安全路径成功");
								Communicate.STATE = false;
							}
							if (Communicate.RESULT.get(0).equals(
									"UserNameExistence")) {
								myhandler.sendEmptyMessage(USERNAME_EXISTENCE);
								Communicate.STATE = false;
							}
							if (Communicate.RESULT.get(0).equals(
									"DevicedExistence")) {
								myhandler.sendEmptyMessage(DEVICE_EXISTENCE);
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

		reimageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(RegisterActivity.this).setItems(
						function, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case TAKE_PICTURE:
									CHOISESATAE = 1;
									Intent openCameraIntent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									Uri imageUri = Uri.fromFile(new File(
											Environment
													.getExternalStorageDirectory(),
											"image.jpg"));
									openCameraIntent.putExtra(
											MediaStore.EXTRA_OUTPUT, imageUri);
									startActivityForResult(openCameraIntent,
											TAKE_PICTURE);
									break;
								case CHOOSE_PICTURE:
									CHOISESATAE = 1;
									Intent openAlbumIntent = new Intent(
											Intent.ACTION_GET_CONTENT);
									openAlbumIntent.setType("image/*");
									startActivityForResult(openAlbumIntent,
											CHOOSE_PICTURE);
									break;

								default:
									break;
								}
							}
						}).show();
			}
		});

		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (reusername.getText().toString() != null
						&& reusername.getText().toString().length() != 0) {
					if (repassword.getText().toString() != null
							&& repassword.getText().toString().length() != 0) {
						if (repasswordagin.getText().toString() != null
								&& repasswordagin.getText().toString().length() != 0) {
							if (regname.getText().toString() != null
									&& regname.getText().toString().length() != 0) {
								if (regaccount.getText().toString() != null
										&& regaccount.getText().toString()
												.length() != 0) {
									if (deviceid.getText().toString() != null
											&& deviceid.getText().toString()
													.length() != 0) {
										if (repassword
												.getText()
												.toString()
												.equals(repasswordagin
														.getText().toString())) {
											if (!RegularUserName(reusername
													.getText().toString())) {
												reusername.requestFocus();
												reusername
														.setError("请输入正确的账号格式(最少6位，不能有特殊符号,必须以字母开头)");
												return;
											}
											if (avatarBitmap != null) {
												MyApplication.device = deviceid
														.getText().toString();
												try {
													saveFile(avatarBitmap);
												} catch (IOException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}
											}

											// JSONObject ja = new JSONObject();
											// String add = "A|";
											// try {
											// ja.put("imageurl", AVATAR_NAME
											// + deviceid.getText()
											// .toString());
											// ja.put("username", reusername
											// .getText().toString());
											// ja.put("password", repassword
											// .getText().toString());
											//
											// ja.put("name", regname
											// .getText().toString());
											// ja.put("relationship",
											// regaccount.getText()
											// .toString());
											// ja.put("device_no", deviceid
											// .getText().toString());
											// ja.put("SIMnum", mynumber
											// .getItemAtPosition(0)
											// .toString());
											//
											// } catch (JSONException e) { //
											// TODO
											// // Auto-generated
											// // catch
											// // //
											// // block
											// e.printStackTrace();
											// }
											//
											// List<String> s = new
											// ArrayList<String>();
											// s.add(deviceid.getText().toString());
											// s.add(add + ja.toString());
											// Log.e("main", ja.toString());
											//
											// List<String> starttags = new
											// ArrayList<String>();
											// starttags.add("<devicenum>");
											// starttags.add("<content>");
											//
											// Communicate.sss
											// .add("<DealUserResult>");
											// Communicate.sss
											// .add("</DealUserResult>");
											//
											// Communicate.xmlclassdata =
											// MatchString
											// .addString(
											// RegisterActivity.this,
											// "DealUser.xml", s,
											// starttags);
											// Communicate.mark = true;

											List<String> s2 = new ArrayList<String>();
											JSONObject ja = new JSONObject();
											try {
												ja.put("username", reusername
														.getText().toString());
												ja.put("SIMnum",
														Communicate.IMSI);
												ja.put("device_no", deviceid
														.getText().toString());
												s2.add(ja.toString());
												List<String> starttags2 = new ArrayList<String>();
												starttags2.add("<content>");
												Communicate.sss
														.add("<QueryRegisterResult>");
												Communicate.sss
														.add("</QueryRegisterResult>");
												Communicate.xmlclassdata = MatchString
														.addString(
																RegisterActivity.this,
																"QueryRegister.xml",
																s2, starttags2);
												Communicate.mark = true;
											} catch (JSONException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

										} else {

											repasswordagin.setError("密码不一致");
											repasswordagin.requestFocus();
										}
									} else {
										deviceid.setError("设备ID不能为空");
										deviceid.requestFocus();

									}
								} else {

									regaccount.setError("与使用人关系不能为空");
									regaccount.requestFocus();

								}
							} else {
								regname.setError("姓名不能为空");
								regname.requestFocus();
							}
						} else {
							repasswordagin.setError("确认密码不能为空");
							repasswordagin.requestFocus();

						}
					} else {
						repassword.setError("0密0码不能为空");
						repassword.requestFocus();
					}
				} else {
					reusername.setError("帐号不能为空");
					reusername.requestFocus();

				}
			}

			private boolean RegularUserName(String string) {
				// TODO Auto-generated method stub
				return string.matches("^[a-zA-Z_][a-zA-Z0-9_]{5,16}$");

			}
		});

		regback.setOnClickListener(new OnClickListener() {
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

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, s);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mynumber.setAdapter(adapter);
		mynumber.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				Bitmap bitmap = BitmapFactory.decodeFile(Environment
						.getExternalStorageDirectory() + "/image.jpg");
				avatarBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth()
						/ SCALE, bitmap.getHeight() / SCALE);
				bitmap.recycle();
				switch (CHOISESATAE) {
				case 0:

					break;
				case 1:
					reimageview.setImageBitmap(avatarBitmap);
					break;
				}
				ImageTools.savePhotoToSDCard(avatarBitmap, Environment
						.getExternalStorageDirectory().getAbsolutePath(),
						String.valueOf(System.currentTimeMillis()));
				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = getContentResolver();
				Uri originalUri = data.getData();
				try {
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);
					if (photo != null) {
						avatarBitmap = ImageTools.zoomBitmap(photo,
								photo.getWidth() / SCALE, photo.getHeight()
										/ SCALE);
						photo.recycle();
						switch (CHOISESATAE) {
						case 0:

							break;
						case 1:
							reimageview.setImageBitmap(avatarBitmap);
							break;
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public File saveFile(Bitmap bm) throws IOException {
		String deviced = MyApplication.device + "";
		MyApplication.avatarPath = AVATAR_NAME + deviced + JPG;
		Log.e("进入了保存头像图片方法", "进入了头像保存图片方法");
		File myCaptureFile = new File(getExternalCacheDir(), AVATAR_NAME
				+ deviced + JPG);
		BufferedOutputStream fos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 50, fos);
		fos.flush();
		fos.close();
		return myCaptureFile;

	}

}
