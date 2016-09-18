package com.szhd.guardian;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.king.photo.activity.AlbumActivity;
import com.king.photo.activity.GalleryActivity;
import com.king.photo.activity.ImageFile;
import com.king.photo.activity.ShowAllPhoto;
import com.king.photo.util.ImageItem;
import com.szhd.server.ImgHttpClient;
import com.szhd.ui.MyDatePickerDialog;
import com.szhd.ui.RoundImageView;
import com.szhd.util.ImageTools;
import com.szhd.util.MatchString;
import com.szhd.util.MyApplication;
import com.szhd.util.PreventContinuousClick;
import com.szhd.webservice.Communicate;

/**
 * 
 * @author 卢文王
 * 
 * @time 2016.02.29
 * 
 * @description 添加人信息
 * 
 */
public class SetActivity extends Activity {

	private EditText setname;
	private TextView setgender;
	private TextView setage;
	private TextView setheight;
	private TextView setweight;
	private TextView setbirth;
	private TextView setspecial;
	private EditText setaddr;
	private Button bt1;
	private EditText deviceid;
	private ImageView bt2;
	public static Bitmap bimap;
	private TextView textView1;
	private RoundImageView image;
	private String[] s = new String[] { "0" };
	private String[] gender = new String[] { "男", "女", "未知" };
	private String[] function = new String[] { "拍照", "从相册选择" };
	private AlertDialog ad = null;
	private Boolean mark = false;
	private static Boolean addAndUpdateBoolean = false;
	private int position = -1;
	private static String beguard_name = null, beguard_sex = null,
			beguard_age = null, beguard_height = null, beguard_weight = null,
			beguard_birth = null, img_descript_c = null, device_no = null,
			beguard_address = null;
	private static final int DEVICE_EXISTS_NO = 103;
	private static final int DEVICE_EXISTS_YES = 104;
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int UPDATEOK = 105;
	private static final int SCALE = 5;
	private int CHOISESATAE;
	private static final String SPECIAL_IMG = "image_name";
	private Handler mHandler;
	private static final int DOWN_IMG_START = 0;
	private static final int DOWN_IMG_END = 1;
	private static ArrayList<ImageItem> imgList;
	private boolean loginthread = true;
	private static final String SPECIAL_AVATAR_STRING = "adduser_img_";

	private static final String JPG = ".jpg";
	private static final int SAVE_AVVATAR_FILE_CODE = 0;
	private static final int SAVE_SPECIAL_FILE_CODE = 1;
	private final static int DEVICE_OK = 101;
	private final static int DEVICE_NO = 102;
	private final static int OK = 100;
	private Bitmap AvatarBitmap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_activity);
		init();
		setOnClickListener();
		// 从savedInstanceState中恢复数据, 如果没有数据需要恢复savedInstanceState为null
	}

	private void init() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					while (loginthread) {
						if (Communicate.STATE == true) {
							if (Communicate.RESULT.get(0).equals("AddOK")) {
								// 关闭发送状态
								mHandler.sendEmptyMessage(OK);
								Communicate.STATE = false;
							}

							if (Communicate.RESULT.get(0).equals(
									"QueryDeviceExistsNO")) {
								mHandler.sendEmptyMessage(DEVICE_NO);
								Communicate.STATE = false;
							}
							if (Communicate.RESULT.get(0).equals(
									"QueryDeviceExistsOK")) {
								mHandler.sendEmptyMessage(DEVICE_OK);
								Communicate.STATE = false;
							}

							if (Communicate.RESULT.get(0).equals("ExistsNO")) {
								Log.e("main", "device没有被使用");
								mHandler.sendEmptyMessage(DEVICE_EXISTS_NO);
								Communicate.STATE = false;
							}
							if (Communicate.RESULT.get(0).equals("UpdateOK")) {
								Log.e("更新被监护人成功", "更新被监护人成功");
								mHandler.sendEmptyMessage(UPDATEOK);
								Communicate.STATE = false;
							}
							if (Communicate.RESULT.get(0)
									.equals("NotExistence")) {
								Log.e("main", "更新被监护人找不到该监护人信息");
							}

						}
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		s[0] = Communicate.IMSI;
		setname = (EditText) findViewById(R.id.setname);
		setgender = (TextView) findViewById(R.id.setgender);
		setage = (TextView) findViewById(R.id.setage);
		setheight = (TextView) findViewById(R.id.setheight);
		setweight = (TextView) findViewById(R.id.setweight);
		setbirth = (TextView) findViewById(R.id.setbirth);
		setspecial = (TextView) findViewById(R.id.setspecial);
		deviceid = (EditText) findViewById(R.id.deviceid);

		setaddr = (EditText) findViewById(R.id.setaddr);
		bt1 = (Button) findViewById(R.id.button1);
		bt2 = (ImageView) findViewById(R.id.button2);
		textView1 = (TextView) findViewById(R.id.textView1);
		image = (RoundImageView) findViewById(R.id.cornersimageview);
		image.setImageResource(R.drawable.icon_sb);
		Intent intent = getIntent();
		if (intent != null) {
			if (intent.getStringExtra("Action").equals("ADD")) {
				deviceid.setEnabled(true);
				addAndUpdateBoolean = false;
			}
			if (intent.getStringExtra("Action").equals("UPDATE")) {
				addAndUpdateBoolean = true;
				deviceid.setEnabled(false);
			}
			if (intent.getStringExtra("iconRes") != null) {
				String stringExtra = intent.getStringExtra("iconRes");
				File file = new File(getExternalCacheDir(), stringExtra);
				if (file.exists()) {
					Bitmap decodeFile = BitmapFactory
							.decodeFile(file.getPath());
					image.setImageBitmap(decodeFile);
				}
			}
			if (intent.getStringExtra("device") != null) {
				deviceid.setText(intent.getStringExtra("device"));
				MyApplication.device = intent.getStringExtra("device");
			}
			if (intent.getStringExtra("name") != null)
				setname.setText(intent.getStringExtra("name"));
			if (intent.getStringExtra("age") != null)
				setage.setText(intent.getStringExtra("age"));
			if (intent.getStringExtra("gender") != null)
				setgender.setText(intent.getStringExtra("gender"));
			if (intent.getStringExtra("height") != null)
				setheight.setText(intent.getStringExtra("height"));
			if (intent.getStringExtra("weight") != null)
				setweight.setText(intent.getStringExtra("weight"));
			if (intent.getStringExtra("address") != null) {
				setaddr.setText(intent.getStringExtra("address"));
			}
			if (intent.getStringExtra("mark") != null) {
				mark = true;
				position = Integer.valueOf(intent.getStringExtra("mark"));
			} else {
				mark = false;
				position = -1;
			}
		} else {
			Bundle bundle = intent.getExtras();
			imgList = (ArrayList<ImageItem>) bundle.get("imglist");
		}

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DOWN_IMG_START:
					if (!SetActivity.this.isFinishing()) {

						ProgressDialog show = ProgressDialog.show(
								SetActivity.this, "保存", "请稍后...", true);
					}
					break;
				case DOWN_IMG_END:
					Toast.makeText(SetActivity.this, "保存用户成功",
							Toast.LENGTH_LONG).show();
					final Intent intent = new Intent();
					intent.setClass(SetActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
					break;
				case OK:
					// 上传特征图片
					UpdateSpecial();
					break;
				case DEVICE_OK:

					if (addAndUpdateBoolean) {
						updateUserData(MyApplication.device + "");
					} else {
						insertUserData();
					}
					break;
				case DEVICE_NO:
					Toast.makeText(SetActivity.this, "无此设备ID",
							Toast.LENGTH_LONG).show();
					break;
				case DEVICE_EXISTS_NO:
					insertUserData();
					break;
				case DEVICE_EXISTS_YES:
					Toast.makeText(SetActivity.this, "此设备已经被监护",
							Toast.LENGTH_SHORT).show();
					break;
				case UPDATEOK:
					Toast.makeText(SetActivity.this, "保存用户成功",
							Toast.LENGTH_LONG).show();
					final Intent intent2 = new Intent();
					intent2.setClass(SetActivity.this, MainActivity.class);
					startActivity(intent2);
					finish();
					break;
				default:
					Toast.makeText(SetActivity.this, "插入用户失败",
							Toast.LENGTH_LONG).show();
					break;
				}

			}

			private void queryDeviceExists() {
				String deviced = deviceid.getText().toString();
				List<String> s = new ArrayList<String>();
				s.add(deviced);
				List<String> starttags = new ArrayList<String>();
				starttags.add("<device>");
				Communicate.sss.add("<QueryDeviceExistsResponse>");
				Communicate.sss.add("</QueryDeviceExistsResponse>");
				Communicate.xmlclassdata = MatchString.addString(
						SetActivity.this, "1.xml", s, starttags);
				Communicate.mark = true;
			}
		};

	}

	/**
	 * 当检查有该设备ID后 在服务器中插入数据
	 */
	protected void insertUserData() {
		Log.e("进入了插入新数据的方法", "进入了插入新数据的方法");
		try {
			saveFile(AvatarBitmap, 1, SAVE_AVVATAR_FILE_CODE);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		final Intent intent = new Intent();
		intent.getExtras();
		intent.setClass(SetActivity.this, MainActivity.class);
		intent.putExtra("iconRes", String.valueOf(R.drawable.girl));
		String add = "person|A|";
		JSONObject ja = new JSONObject();
		try {
			String deviced = deviceid.getText().toString();
			File file = new File(getExternalCacheDir(), SPECIAL_AVATAR_STRING
					+ deviced + JPG);
			ja.put("image_name1", "//");
			ja.put("image_name2", "//");
			ja.put("image_name3", "//");
			ja.put("image_name4", "//");
			ja.put("image_name5", "//");
			ja.put("image_descript", img_descript_c);
			ja.put("beguard_name", beguard_name);
			ja.put("beguard_sex", beguard_sex);
			ja.put("beguard_age", beguard_age);
			ja.put("beguard_height", beguard_height);
			ja.put("beguard_weight", beguard_weight);
			if (file.exists()) {
				ja.put("beguard_img_url", file.getName());
			} else {
				ja.put("beguard_img_url", "无图片");
			}
			ja.put("beguard_birth", beguard_birth);
			ja.put("beguard_phonenum", Communicate.IMSI);
			ja.put("beguard_address", beguard_address);
			ja.put("device_no", device_no);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		List<String> s2 = new ArrayList<String>();
		s2.add(device_no);
		s2.add(add + ja.toString());
		Log.e("JSON.tostring=", ja.toString());
		List<String> starttags2 = new ArrayList<String>();
		starttags2.add("<devicenum>");
		starttags2.add("<content>");
		Communicate.sss.add("<DealbeguardResult>");
		Communicate.sss.add("</DealbeguardResult>");
		Communicate.xmlclassdata = MatchString.addString(SetActivity.this,
				"Dealbeguard.xml", s2, starttags2);
		// Communicate cc = new Communicate();
		// cc.start();
		Communicate.mark = true;

		intent.putExtra("name", setname.getText().toString());
		intent.putExtra("age", setage.getText().toString());
		intent.putExtra("gender", setgender.getText().toString());
		intent.putExtra("height", setheight.getText().toString());
		intent.putExtra("weight", setweight.getText().toString());

		if (mark == true) {
			intent.putExtra("mark", String.valueOf(position));
		} else {
			intent.putExtra("mark", "mark");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e("调用了onResume方法", "调用了onResume方法");
		int num = getIntent().getIntExtra("num", 0);
		setspecial.setText("已选择 " + num + " 张图片");

	}

	private void setOnClickListener() {

		setspecial.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (!PreventContinuousClick.isFastClick()) {
					if (event.getX() >= (setspecial.getWidth() - setspecial
							.getCompoundDrawables()[2].getBounds().width())) {
						if (ad == null || !ad.isShowing()) {
							ad = new AlertDialog.Builder(SetActivity.this)
									.setItems(
											function,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													switch (which) {
													case TAKE_PICTURE:
														CHOISESATAE = 0;
														Intent openCameraIntent = new Intent(
																MediaStore.ACTION_IMAGE_CAPTURE);
														Uri imageUri = Uri
																.fromFile(new File(
																		Environment
																				.getExternalStorageDirectory(),
																		"image.jpg"));
														openCameraIntent
																.putExtra(
																		MediaStore.EXTRA_OUTPUT,
																		imageUri);
														startActivityForResult(
																openCameraIntent,
																TAKE_PICTURE);
														break;
													case CHOOSE_PICTURE:
														CHOISESATAE = 0;
														ImageFile.cls = SetActivity.class;
														AlbumActivity.cls = SetActivity.class;
														GalleryActivity.cls = SetActivity.class;
														ShowAllPhoto.cls = SetActivity.class;

														Intent intent = new Intent(
																SetActivity.this,
																AlbumActivity.class);
														startActivity(intent);
														overridePendingTransition(
																R.anim.activity_translate_in,
																R.anim.activity_translate_out);
														break;

													}
												}
											}).show();
						}
						setspecial.clearFocus();
						setspecial.setFocusable(false);
					} else {
						setspecial.setFocusable(true);
						setspecial.setFocusableInTouchMode(true);
						setspecial.requestFocus();
						setspecial.requestFocusFromTouch();
					}
				}
				return false;
			}
		});

		setgender.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(SetActivity.this).setItems(gender,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								setgender.setText(gender[which]);
								dialog.dismiss();
							}
						}).show();
			}
		});

		setage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NumberPicker mPicker = new NumberPicker(SetActivity.this);
				mPicker.setMinValue(0);
				mPicker.setMaxValue(150);
				mPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
				mPicker.setValue(Integer.valueOf(setage.getText().toString()));
				mPicker.setOnValueChangedListener(new OnValueChangeListener() {
					@Override
					public void onValueChange(NumberPicker picker, int oldVal,
							int newVal) {
						setage.setText(String.valueOf(newVal));
					}
				});
				AlertDialog mAlertDialog = new AlertDialog.Builder(
						SetActivity.this).setView(mPicker)
						.setPositiveButton("确定", null).create();
				mAlertDialog.show();
			}
		});

		setheight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NumberPicker mPicker = new NumberPicker(SetActivity.this);
				mPicker.setMinValue(0);
				mPicker.setMaxValue(300);
				mPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
				mPicker.setValue(Integer
						.valueOf(setheight.getText().toString()));
				mPicker.setOnValueChangedListener(new OnValueChangeListener() {
					@Override
					public void onValueChange(NumberPicker picker, int oldVal,
							int newVal) {
						setheight.setText(String.valueOf(newVal));
					}
				});
				AlertDialog mAlertDialog = new AlertDialog.Builder(
						SetActivity.this).setView(mPicker)
						.setPositiveButton("确定", null).create();
				mAlertDialog.show();
			}
		});

		setweight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NumberPicker mPicker = new NumberPicker(SetActivity.this);
				mPicker.setMinValue(0);
				mPicker.setMaxValue(200);
				mPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
				mPicker.setValue(Integer
						.valueOf(setweight.getText().toString()));
				mPicker.setOnValueChangedListener(new OnValueChangeListener() {
					@Override
					public void onValueChange(NumberPicker picker, int oldVal,
							int newVal) {
						setweight.setText(String.valueOf(newVal));
					}
				});
				AlertDialog mAlertDialog = new AlertDialog.Builder(
						SetActivity.this).setView(mPicker)
						.setPositiveButton("确定", null).create();
				mAlertDialog.show();
			}
		});

		setbirth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String s = setbirth.getText().toString();
				new MyDatePickerDialog(SetActivity.this,
						new MyDatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int day) {
								int mYear = year;
								int mMonth = month;
								int mDay = day;
								setbirth.setText(new StringBuilder()
										.append(mYear)
										.append("年")
										.append((mMonth < 10) ? "0"
												+ (mMonth + 1) : (mMonth + 1))
										.append("月")
										.append((mDay < 10) ? "0" + mDay : mDay)
										.append("日"));
							}
						}, Integer.valueOf(s.substring(0, 4)), Integer
								.valueOf(s.substring(5, 7)) - 1, Integer
								.valueOf(s.substring(8, 10))).show();
			}
		});

		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(SetActivity.this).setItems(function,
						new DialogInterface.OnClickListener() {
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
									// 从相册选取
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

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, s);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		/*
		 * setphonenum .setAdapter(adapter);
		 * setphonenum.setOnItemSelectedListener(new OnItemSelectedListener() {
		 * 
		 * @Override public void onItemSelected(AdapterView<?> parent, View
		 * view, int pos, long id) { beguard_phonenum = s[pos]; }
		 * 
		 * @Override public void onNothingSelected(AdapterView<?> parent) { }
		 * });
		 */

		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				beguard_name = setname.getText().toString();
				beguard_sex = setgender.getText().toString();
				beguard_age = setage.getText().toString();
				beguard_height = setheight.getText().toString();
				beguard_weight = setweight.getText().toString();
				beguard_birth = setbirth.getText().toString();
				img_descript_c = setspecial.getText().toString();
				device_no = deviceid.getText().toString();
				beguard_address = setaddr.getText().toString();
				if (beguard_name != null && beguard_name.length() != 0
						&& beguard_sex != null && beguard_sex.length() != 0
						&& beguard_age != null && beguard_age.length() != 0
						&& beguard_height != null
						&& beguard_height.length() != 0
						&& beguard_weight != null
						&& beguard_weight.length() != 0
						&& beguard_birth != null && beguard_birth.length() != 0
						&& img_descript_c != null
						&& img_descript_c.length() != 0 && device_no != null
						&& device_no.length() != 0 && beguard_address != null
						&& beguard_address.length() != 0) {
					if (device_no.length() != 0 && device_no != null) {

						if (addAndUpdateBoolean) {
							updateUserData(MyApplication.device);
						} else {
							Intent intent = getIntent();
							Bundle bundle = intent.getExtras();
							int num = getIntent().getIntExtra("num", 0);
							if (num > 0) {
								imgList = (ArrayList<ImageItem>) bundle
										.get("imglist");
							}
							List<String> s = new ArrayList<String>();
							s.add(device_no);

							List<String> starttags = new ArrayList<String>();
							starttags.add("<device>");
							Communicate.sss.add("<QueryDeviceExistsResult>");
							Communicate.sss.add("</QueryDeviceExistsResult>");
							Communicate.xmlclassdata = MatchString.addString(
									SetActivity.this, "QueryDeviceExists.xml",
									s, starttags);
							Communicate.mark = true;

						}

					} else {

						Toast.makeText(SetActivity.this, "设备ID不能为空",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(SetActivity.this, "不能为空", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		bt2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		textView1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	@Override
	public void onBackPressed() {
		Log.e("main", "调用了返回按键的方法");
		super.onBackPressed();
		Intent intent = new Intent();
		intent.setClass(SetActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		getIntent().putExtras(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 选择头像会返回头像图片
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				Bitmap bitmap = BitmapFactory.decodeFile(Environment
						.getExternalStorageDirectory() + "/image.jpg");
				// Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,
				// bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				Bitmap zoomBitmap = ImageTools.zoomBitmap(bitmap,
						bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				AvatarBitmap = Bitmap.createBitmap(zoomBitmap);
				// try {
				// saveFile(newBitmap, 1, SAVE_AVVATAR_FILE_CODE);
				// } catch (IOException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// }
				bitmap.recycle();
				switch (CHOISESATAE) {
				case 0:

					break;
				case 1:
					// image.setImageBitmap(newBitmap);
					image.setImageBitmap(AvatarBitmap);

					break;
				}
				// ImageTools.savePhotoToSDCard(newBitmap, Environment
				// .getExternalStorageDirectory().getAbsolutePath(),
				// String.valueOf(System.currentTimeMillis()));

				ImageTools.savePhotoToSDCard(AvatarBitmap, Environment
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
						// Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
						// photo.getWidth() / SCALE, photo.getHeight()
						// / SCALE);

						AvatarBitmap = ImageTools.zoomBitmap(photo,
								photo.getWidth() / SCALE, photo.getHeight()
										/ SCALE);
						// saveFile(smallBitmap, 1, SAVE_AVVATAR_FILE_CODE);
						photo.recycle();
						switch (CHOISESATAE) {
						case 0:

							break;
						case 1:
							image.setImageBitmap(AvatarBitmap);
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

	private boolean telCheck(String tel) {
		Pattern p = Pattern
				.compile("^((13\\d{9}$)|(15[0,1,2,3,5,6,7,8,9]\\d{8}$)|(18[0,1,2,5,6,7,8,9]\\d{8}$)|(147\\d{8})$)");
		Matcher m = p.matcher(tel);
		return m.matches();
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * 
	 * @throws IOException
	 */
	public File saveFile(Bitmap bm, int i, int CODE) throws IOException {
		if (CODE == 0) {// 保存头像图片
			Log.e("进入了保存头像图片方法", "进入了头像保存图片方法");
			String deviced = deviceid.getText().toString();
			File myCaptureFile = new File(getExternalCacheDir(),
					SPECIAL_AVATAR_STRING + deviced + JPG);
			BufferedOutputStream fos = new BufferedOutputStream(
					new FileOutputStream(myCaptureFile));
			bm.compress(Bitmap.CompressFormat.JPEG, 50, fos);
			fos.flush();
			fos.close();
			return myCaptureFile;
		} else {
			Log.e("进入了保存图片方法", "进入了保存图片方法");
			File myCaptureFile = new File(getExternalCacheDir(),
					"adduser_special_img_" + (i + 1) + "_IMSI.jpg");
			BufferedOutputStream fos = new BufferedOutputStream(
					new FileOutputStream(myCaptureFile));
			bm.compress(Bitmap.CompressFormat.JPEG, 50, fos);
			fos.flush();
			fos.close();
			return myCaptureFile;
		}
	}

	private void UpdateSpecial() {

		new Thread() {
			@Override
			public void run() {

				String deviced = deviceid.getText().toString();
				Log.e("main", "deviced=========" + deviced);
				mHandler.sendEmptyMessage(DOWN_IMG_START);
				File file2 = new File(getExternalCacheDir(),
						SPECIAL_AVATAR_STRING + deviced + JPG);
				if (file2.exists()) {
					try {
						new ImgHttpClient(SetActivity.this).uploadForm(null,
								"a", file2, file2.getName());
						Log.e("添加用户的头像上传成功", "成功");

					} catch (IOException e) {
						e.printStackTrace();
						Log.e("添加用户的头像发生异常：", e.getMessage());

					}
				} else {
					Log.e("main", "该文件不存在！！！");
				}
				if (imgList != null) {
					for (int i = 0; i < imgList.size(); i++) {
						Bitmap item = imgList.get(i).getBitmap();
						try {
							File file = saveFile(item, i,
									SAVE_SPECIAL_FILE_CODE);
							new ImgHttpClient(SetActivity.this).uploadForm(
									null, "a", file, "adduser_special_img_"
											+ (i + 1) + "_IMSI.jpg");
						} catch (IOException e) {
							e.printStackTrace();
							Log.e("上传图片发生IO异常", "上传图片发生IO异常");
						}
					}
				} else {
					Log.e("main", "imglist==null!!!!");
				}
				mHandler.sendEmptyMessage(DOWN_IMG_END);
			}

		}.start();
	}

	// @Override
	// protected void onSaveInstanceState(Bundle outState) {
	// // TODO Auto-generated method stub
	// super.onSaveInstanceState(outState);
	// outState.putString("name", beguard_name);
	// outState.putString("sex", beguard_sex);
	// outState.putString("age", beguard_age);
	// outState.putString("height", beguard_height);
	// outState.putString("weight", beguard_weight);
	// outState.putString("birth", beguard_birth);
	// outState.putString("device", device_no);
	// outState.putString("address", beguard_address);
	//
	// }
	//
	// @Override
	// protected void onRestoreInstanceState(Bundle savedInstanceState) {
	// // TODO Auto-generated method stub
	// if (savedInstanceState != null) {
	// setname.setText(savedInstanceState.getString("name"));
	// setgender.setText(savedInstanceState.getString("sex"));
	// setage.setText(savedInstanceState.getString("age"));
	// setheight.setText(savedInstanceState.getString("height"));
	// setweight.setText(savedInstanceState.getString("weight"));
	// setbirth.setText(savedInstanceState.getString("birth"));
	// deviceid.setText(savedInstanceState.getString("device_no"));
	// setaddr.setText(savedInstanceState.getString("beguard_address"));
	// }
	//
	// }

	private void updateUserData(String device) {
		if (AvatarBitmap != null) {
			new Thread() {
				public void run() {
					try {
						File saveFile = saveFile(AvatarBitmap, 1,
								SAVE_AVVATAR_FILE_CODE);
						new ImgHttpClient(SetActivity.this).uploadForm(null,
								"a", saveFile, saveFile.getName());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
		}
		Log.e("main", "进入了更新被监护人的方法");
		String add = "person|U|";
		JSONObject ja = new JSONObject();
		try {
			ja.put("beguard_name", beguard_name);
			ja.put("beguard_sex", beguard_sex);
			ja.put("beguard_age", beguard_age);
			ja.put("beguard_height", beguard_height);
			ja.put("beguard_weight", beguard_weight);
			ja.put("beguard_address", beguard_address);
			ja.put("beguard_address", beguard_address);
			ja.put("beguard_birth", beguard_birth);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		List<String> s2 = new ArrayList<String>();
		s2.add(device_no);
		s2.add(add + ja.toString());
		Log.e("JSON.tostring=", ja.toString());
		List<String> starttags2 = new ArrayList<String>();
		starttags2.add("<devicenum>");
		starttags2.add("<content>");
		Communicate.sss.add("<DealbeguardResult>");
		Communicate.sss.add("</DealbeguardResult>");
		Communicate.xmlclassdata = MatchString.addString(SetActivity.this,
				"Dealbeguard.xml", s2, starttags2);
		Communicate.mark = true;
	}

}
