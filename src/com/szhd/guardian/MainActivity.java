package com.szhd.guardian;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.king.photo.util.Res;
import com.szhd.bean.UserBean;
import com.szhd.bluetooth.BluetoothChatService;
import com.szhd.impl.IXListViewListener;
import com.szhd.impl.OnSlideListener;
import com.szhd.pulltorefresh.RefreshTime;
import com.szhd.server.ImgHttpClient;
import com.szhd.ui.AddDialog;
import com.szhd.ui.FunctionActivity;
import com.szhd.ui.RoundImageView;
import com.szhd.util.MatchString;
import com.szhd.util.MyApplication;
import com.szhd.webservice.Communicate;

/**
 * 
 * @author 卢文王
 * 
 * @time 2016.02.29
 * 
 * @description 主activity界面
 * 
 */
public class MainActivity extends Activity implements OnItemClickListener,
		OnClickListener, OnSlideListener, IXListViewListener {

	private ListViewCompat mListView;
	private ImageView addinfo;
	private ImageView menubtn;
	private SlideAdapter slideAdapter;
	private TextView mmain_tv_hint;
	private ArrayList<UserBean> userBeans = new ArrayList<UserBean>();
	private List<MessageItem> mMessageItems = new ArrayList<MessageItem>();
	private final int TYPE_ONE = 0, TYPE_TWO = 1;
	private SlideView mLastSlideViewWithStatusOn;
	private static final int NOTIFICATION_FLAG = 1;
	private SlidingMenu menu;
	private SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm",
			Locale.getDefault());
	private long exitTime = 0;
	private AddDialog customdialog;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	private ProgressDialog pdDialog;
	private String mConnectedDeviceName;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothChatService mChatService = null;

	private ArrayList<String> mPairedDevicesList = new ArrayList<String>();
	private ArrayList<String> mNewDevicesList = new ArrayList<String>();
	private String[] strName;
	private String address;
	private RelativeLayout layout;
	private static final int PULL_NO = 26;
	// 更新失败
	private static final int DOWN_APK_ERROR = 21;
	// 更新成功
	private static final int DOWN_APK_SUCCESS = 22;
	private static final int PULL_OK = 24;
	private List<String> MyList;
	private List<String> BackList;
	private static final int DELETE_PERSION_OK = 25;
	private static final int QUERY_JURISDICTION = 27;

	private boolean loginthread = true;
	private Handler myhandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// LoginActivity.instance.finish();
		Res.init(this);
		myhandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {

				case 7:
					List<String> s = (List<String>) msg.obj;
					for (String d : s) {

						Log.e("s=", d.toString());
					}
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, AccountActivity.class);

					intent.putExtra("id", s.get(1));
					intent.putExtra("username", s.get(2));
					intent.putExtra("password", s.get(3));
					intent.putExtra("phonenum", s.get(4));
					intent.putExtra("device_no", s.get(5));
					intent.putExtra("relationship", s.get(6));
					intent.putExtra("name", s.get(7));
					MyApplication.device = s.get(5);
					startActivity(intent);
					break;
				case 20:
					float version = selectPageVersion();
					float newVersion = (Float) msg.obj;
					if (newVersion > version) {
						startAlertDialog(MainActivity.this, 1, newVersion);
					} else {
						startAlertDialog(MainActivity.this, -1, version);
					}
					break;
				case DOWN_APK_ERROR:
					pdDialog.dismiss();
					Toast.makeText(MainActivity.this, "更新失败，请检查网络",
							Toast.LENGTH_LONG).show();
					break;
				case DOWN_APK_SUCCESS:
					pdDialog.dismiss();
					String pathString = (String) msg.obj;
					Toast.makeText(MainActivity.this, "下载成功，请安装新版本",
							Toast.LENGTH_LONG).show();
					File dir = Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
					String path = (String) msg.obj;
					File file = new File(dir, path);
					Intent intent2 = new Intent(Intent.ACTION_VIEW);
					intent2.setDataAndType(Uri.fromFile(file),
							"application/vnd.android.package-archive");
					startActivity(intent2);
					break;
				case PULL_OK:
					mmain_tv_hint.setVisibility(View.GONE);
					MyList = new ArrayList<String>();
					BackList = (List<String>) msg.obj;// 源数据
					for (String s1 : BackList) {

						Log.e("元数据：", "BackList=" + s1);
					}

					for (int j = 0; j < BackList.size() / 9; j++) {
						for (int i = 0; i < 9; i++) {
							MyList.add(BackList.get(BackList.size() / 9 * i + j));
						}
					}
					for (String s1 : BackList) {

						Log.e("main", "BackList=" + s1);
					}

					// 将获取的List转为数组
					String[] strings = MyList
							.toArray(new String[MyList.size()]);

					String[][] strings2 = new String[MyList.size() / 9][9];
					int o = 0;
					for (int i = 0; i < strings.length / 9; i++) {
						for (int j = 0; j < 9 && o < strings.length; j++, o++) {
							strings2[i][j] = strings[o];
							Log.e("main", "strings2[" + i + "][" + j + "]="
									+ strings2[i][j]);
						}
					}
					userBeans.clear();
					// 将数组转为二维数组
					for (int i = 0; i < strings.length / 9; i++) {
						UserBean userBean = new UserBean();
						userBean.setName(strings2[i][1]);
						userBean.setSex(strings2[i][2]);
						userBean.setAge(Integer.parseInt(strings2[i][3]));
						userBean.setHeight(Integer.parseInt(strings2[i][4]));
						userBean.setWeight(Integer.parseInt(strings2[i][5]));
						userBean.setiConUrl(strings2[i][6]);
						userBean.setAddress(strings2[i][7]);
						userBean.setDevice(strings2[i][8]);
						userBeans.add(userBean);

					}

					for (UserBean u : userBeans) {
						Log.e("userbeans=", u.toString());
					}

					slideAdapter = new SlideAdapter(userBeans);
					// slideAdapter.addContactList(userBeans);
					slideAdapter.notifyDataSetChanged();
					mListView.setAdapter(slideAdapter);
					break;
				case DELETE_PERSION_OK:
					Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_LONG)
							.show();
					break;
				case PULL_NO:
					mmain_tv_hint.setVisibility(View.VISIBLE);
					break;
				case QUERY_JURISDICTION:
					// 查询用户的权限信息 当点击权限移交时
					List<String> list = (List<String>) msg.obj;
					Intent intent3 = new Intent();
					intent3.putExtra("first_Guardian", list.get(1));
					intent3.setClass(MainActivity.this, ChangeAdmin.class);
					startActivity(intent3);
					break;
				}
			}

			private float selectPageVersion() {
				PackageManager pm = getPackageManager();
				PackageInfo pi;
				try {
					pi = pm.getPackageInfo(getPackageName(), 0);
					String versionName = pi.versionName;
					Log.e("main", "versionName:" + versionName);
					return Float.parseFloat(versionName);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			}
		};

		initData();
		initView();
	}

	private void initData() {
		// new ImgHttpClient(this).downForm()
		// 发送获取所有被监护人的信息
		List<String> s = new ArrayList<String>();
		s.add(Communicate.IMSI);
		List<String> starttags = new ArrayList<String>();
		starttags.add("<imsi>");
		Communicate.sss.add("<def1>");
		Communicate.sss.add("</def1>");
		Communicate.sss.add("<beguard_name>");
		Communicate.sss.add("</beguard_name>");
		Communicate.sss.add("<beguard_sex>");
		Communicate.sss.add("</beguard_sex>");
		Communicate.sss.add("<beguard_age>");
		Communicate.sss.add("</beguard_age>");
		Communicate.sss.add("<beguard_height>");
		Communicate.sss.add("</beguard_height>");
		Communicate.sss.add("<beguard_weight>");
		Communicate.sss.add("</beguard_weight>");
		Communicate.sss.add("<beguard_img_url>");
		Communicate.sss.add("</beguard_img_url>");
		Communicate.sss.add("<beguard_address>");
		Communicate.sss.add("</beguard_address>");
		Communicate.sss.add("<device_no>");
		Communicate.sss.add("</device_no>");
		Communicate.xmlclassdata = MatchString.addString(MainActivity.this,
				"GetUserInfo.xml", s, starttags);
		Communicate.mark = true;

	}

	private void initView() {
		mListView = (ListViewCompat) findViewById(android.R.id.list);
		PendingIntent pendingIntent3 = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);

		Notification notify3 = new Notification.Builder(this)
				.setSmallIcon(R.drawable.notelogo)
				.setTicker("TickerText:" + "提示")
				.setContentTitle("Notification Title")
				.setContentText("This is the notification message")
				.setContentIntent(pendingIntent3).setNumber(1).build();
		notify3.flags |= Notification.FLAG_AUTO_CANCEL;
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(NOTIFICATION_FLAG, notify3);
		mmain_tv_hint = (TextView) findViewById(R.id.main_tv_hint);
		addinfo = (ImageView) findViewById(R.id.addinfo);
		addinfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				customdialog = new AddDialog(MainActivity.this, R.style.dialog,
						new AddDialog.MyDialogListener() {
							@Override
							public void onClick(View view) {
								switch (view.getId()) {
								case R.id.addperson:
									Intent intentaddperson = new Intent();
									intentaddperson.putExtra("Action", "ADD");
									intentaddperson.setClass(MainActivity.this,
											SetActivity.class);
									startActivityForResult(intentaddperson, 0);
									customdialog.cancel();
									break;
								// 屏蔽了添加物
								/*
								 * case R.id.addsth: Intent intentaddsth = new
								 * Intent();
								 * intentaddsth.setClass(MainActivity.this,
								 * SetSthActivity.class);
								 * startActivity(intentaddsth);
								 * customdialog.cancel(); break;
								 */
								}
							}
						});
				Window win = customdialog.getWindow();

				LayoutParams params = new LayoutParams();
				int[] location = new int[2];
				addinfo.getLocationOnScreen(location);
				DisplayMetrics dm = getResources().getDisplayMetrics();

				params.x = dm.widthPixels;
				params.y = (int) ((3.5f * location[1]) - (dm.heightPixels / 2));
				win.setAttributes(params);
				customdialog.setCanceledOnTouchOutside(true);
				customdialog.show();

			}
		});

		// for (int i = 0; i < 4; i++) {
		// MessageItem item = new MessageItem();
		// item.iconRes = R.drawable.sth;
		// item.name = "香奈儿皮包";
		// item.age = "1";
		// item.gender = "2";
		// item.mesmark = false;
		// mMessageItems.add(item);
		// }
		// slideAdapter = new SlideAdapter();
		// mListView.setAdapter(slideAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);

		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeEnabled(true);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.slide_menu);
		// menu.setBackgroundColor(getResources().getColor(
		// android.R.color.background_dark));
		View menuroot = menu.getMenu();

		menubtn = (ImageView) findViewById(R.id.menu);
		menubtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menu.showMenu();
			}
		});

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (loginthread) {
						if (Communicate.STATE == true) {
							if ("999999998".equals(Communicate.RESULT.get(0))) {
								Message message = myhandler.obtainMessage();
								message.what = 7;
								message.obj = Communicate.RESULT;
								myhandler.sendMessage(message);
								// 关闭发送状态
								Communicate.STATE = false;
							}
							if ("PullOK".equals(Communicate.RESULT.get(0))) {
								Message msg = Message.obtain();
								msg.obj = Communicate.RESULT;
								msg.what = PULL_OK;
								myhandler.sendMessage(msg);
								// 关闭发送状态
								Communicate.STATE = false;
							}
							if ("DeletePersionOK".equals(Communicate.RESULT
									.get(0))) {
								// 删除被监护人成功
								Message msg = Message.obtain();
								myhandler.sendEmptyMessage(DELETE_PERSION_OK);
								Communicate.STATE = false;
							}
							if ("PullNO".equals(Communicate.RESULT.get(0))) {
								myhandler.sendEmptyMessage(PULL_NO);
								Communicate.STATE = false;
							}
							if ("QueryJurisdiction".equals(Communicate.RESULT
									.get(0))) {
								Message msg = Message.obtain();
								msg.what = QUERY_JURISDICTION;
								msg.obj = Communicate.RESULT;
								myhandler.sendMessage(msg);
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

		RelativeLayout user = (RelativeLayout) menuroot
				.findViewById(R.id.admin);
		user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {

				// 发送获取账户信息命令
				List<String> s = new ArrayList<String>();
				s.add(Communicate.IMSI);
				List<String> starttags = new ArrayList<String>();
				starttags.add("<phonenum>");

				Communicate.sss.add("<def1>");
				Communicate.sss.add("</def1>");
				Communicate.sss.add("<id>");
				Communicate.sss.add("</id>");
				Communicate.sss.add("<username>");
				Communicate.sss.add("</username>");
				Communicate.sss.add("<password>");
				Communicate.sss.add("</password>");
				Communicate.sss.add("<SIMnum>");
				Communicate.sss.add("</SIMnum>");
				Communicate.sss.add("<device_no>");
				Communicate.sss.add("</device_no>");
				Communicate.sss.add("<relationship>");
				Communicate.sss.add("</relationship>");
				Communicate.sss.add("<name>");
				Communicate.sss.add("</name>");

				Communicate.xmlclassdata = MatchString.addString(
						MainActivity.this, "QueryUser.xml", s, starttags);
				Communicate.mark = true;

			}
		});

		RelativeLayout safety = (RelativeLayout) menuroot
				.findViewById(R.id.safety);
		safety.setOnClickListener(new OnClickListener() {
			// 权限移交点击事件
			@Override
			public void onClick(final View v) {
				// 发送获取账户信息命令
				List<String> s = new ArrayList<String>();
				s.add(Communicate.IMSI);
				List<String> starttags = new ArrayList<String>();
				starttags.add("<phonenum>");
				Communicate.sss.add("<def1>");
				Communicate.sss.add("</def1>");
				Communicate.sss.add("<first_Guardian>");
				Communicate.sss.add("</first_Guardian>");
				Communicate.xmlclassdata = MatchString.addString(
						MainActivity.this, "QueryJurisdiction.xml", s,
						starttags);
				Communicate.mark = true;
			}
		});

		RelativeLayout clearcachingdata = (RelativeLayout) menuroot
				.findViewById(R.id.clearcachingdata);
		clearcachingdata.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setTitle("提示").setMessage("是否清理缓存？")
						.setNegativeButton("否", null);
				builder.setPositiveButton("是",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								File file = new File(getExternalCacheDir() + "");
								deleteAllFiles(file);
								float f = getResources().getDisplayMetrics().density;
								Toast.makeText(MainActivity.this, "缓存已清理",
										Toast.LENGTH_LONG).show();

							}
						});
				builder.show();
			}
		});

		RelativeLayout versionupdate = (RelativeLayout) menuroot
				.findViewById(R.id.versionupdate);
		versionupdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							while (loginthread) {
								if (Communicate.STATE == true) {
									if (Communicate.RESULT.get(0)
											.substring(0, 15)
											.equals("UpdateVersionOK")) {
										Message msg = Message.obtain();
										msg.what = 20;
										Log.e("main", Communicate.RESULT.get(0));
										String version = Communicate.RESULT
												.get(0);
										String substring = version
												.substring(15);
										msg.obj = Float.parseFloat(substring);
										myhandler.sendMessage(msg);
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

				List<String> s = new ArrayList<String>();
				List<String> starttags = new ArrayList<String>();
				Communicate.sss.add("<QueryVerrsionResult>");
				Communicate.sss.add("</QueryVerrsionResult>");
				Communicate.xmlclassdata = MatchString.addString(
						MainActivity.this, "QueryVersion.xml", s, starttags);
				Communicate.mark = true;

				/*
				 * Bitmap photo = getImageFromAssetsFile("ceshiaaa.png");
				 * ByteArrayOutputStream myStream = new ByteArrayOutputStream();
				 * photo.compress(Bitmap.CompressFormat.PNG, 50, myStream);// (0
				 * byte[] myCopybyte = myStream.toByteArray(); int myLength =
				 * myCopybyte.length; String myCPdata =
				 * Base64.encodeToString(myCopybyte, 0, myLength,
				 * Base64.DEFAULT); UploadImageXml.sb.append("ceshiaaa.png");
				 * UploadImageXml.sb2.append(myCPdata);
				 * UploadImageXml.setImageView(MainActivity.this);
				 * Communicate.mark = true;
				 * 
				 * // new Thread(networkTask).start();
				 */
			}
		});

		RelativeLayout abus = (RelativeLayout) menuroot.findViewById(R.id.abus);
		abus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent = new Intent();
				// intent.setClass(MainActivity.this, AboutUs.class);
				// startActivity(intent);
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse("http://www.baidu.com");
				intent.setData(content_url);
				startActivity(intent);
			}
		});

		RelativeLayout bak = (RelativeLayout) menuroot.findViewById(R.id.bak);
		bak.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, FeedBack.class);
				startActivity(intent);
			}
		});

		RelativeLayout quitapp = (RelativeLayout) menuroot
				.findViewById(R.id.quitapp);
		quitapp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				// //鐢靛瓙鍥存爮娴嬭瘯
				// //璁剧疆瑙ｆ瀽鐨勬爣绛�
				// Communicate.sss.add("<SetElecFenceResult>");
				// Communicate.sss.add("</SetElecFenceResult>");
				// //璁剧疆鍙戦�佹暟鎹�
				// Communicate.xmlclassdata =
				// MatchString.addString(MainActivity.this, "SetElecFence.xml",
				// null, null);
				// //寮�濮嬪彂閫�
				// Communicate.mark = true;

				Intent intent = new Intent();
				intent.setClass(MainActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// TODO
		// 濡傛灉涓簄ull,璇存槑娌℃湁钃濈墮璁惧
		// if (mBluetoothAdapter == null) {
		// Toast.makeText(this, "娌℃湁钃濈墮璁惧", Toast.LENGTH_LONG).show();
		// if(!mBluetoothAdapter.isEnabled()){
		// mBluetoothAdapter.enable();
		// Toast.makeText(MainActivity.this, "钃濈墮宸插紑鍚�",
		// Toast.LENGTH_SHORT).show();
		// //寮�濮嬫悳绱�
		// searchDevice();
		// }else{
		// mBluetoothAdapter.disable();
		// Toast.makeText(MainActivity.this, "钃濈墮宸插叧闂�",
		// Toast.LENGTH_SHORT).show();
		// }
		// }
		// TODO

		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				mPairedDevicesList.add("宸查厤瀵癸細" + device.getName() + "\n"
						+ device.getAddress());
			}
		} else {
			Toast.makeText(this, "娌℃湁宸查厤瀵圭殑璁惧", Toast.LENGTH_SHORT).show();
		}

		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

	}

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
					// TODO
					// "宸茬粡杩炴帴" mConnectedDeviceName
					// mConversationArrayAdapter.clear();
					break;
				}
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				// mConversationArrayAdapter.add(mConnectedDeviceName+":  " +
				// readMessage);
				// 鎺ユ敹鏄剧ず鐨勪俊鎭�
				// TODO
				// text_chat.append(mConnectedDeviceName +"锛�" + readMessage +
				// "\n");
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"杩炴帴鍒� " + mConnectedDeviceName, Toast.LENGTH_SHORT)
						.show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			case 9993:
				searchDevice();
				break;
			case 20:
				getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.showSoftInput(layout, 0); // 鏄剧ず杞敭鐩�
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); // 鏄剧ず杞敭鐩�
				break;
			}
		}
	};

	private Bitmap getImageFromAssetsFile(String fileName) {
		Bitmap image = null;
		AssetManager am = MainActivity.this.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	// 寮�濮嬫椂鏈�鍏堣皟鐢�
	public void startBluetooth() {
		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mChatService == null)
				setupChat();
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "鍐嶆寜涓�娆￠��鍑虹▼搴�",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				LoginActivity.instance.finish();
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		MessageItem item = new MessageItem();
		if (intent.getStringExtra("iconRes") != null)
			item.iconRes = Integer.valueOf(intent.getStringExtra("iconRes"));
		if (intent.getStringExtra("name") != null)
			item.name = intent.getStringExtra("name");
		if (intent.getStringExtra("age") != null)
			item.age = intent.getStringExtra("age");
		if (intent.getStringExtra("gender") != null)
			item.gender = intent.getStringExtra("gender");
		if (intent.getStringExtra("height") != null)
			item.height = intent.getStringExtra("height");
		if (intent.getStringExtra("weight") != null)
			item.weight = intent.getStringExtra("weight");
		if (intent.getStringExtra("mark") != null) {
			if (intent.getStringExtra("mark").equals("mark")) {
				mMessageItems.add(item);
				slideAdapter.notifyDataSetChanged();
			} else {
				mMessageItems.set(
						Integer.valueOf(intent.getStringExtra("mark")), item);
				slideAdapter.notifyDataSetChanged();
			}
		}
	}

	// 璁＄畻鍑虹疮鍔犲拰
	public String addCheck(List<String> ss) {
		int i = 0;
		for (String s : ss) {
			// 16杩涘埗杞崲鎴�10杩涘埗杩涜璁＄畻
			i += Integer.valueOf(s, 16);
		}
		String add = Integer.toHexString(i);
		// 褰撹秴鍑轰袱浣嶅悗瑕佹埅鍙栧嚭鏈�鍚庝袱浣�
		return add.substring(add.length() - 2, add.length());
	}

	// 鍙戦�佹秷鎭�
	public void sendbluetooth() {
		// Send a message using content of the edit text widget
		// String message = text_input.getText().toString();
		// 鍗佽繘鍒惰浆鍗佸叚杩涘埗
		String vh = "";
		String sh = Integer.toHexString(1524);
		if (sh.length() < 6) {
			for (int i = 0; i < 6 - sh.length(); i++) {
				vh += "0";
			}
		}
		vh += sh;

		List<String> m = new ArrayList<String>();
		m.add("AA");
		m.add("02");

		// for(int i=0;i<3;i++){
		// m.add(vh.substring(2*i, 2+(2*i)));
		// }

		m.add("00");
		m.add("05");
		m.add("F4");

		m.add("00");
		m.add("00");
		m.add("00");
		m.add(addCheck(m));
		byte[] b = new byte[m.size()];
		for (int i = 0; i < m.size(); i++) {
			Integer integer = Integer.valueOf(m.get(i), 16);
			b[i] = integer.byteValue();
		}
		// String message = "AA"+"02"+"001524"+"00"+"0000";
		sendMessage(b);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		loginthread = false;

		// Stop the Bluetooth chat services
		if (mChatService != null)
			mChatService.stop();

		// Make sure we're not doing discovery anymore
		if (mBluetoothAdapter != null) {
			mBluetoothAdapter.cancelDiscovery();
		}
		// Unregister broadcast listeners
		this.unregisterReceiver(mReceiver);
	}

	/** 浣挎湰鍦扮殑钃濈墮璁惧鍙鍙戠幇 */
	private void ensureDiscoverable() {
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            A string of text to send.
	 */
	private void sendMessage(byte[] send) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, "没有连接", Toast.LENGTH_SHORT).show();
			return;
		}

		// Check that there's actually something to send
		if (send.length > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			// byte[] send = message.getBytes();
			mChatService.write(send);

			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);
		}
	}

	private void setupChat() {
		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothChatService(this, mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}

	// 杩炴帴钃濈墮璁惧
	private void linkDevice() {
		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		}
		int cou = mPairedDevicesList.size() + mNewDevicesList.size();
		if (cou == 0) {
			Toast.makeText(MainActivity.this, "没有搜索到可用的蓝牙设备",
					Toast.LENGTH_SHORT).show();
			return;
		}

		// 鎶婂凡缁忛厤瀵圭殑钃濈墮璁惧鍜屾柊鍙戠幇鐨勮摑鐗欒澶囩殑鍚嶇О閮芥斁鍏ユ暟缁勪腑锛屼互渚垮湪瀵硅瘽妗嗗垪琛ㄤ腑鏄剧ず
		strName = new String[cou];
		for (int i = 0; i < mPairedDevicesList.size(); i++) {
			strName[i] = mPairedDevicesList.get(i);
		}
		for (int i = mPairedDevicesList.size(); i < strName.length; i++) {
			strName[i] = mNewDevicesList.get(i - mPairedDevicesList.size());
		}
		address = strName[0].substring(strName[0].length() - 17);
		new AlertDialog.Builder(MainActivity.this)
				.setTitle("鎼滅储鍒扮殑钃濈墮璁惧锛�")
				.setSingleChoiceItems(strName, 0,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 褰撶敤鎴风偣鍑婚�変腑鐨勮摑鐗欒澶囨椂锛屽彇鍑洪�変腑鐨勮摑鐗欒澶囩殑MAC鍦板潃
								address = strName[which].split("\\n")[1].trim();
							}
						})
				.setPositiveButton("杩炴帴",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (address == null) {
									Toast.makeText(MainActivity.this,
											"璇峰厛杩炴帴澶栭儴钃濈墮璁惧",
											Toast.LENGTH_SHORT).show();
									return;
								}

								Log.i("sxd", "address:" + address);
								// Get the BLuetoothDevice object
								BluetoothDevice device = mBluetoothAdapter
										.getRemoteDevice(address);
								// Attempt to connect to the device
								mChatService.connect(device);
							}
						})
				.setNegativeButton("鍙栨秷",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).create().show();
	}

	// 鎼滅储钃濈墮璁惧钃濈墮璁惧
	private void searchDevice() {
		setProgressBarIndeterminateVisibility(true);
		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		}
		mNewDevicesList.clear();
		mBluetoothAdapter.startDiscovery();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "鎼滅储璁惧");
		menu.add(0, 2, 0, "鍙鍙戠幇");
		return true;
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 褰撳彂鐜颁竴涓柊鐨勮摑鐗欒澶囨椂
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// If it's already paired, skip it, because it's been listed
				// already
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					String s = "鏈厤瀵癸細 " + device.getName() + "\n"
							+ device.getAddress();
					if (!mNewDevicesList.contains(s))
						mNewDevicesList.add(s);
				}
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				if (mNewDevicesList.size() == 0) {
					Toast.makeText(MainActivity.this, "娌℃湁鍙戠幇鏂拌澶�",
							Toast.LENGTH_SHORT).show();
				}
				linkDevice();
			}
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			searchDevice();
			return true;
		case 2:
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;
		}
		return false;
	}

	private class SlideAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private ArrayList<UserBean> adapteruserBeans;

		public SlideAdapter(ArrayList<UserBean> list) {
			super();
			this.adapteruserBeans = list;
			mInflater = getLayoutInflater();
		}

		// 鑾峰緱鎬昏鏁�
		@Override
		public int getCount() {
			return adapteruserBeans.size();

		}

		// 鑾峰緱item鐨勫疄渚�
		@Override
		public Object getItem(int position) {
			return adapteruserBeans.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void addContactList(ArrayList<UserBean> avatarArrayList) {
			this.adapteruserBeans.clear();
			this.adapteruserBeans.addAll(avatarArrayList);
			notifyDataSetChanged();
		}

		// 鍒涘缓item
		@Override
		public View getView(int position, View convertView,
				final ViewGroup parent) {
			ViewHolder holder = null;
			ViewHolder2 holder2 = null;
			int type = getItemViewType(position);
			SlideView slideView = (SlideView) convertView;
			// 濡傛灉涓虹┖鍒欏垱寤烘柊鐨刬tem
			if (slideView == null) {
				switch (type) {
				case TYPE_ONE:
					View itemView = mInflater.inflate(R.layout.list_item,
							parent, false);

					slideView = new SlideView(MainActivity.this);
					slideView.setContentView(itemView);
					holder = new ViewHolder(slideView);
					slideView.setOnSlideListener(MainActivity.this);
					slideView.setTag(holder);
					break;
				case TYPE_TWO:
					View sthitemView = mInflater.inflate(R.layout.sth_item,
							parent, false);

					slideView = new SlideView(MainActivity.this);
					slideView.setContentView(sthitemView);

					holder2 = new ViewHolder2(slideView);
					slideView.setOnSlideListener(MainActivity.this);
					slideView.setTag(holder2);
					break;
				}
				// 濡傛灉涓嶄负绌哄垯灏嗕箣鍓嶅洖鏀剁殑view閲嶆柊鍒╃敤
			} else {
				switch (type) {
				case TYPE_ONE:
					holder = (ViewHolder) slideView.getTag();
					break;
				case TYPE_TWO:
					holder2 = (ViewHolder2) slideView.getTag();
					break;
				}
			}
			switch (type) {
			case TYPE_ONE:
				// 璁剧疆璧勬簮
				final UserBean item = adapteruserBeans.get(position);
				item.sv = slideView;
				item.sv.reset();
				holder.name.setText(item.getName());
				holder.name.setSelected(true);
				holder.age.setText(item.getAge() + "岁");
				holder.gender.setText(item.getSex());
				holder.height.setText(item.getHeight() + "cm");
				holder.weight.setText(item.getWeight() + "kg");
				holder.riv.setTag(item.getiConUrl());
				if (item.getiConUrl() != null) {
					settingAvatar(item.getiConUrl(), holder.riv);
				}
				File file = new File(getExternalCacheDir(), item.getiConUrl());
				Log.e("file=", file.getPath());
				holder.rl.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 閲嶆柊鍔犺浇鐐瑰嚮浜嬩欢
						Log.d("鐐瑰嚮", "-------");
					}
				});
				holder.leftHolder.setOnClickListener(MainActivity.this);
				holder.rightHolder.setOnClickListener(MainActivity.this);
				break;
			/*
			 * case TYPE_TWO: // 璁剧疆璧勬簮 MessageItem item2 =
			 * mMessageItems.get(position); item2.sv = slideView;
			 * item2.sv.reset();
			 * holder2.sthlistimageview.setImageResource(item2.iconRes);
			 * holder2.sthname.setText(item2.name);
			 * holder2.sthname.setSelected(true);
			 * holder2.belongname.setText(item2.age);
			 * holder2.sthdetail.setText(item2.gender);
			 * holder2.rl.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { // 閲嶆柊鍔犺浇鐐瑰嚮浜嬩欢
			 * Log.d("鐐瑰嚮", "-------"); } });
			 * holder2.leftHolder.setOnClickListener(MainActivity.this);
			 * holder2.rightHolder.setOnClickListener(MainActivity.this); break;
			 */
			}
			return slideView;
		}

		@Override
		public int getItemViewType(int position) {
			if (position < userBeans.size()) {
				return TYPE_ONE;
			} else {
				return TYPE_TWO;
			}
		}

	}

	private static class ViewHolder {

		public RoundImageView riv;
		public TextView name;
		public TextView age;
		public TextView gender;
		public TextView height;
		public TextView weight;
		public RelativeLayout rl;
		public ViewGroup leftHolder;
		public ViewGroup rightHolder;

		public ViewHolder(View view) {
			riv = (RoundImageView) view.findViewById(R.id.listimageview);
			name = (TextView) view.findViewById(R.id.setname);
			age = (TextView) view.findViewById(R.id.setage);
			gender = (TextView) view.findViewById(R.id.setgender);
			height = (TextView) view.findViewById(R.id.setheight);
			weight = (TextView) view.findViewById(R.id.setweight);
			rl = (RelativeLayout) view.findViewById(R.id.t);
			leftHolder = (ViewGroup) view.findViewById(R.id.left_holder);
			rightHolder = (ViewGroup) view.findViewById(R.id.right_holder);
		}

	}

	private static class ViewHolder2 {

		public RoundImageView sthlistimageview;
		public TextView sthname;
		public TextView belongname;
		public TextView sthdetail;
		public RelativeLayout rl;
		public ViewGroup leftHolder;
		public ViewGroup rightHolder;

		public ViewHolder2(View view) {
			sthlistimageview = (RoundImageView) view
					.findViewById(R.id.sthlistimageview);
			sthname = (TextView) view.findViewById(R.id.sthname);
			belongname = (TextView) view.findViewById(R.id.belongname);
			sthdetail = (TextView) view.findViewById(R.id.sthdetail);
			rl = (RelativeLayout) view.findViewById(R.id.stht);
			leftHolder = (ViewGroup) view.findViewById(R.id.left_holder);
			rightHolder = (ViewGroup) view.findViewById(R.id.right_holder);
		}

	}

	public class MessageItem {
		public int iconRes;
		public SlideView sv;
		public String name;// sthname
		public String age;// belongname
		public String gender;// sthdetail
		public String height;
		public String weight;
		public boolean mesmark;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SlideView slideView = userBeans.get(position - 1).sv;
		if (slideView.ismIsMoveClick()) {
			return;
		}
		if (slideView.close() && slideView.getScrollX() == 0) {
			// 璺宠浆Activity
			UserBean ub = userBeans.get(position - 1);
			Intent intent = new Intent(this, FunctionActivity.class);
			intent.putExtra("name", ub.getName());
			MyApplication.device = ub.getDevice();
			// intent.putExtra("mesmark", ub.mesmark);
			startActivity(intent);
			// 姝ゅ娣诲姞item鐨勭偣鍑讳簨浠�
			Toast.makeText(this, "鐐瑰嚮浜�" + position, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onSlide(View view, int status) {
		if (mLastSlideViewWithStatusOn != null
				&& mLastSlideViewWithStatusOn != view) {
			mLastSlideViewWithStatusOn.shrink();
		}
		if (status == SLIDE_STATUS_ON) {
			mLastSlideViewWithStatusOn = (SlideView) view;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.left_holder) {// 点击设置
			// 鑾峰彇鐐瑰嚮鐨勪綅缃�
			UserBean mi = userBeans.get(mListView.getPosition());
			if (mi.mesmark == false) {
				// 璺宠浆Activity
				Intent intent = new Intent(this, SetActivity.class);
				intent.putExtra("Action", "UPDATE");
				intent.putExtra("iconRes", String.valueOf(mi.getiConUrl()));
				intent.putExtra("name", mi.getName());
				intent.putExtra("age", mi.getAge());
				intent.putExtra("gender", mi.getSex());
				intent.putExtra("height", mi.getHeight());
				intent.putExtra("weight", mi.getWeight());
				intent.putExtra("device", mi.getDevice());
				intent.putExtra("address", mi.getAddress());
				intent.putExtra("mark", String.valueOf(mListView.getPosition()));
				startActivityForResult(intent, 0);
			} else {
				// 璺宠浆Activity
				Intent intent = new Intent(this, SetSthActivity.class);
				intent.putExtra("iconRes", String.valueOf(mi.getiConUrl()));
				intent.putExtra("sthname", mi.getName());
				intent.putExtra("belongname", mi.getAge());
				intent.putExtra("sthdetail", mi.getSex());
				intent.putExtra("mark", String.valueOf(mListView.getPosition()));
				startActivity(intent);
			}
		} else if (v.getId() == R.id.right_holder) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("删除用户").setMessage("确定要删除用户吗？")
					.setNegativeButton("取消", null);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Log.e("main==", mListView.getPosition() + "");
							UserBean userBean = userBeans.get(mListView
									.getPosition());
							String device = userBean.getDevice();// 使用设备ID来删除对应的记录
							userBeans.remove(userBean);
							slideAdapter.notifyDataSetChanged();
							if (userBeans.size() == 0) {
								mmain_tv_hint.setVisibility(View.VISIBLE);
							}
							List<String> s = new ArrayList<String>();
							s.add(device);
							s.add("person|D|");
							List<String> starttags2 = new ArrayList<String>();
							starttags2.add("<devicenum>");
							starttags2.add("<content>");
							Communicate.sss.add("<DealbeguardResult>");
							Communicate.sss.add("</DealbeguardResult >");
							Communicate.xmlclassdata = MatchString.addString(
									MainActivity.this, "Dealbeguard.xml", s,
									starttags2);
							// Communicate cc = new Communicate();
							// cc.start();
							Communicate.mark = true;
						}
					});
			builder.show();
		}
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				RefreshTime.setRefreshTime(getApplicationContext(),
						df.format(new Date()));
				onLoad();
				// // 涓嬫媺鍒锋柊鍚庡紑濮嬭姹傛坊鍔犱汉鍜屾坊鍔犵墿鏁版嵁
				// // 璁剧疆瑕佹彃鍏ョ殑鏁版嵁
				// StringBuffer sb = new StringBuffer();
				// // 瀛樺叆鐢佃瘽鍙风爜
				// sb.append("13000000000");
				// List<String> s = new ArrayList<String>();
				// s.add(sb.toString());
				// // 璁剧疆鎻掑叆鐨勬爣绛�
				// List<String> starttags = new ArrayList<String>();
				// starttags.add("<phonenum>");
				// // 璁剧疆瑙ｆ瀽鐨勬爣绛�
				// Communicate.sss.add("<GetGuardianInfoResult>");
				// Communicate.sss.add("</GetGuardianInfoResult>");
				// // 璁剧疆鍙戦�佹暟鎹�
				// Communicate.xmlclassdata = MatchString.addString(
				// MainActivity.this, "GetGuardianInfo.xml", s, starttags);
				// // 寮�濮嬪彂閫�
				// Communicate.mark = true;
				initData();
			}
		}, 50);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				onLoad();
			}
		}, 50);
	}

	private void onLoad() {
		mListView.setRefreshTime(RefreshTime
				.getRefreshTime(getApplicationContext()));
		mListView.stopRefresh();
	}

	/**
	 * 弹出检测到新版本对话框 用户选择更新与否
	 * 
	 * @param mainActivity
	 */

	private void startAlertDialog(MainActivity mainActivity, int i,
			final float newVersion) {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		if (i == 1) {
			builder.setTitle("检测到新版本")
					.setMessage("是否更新？")
					.setPositiveButton("更新",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									Log.e("main", "点了更新，进入了点击方法");
									pdDialog = new ProgressDialog(
											MainActivity.this);
									pdDialog.setTitle("更新中");
									pdDialog.setMessage("请稍后...");
									pdDialog.setMax(100);
									pdDialog.setProgress(11);
									pdDialog.setCancelable(true);
									pdDialog.show();
									new Thread() {

										@Override
										public void run() {
											try {
												int j = new ImgHttpClient(
														MainActivity.this)
														.UpdateVersion("version_"
																+ newVersion
																+ ".apk");
												if (j == 1) {
													Message msg = Message
															.obtain();
													msg.what = DOWN_APK_SUCCESS;
													msg.obj = "version_"
															+ newVersion
															+ ".apk";
													myhandler.sendMessage(msg);
												} else {
													Message msg = Message
															.obtain();
													myhandler
															.sendEmptyMessage(DOWN_APK_ERROR);
												}

											} catch (IOException e) {

												e.printStackTrace();
											}
										}
									}.start();

								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {

								}
							}).create().show();
		}
		if (i == -1) {
			Toast.makeText(MainActivity.this, "恭喜你，当前已是最新版本！",
					Toast.LENGTH_LONG).show();
		}

	}

	private void deleteAllFiles(File root) {
		File files[] = root.listFiles();
		if (files != null)
			for (File f : files) {
				if (f.isDirectory()) { // 判断是否为文件夹
					deleteAllFiles(f);
					try {
						f.delete();
					} catch (Exception e) {
					}
				} else {
					if (f.exists()) { // 判断是否存在
						deleteAllFiles(f);
						try {
							f.delete();
						} catch (Exception e) {
						}
					}
				}
			}
	}

	public void settingAvatar(final String getiConUrl, final RoundImageView riv) {
		File file = new File(getExternalCacheDir(), getiConUrl);// 根据用户id在缓存下查找头像
		final String path = file.getAbsolutePath();
		if (file.exists()) {// 在缓存中找到了这张图片
			Bitmap bm = BitmapFactory.decodeFile(path);
			Log.e("存在此图片", "存在此图片");
			riv.setImageBitmap(bm);
			Log.e("mian", "头像设置成功了");
			return;
		}
		if (!file.exists()) {
			Log.e("缓存中不存在此图片", "缓存中不存在此图片");
			// 缓存中没有 去服务器拿图片
			new Thread() {
				public void run() {
					try {
						int code = new ImgHttpClient(MainActivity.this)
								.downForm(getiConUrl);
						if (code == 1) {
							Log.e("main", "下载了图片可以更改Ui");
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Bitmap bm = BitmapFactory.decodeFile(path);
									riv.setImageBitmap(bm);
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
		riv.setImageResource(R.drawable.icon_sb);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		initData();
	}

}
