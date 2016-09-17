package com.szhd.guardian;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szhd.server.ImgHttpClient;
import com.szhd.ui.RoundImageView;
import com.szhd.util.ImageTools;
import com.szhd.util.MyApplication;
import com.szhd.webservice.Communicate;
import com.szhd.webservice.MatchString;

public class AccountActivity extends Activity {

	private ImageView regquback;
	private Button anext;
	private Button next;
	private String[] ch = new String[] { "修改密码", "修改密保" };
	private String[] function = new String[] { "拍照", "从相册选择" };
	private boolean init = false;

	private TextView reusername;
	private TextView reusername1;
	private EditText repassword;
	private TextView repassword1;
	private TextView repasswordagin;
	private TextView repasswordagin1;
	private TextView sbid;
	private TextView sbid1;
	private EditText relationship;
	private TextView relationship1;
	private TextView textView7;
	private RelativeLayout reone;
	private RelativeLayout retwo;
	private RoundImageView reimageview;
	private static final String AVATAR_NAME = "user_img_";
	private static final String msave = "b";
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int SCALE = 5;
	private int CHOISESATAE;
	private boolean loginthread = true;
	private Handler myhandler;
	private Bitmap newBitmap = null;
	private String id = MyApplication.device + "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
		init();
	}

	private void init() {
		reimageview = (RoundImageView) findViewById(R.id.reimageview);
		reone = (RelativeLayout) findViewById(R.id.reone);
		retwo = (RelativeLayout) findViewById(R.id.retwo);
		reone.setVisibility(View.GONE);
		retwo.setVisibility(View.VISIBLE);
		reusername = (TextView) findViewById(R.id.reusername);
		reusername1 = (TextView) findViewById(R.id.reusername1);
		repassword = (EditText) findViewById(R.id.repassword);
		repassword1 = (TextView) findViewById(R.id.repassword1);
		repasswordagin = (TextView) findViewById(R.id.repasswordagin);
		repasswordagin1 = (TextView) findViewById(R.id.repasswordagin1);
		sbid = (TextView) findViewById(R.id.sbid);
		sbid1 = (TextView) findViewById(R.id.sbid1);
		relationship = (EditText) findViewById(R.id.relationship);
		relationship1 = (TextView) findViewById(R.id.relationship1);

		Intent intent = getIntent();
		reusername.setText(intent.getStringExtra("username"));
		reusername1.setText(intent.getStringExtra("username"));
		repassword.setText(intent.getStringExtra("name"));
		repassword1.setText(intent.getStringExtra("name"));
		repasswordagin.setText(intent.getStringExtra("phonenum"));
		repasswordagin1.setText(intent.getStringExtra("phonenum"));
		sbid.setText(intent.getStringExtra("device_no"));
		sbid1.setText(intent.getStringExtra("device_no"));
		MyApplication.device = intent.getStringExtra("device_no");
		relationship.setText(intent.getStringExtra("relationship"));
		relationship1.setText(intent.getStringExtra("relationship"));
		Log.e("id=", MyApplication.device + "");
		settingAvatar(id);

		anext = (Button) findViewById(R.id.anext);
		anext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(AccountActivity.this).setItems(ch,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									// 传输密码
									Intent intent1 = new Intent();
									intent1.putExtra("password", getIntent()
											.getStringExtra("password"));
									intent1.putExtra("username", getIntent()
											.getStringExtra("username"));
									intent1.setClass(AccountActivity.this,
											ModifyPassword.class);
									startActivity(intent1);
									break;
								case 1:
									Intent intent2 = new Intent();
									intent2.putExtra("tag", "AccountActivity");
									intent2.putExtra("id", getIntent()
											.getStringExtra("id"));
									intent2.setClass(AccountActivity.this,
											RegisterQuActivity.class);
									startActivity(intent2);
									break;
								}
								dialog.dismiss();
							}
						}).show();
			}
		});

		textView7 = (TextView) findViewById(R.id.textView7);
		textView7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		myhandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 8:
					if (newBitmap != null) {
						try {
							Log.e("main", "进入了拍照后上传");
							saveFile(newBitmap);

						} catch (IOException e1) {
							Log.e("main", "IO异常：" + e1.getMessage());
							e1.printStackTrace();
						}
					}
					Toast.makeText(AccountActivity.this, "用户信息修改成功",
							Toast.LENGTH_LONG).show();
					break;
				case 9:
					Toast.makeText(AccountActivity.this, "无此设备ID",
							Toast.LENGTH_LONG).show();
					break;
				case 20:
					String v = (String) msg.obj;
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
							//
							if (Communicate.RESULT.get(0).equals(
									"UpdateVersionOK")) {
								Message message = myhandler.obtainMessage();
								message.what = 20;
								message.obj = Communicate.RESULT.get(0);
								myhandler.sendMessage(message);
								// 关闭发送状态
								Communicate.STATE = false;
							}
							//
							if (Communicate.RESULT.get(0)
									.equals("ModifyUserOK")) {
								Message message = myhandler.obtainMessage();
								message.what = 8;
								myhandler.sendMessage(message);
								// 关闭发送状态
								Communicate.STATE = false;
								// 上传图片
								File file = new File(getExternalCacheDir(),
										AVATAR_NAME + id + ".jpg");
								if (file.exists()) {
									try {
										Log.e("开始上传头像", "开始上传头像");
										new ImgHttpClient(AccountActivity.this)
												.uploadForm(null, msave, file,
														AVATAR_NAME + id
																+ ".jpg");// 调用Post上传请求
									} catch (IOException e) {
										Log.e("Io异常：", e.getMessage());
										e.printStackTrace();
									}

									// 删除图片
									/*
									 * if (file.delete()) { Log.e("main",
									 * "删除成功！"); }
									 */
								}

							}
							if (Communicate.RESULT.get(0).equals("NoDevice")) {
								Message message = myhandler.obtainMessage();
								message.what = 9;
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

		next = (Button) findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (init == true) {
					// 判空
					if (reusername.getText().toString() != null
							&& reusername.getText().toString().length() != 0) {
						if (repassword.getText().toString() != null
								&& repassword.getText().toString().length() != 0) {
							if (repasswordagin.getText().toString() != null) {
								if (sbid.getText().toString() != null
										&& sbid.getText().toString().length() != 0) {
									if (relationship.getText().toString() != null
											&& relationship.getText()
													.toString().length() != 0) {
										// 将EditText的信息存入TextView中
										reusername1.setText(reusername
												.getText().toString());
										repassword1.setText(repassword
												.getText().toString());
										repasswordagin1.setText(repasswordagin
												.getText().toString());
										sbid1.setText(sbid.getText().toString());
										relationship1.setText(relationship
												.getText().toString());

										Intent intent = getIntent();
										// 发送修改资料指令
										List<String> s = new ArrayList<String>();// S储存的是账号和密码还有用户id
										s.add(intent.getStringExtra("id"));
										s.add(reusername.getText().toString());
										s.add(repassword.getText().toString());
										s.add(relationship.getText().toString());
										s.add(repasswordagin.getText()
												.toString());
										s.add(sbid.getText().toString());

										List<String> starttags = new ArrayList<String>();
										starttags.add("<id>");
										starttags.add("<username>");
										starttags.add("<name>");
										starttags.add("<relationship>");
										starttags.add("<phonenum>");
										starttags.add("<device_no>");
										Communicate.sss
												.add("<UpdateUserResult>");
										Communicate.sss
												.add("</UpdateUserResult>");

										Communicate.xmlclassdata = MatchString
												.addString(
														AccountActivity.this,
														"UpdateUser.xml", s,
														starttags);
										Communicate.mark = true;

										//
										/*
										 * Communicate.sss
										 * .add("<QueryVerrsionResult>");
										 * Communicate.sss
										 * .add("</QueryVerrsionResult>");
										 * 
										 * Communicate.xmlclassdata =
										 * MatchString .addString(
										 * AccountActivity.this,
										 * "QueryVersion.xml", s, starttags);
										 * Communicate.mark = true;
										 */
										//
										init = false;
										next.setText("编辑资料");
										reone.setVisibility(View.GONE);
										retwo.setVisibility(View.VISIBLE);

									} else {
										Toast.makeText(AccountActivity.this,
												"关系不能为空", Toast.LENGTH_LONG)
												.show();
									}
								} else {
									Toast.makeText(AccountActivity.this,
											"设备ID不能为空", Toast.LENGTH_LONG)
											.show();
								}
							} else {
								Toast.makeText(AccountActivity.this, "电话不能为空",
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(AccountActivity.this, "姓名不能为空",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(AccountActivity.this, "帐号不能为空",
								Toast.LENGTH_LONG).show();
					}
				} else {
					init = true;
					next.setText("保存");
					reone.setVisibility(View.VISIBLE);
					retwo.setVisibility(View.GONE);
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

		reimageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (init == true) {
					new AlertDialog.Builder(AccountActivity.this).setItems(
							function, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case TAKE_PICTURE:
										CHOISESATAE = 1;
										Intent openCameraIntent = new Intent(
												MediaStore.ACTION_IMAGE_CAPTURE);
										Uri imageUri = Uri.fromFile(new File(
												getExternalCacheDir(),
												"image.jpg"));
										openCameraIntent.putExtra(
												MediaStore.EXTRA_OUTPUT,
												imageUri);
										startActivityForResult(
												openCameraIntent, TAKE_PICTURE);
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
			}
		});

	}

	private void settingAvatar(String id2) {
		File file = new File(getExternalCacheDir(), AVATAR_NAME + id2 + ".jpg");// 根据用户id在缓存下查找头像
		final String path = file.getAbsolutePath();
		if (file.exists()) {// 在缓存中找到了这张图片
			Bitmap bm = BitmapFactory.decodeFile(path);
			Log.e("存在此图片", "存在此图片");
			reimageview.setImageBitmap(bm);
			Log.e("mian", "头像设置成功了");
			return;
		}
		if (!file.exists()) {
			Log.e("缓存中不存在此图片", "缓存中不存在此图片");
			// 缓存中没有 去服务器拿图片
			new Thread() {
				public void run() {
					try {
						int code = new ImgHttpClient(AccountActivity.this)
								.downForm(AVATAR_NAME + id + ".jpg");
						if (code == 1) {
							Log.e("main", "下载了图片可以更改Ui");
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									BitmapFactory.Options options = new BitmapFactory.Options();
									Bitmap bm = BitmapFactory.decodeFile(path,
											options);
									reimageview.setImageBitmap(bm);
									return;
								}
							});
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
		}
		reimageview.setImageResource(R.drawable.icon_sb);
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

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public void saveFile(Bitmap bm) throws IOException {
		Log.e("进入了保存图片方法", "进入了保存图片方法");
		File myCaptureFile = new File(getExternalCacheDir(), AVATAR_NAME + id
				+ ".jpg");
		BufferedOutputStream fos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 50, fos);
		fos.flush();
		fos.close();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				// 拍照后上传
				Bitmap bitmap = BitmapFactory.decodeFile(getExternalCacheDir()
						+ "/image.jpg");
				newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth()
						/ SCALE, bitmap.getHeight() / SCALE);

				switch (CHOISESATAE) {
				case 0:

					break;
				case 1:
					reimageview.setImageBitmap(newBitmap);
					break;
				}
				ImageTools.savePhotoToSDCard(newBitmap, Environment
						.getExternalStorageDirectory().getAbsolutePath(),
						String.valueOf(System.currentTimeMillis()));
				break;

			case CHOOSE_PICTURE:
				// 选择从相册选取图片
				ContentResolver resolver = getContentResolver();
				Uri originalUri = data.getData();
				try {
					Bitmap smallBitmap = MediaStore.Images.Media.getBitmap(
							resolver, originalUri);
					if (smallBitmap != null) {
						newBitmap = ImageTools.zoomBitmap(smallBitmap,
								smallBitmap.getWidth() / SCALE,
								smallBitmap.getHeight() / SCALE);
						switch (CHOISESATAE) {
						case 0:

							break;
						case 1:
							reimageview.setImageBitmap(newBitmap);
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

}
