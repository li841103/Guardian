package com.szhd.guardian;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.szhd.bean.QueryUserBean;
import com.szhd.util.MatchString;
import com.szhd.webservice.Communicate;

public class ChangeAdmin extends Activity {

	private ListView lv;
	private SleepAdapter sa;
	private TextView textView3;
	private ImageView regquback;
	private ImageView imageView;
	private boolean loginthread = true;
	private Handler myhandler;
	private List<QueryUserBean> userBeans = new ArrayList<QueryUserBean>();
	private List<String> MyList;
	private List<String> BackList;
	private ViewHolder holder = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changeadmin);
		init();
		Log.e("main", "onCreate方法被调用！");
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				String name = userBeans.get(position).getName();
				new AlertDialog.Builder(ChangeAdmin.this)
						.setTitle("权限移交")
						.setMessage("确定将权限移交给 " + name + " ?")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										QueryUserBean item = sa
												.getItem(position);
										// item.set
										// itemAtPosition.iv1
										// .setImageResource(R.drawable.star_yel);
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								}).create().show();
				Log.e("激活列表项点击事件", "激活列表项点击事件");
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e("main", "onResume方法被调用！");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("main", "onPause方法被调用！");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.e("main", "onStop方法被调用！");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("main", "onDestroy方法被调用！");
	}

	private void init() {
		imageView = (ImageView) findViewById(R.id.imageView1);
		Intent intent = getIntent();
		String extra = intent.getStringExtra("first_Guardian");
		if (extra.equals("true")) {
			imageView.setImageResource(R.drawable.star_yel);
		}
		lv = (ListView) findViewById(R.id.changeadminlistview);
		myhandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					MyList = new ArrayList<String>();
					BackList = (List<String>) msg.obj;// 源数据
					for (int j = 0; j < BackList.size() / 6; j++) {
						for (int i = 0; i < 6; i++) {
							MyList.add(BackList.get(BackList.size() / 6 * i + j));
						}
					}
					for (String s : BackList) {

						Log.e("main", "BackList=" + s);
					}

					// 将获取的List转为数组
					String[] strings = MyList
							.toArray(new String[MyList.size()]);

					String[][] strings2 = new String[MyList.size() / 6][6];
					int o = 0;
					for (int i = 0; i < strings.length / 6; i++) {
						for (int j = 0; j < 6 && o < strings.length; j++, o++) {
							strings2[i][j] = strings[o];
							Log.e("main", "strings2[" + i + "][" + j + "]="
									+ strings2[i][j]);
						}
					}
					// 将数组转为二维数组
					for (int i = 0; i < strings.length / 6; i++) {
						QueryUserBean userBean = new QueryUserBean();
						userBean.setDef1(strings2[i][0]);
						userBean.setId(Integer.parseInt(strings2[i][1]));
						userBean.setFirst_Guardian((strings2[i][2]));
						userBean.setPhonenum(strings2[i][3]);
						userBean.setRelationship(strings2[i][4]);
						userBean.setName(strings2[i][5]);
						userBeans.add(userBean);
					}
					for (QueryUserBean userBean : userBeans) {
						Log.e("userBeans=", userBean.toString());
					}
					if (userBeans != null) {
						Log.e("UserBeans=", userBeans.size() + "");
					}
					sa = new SleepAdapter(ChangeAdmin.this, userBeans);
					lv.setAdapter(sa);
					sa.notifyDataSetChanged();

					// sa.addCountAll(userBeans);
					break;
				default:
					Toast.makeText(ChangeAdmin.this, "没有监护人信息！",
							Toast.LENGTH_LONG).show();
				}

			};
		};

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (loginthread) {
						if (Communicate.STATE == true) {
							if (Communicate.RESULT.get(0) != null) {
								if (Communicate.RESULT.get(0).equals(
										"QueryAllUser")) {
									Message message = Message.obtain();
									message.what = 1;
									message.obj = Communicate.RESULT;
									myhandler.sendMessage(message);
									// 关闭发送状态
									Communicate.STATE = false;
								}
							}

						}
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		Communicate.sss.add("<def1>");
		Communicate.sss.add("</def1>");
		Communicate.sss.add("<id>");
		Communicate.sss.add("</id>");
		Communicate.sss.add("<first_Guardian>");
		Communicate.sss.add("</first_Guardian>");
		Communicate.sss.add("<phonenum>");
		Communicate.sss.add("</phonenum>");
		Communicate.sss.add("<relationship>");
		Communicate.sss.add("</relationship>");
		Communicate.sss.add("<name>");
		Communicate.sss.add("</name>");

		Communicate.xmlclassdata = MatchString.addString(ChangeAdmin.this,
				"QueryAllUser.xml", null, null);
		Communicate.mark = true;

		regquback = (ImageView) findViewById(R.id.regquback);
		regquback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		textView3 = (TextView) findViewById(R.id.textView3);
		textView3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	private class SleepAdapter extends BaseAdapter {
		List<QueryUserBean> ubarr;
		private LayoutInflater mInflater;

		public SleepAdapter(Context context, List<QueryUserBean> userBeans) {
			this.mInflater = LayoutInflater.from(context);
			this.ubarr = userBeans;
		}

		@Override
		public int getCount() {
			return ubarr.size();
		}

		@Override
		public QueryUserBean getItem(int position) {
			return this.ubarr.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			QueryUserBean queryUserBean = userBeans.get(position);
			// ViewHolder holder = null;
			// 如果为空则创建新的item
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.changeadminitem,
						parent, false);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
				// 如果不为空则将之前回收的view重新利用
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv1.setText(queryUserBean.getName().toString());
			holder.tv2.setText(queryUserBean.getRelationship().toString());
			holder.tv3.setText(queryUserBean.getPhonenum().toString());
			if (queryUserBean.getFirst_Guardian().equals("true")) {
				holder.iv1.setImageResource(R.drawable.star_yel);
			}
			return convertView;
		}
	}

	private class ViewHolder {
		private TextView tv1;
		private TextView tv2;
		private TextView tv3;
		private ImageView iv1;

		public ViewHolder(View view) {
			tv1 = (TextView) view.findViewById(R.id.changeadminname);
			tv2 = (TextView) view.findViewById(R.id.changeadminsh);
			tv3 = (TextView) view.findViewById(R.id.changeadminnum);
			iv1 = (ImageView) view.findViewById(R.id.imageView1);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
