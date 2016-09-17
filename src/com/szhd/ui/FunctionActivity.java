package com.szhd.ui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.szhd.guardian.Healthy_Main;
import com.szhd.guardian.R;
import com.szhd.map.MapFragment;

@SuppressLint("NewApi")
public class FunctionActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {

	private NoScrollViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;

	private List<ChangeColorIconWithTextView> mTabIndicator = new ArrayList<ChangeColorIconWithTextView>();
	private ImageView back;
	private TextView textView1;
	private TextView setname;
	private MapFragment tabf;
	private Healthy_Main healthyf;
	private WarningFragment warningf;
	private FunctionFragment functionf;
	private ImageView findaddr;
	private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.function_activity);

		setOverflowShowingAlways();
		// getActionBar().setDisplayShowHomeEnabled(false);
		mViewPager = (NoScrollViewPager) findViewById(R.id.id_viewpager);
		// 设置viewpager不能左右滑动
		mViewPager.setNoScroll(true);
		initDatas();

		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
	}

	private void initDatas() {
		textView1 = (TextView) findViewById(R.id.textView1);
		back = (ImageView) findViewById(R.id.functionback);
		setname = (TextView) findViewById(R.id.name);
		findaddr = (ImageView) findViewById(R.id.findaddr);
		title = (TextView) findViewById(R.id.title);

		findaddr.setOnClickListener(new OnClickListener() {// 查找城市
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(FunctionActivity.this, FindAddrActivity.class);
				intent.putExtra("city", MapFragment.nowcity);
				startActivity(intent);
			}
		});

		back.setOnClickListener(new OnClickListener() {
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

		// if(intent.getStringExtra("name") != null)
		// setname.setText(intent.getStringExtra("name"));
		// setname.setSelected(true);
		// 显示当前日期
		// final String date = GetNowDate.getTodayData();
		// GetNowDate.getBackData();
		// GetNowDate.getTomoData();

		tabf = new MapFragment();
		healthyf = new Healthy_Main();
		warningf = new WarningFragment();
		functionf = new FunctionFragment();

		mTabs.add(tabf);
		mTabs.add(healthyf);
		mTabs.add(warningf);
		mTabs.add(functionf);

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mTabs.get(arg0);
			}
		};
		initTabIndicator();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// 更新intent
		setIntent(intent);
		// 共享intent
		getIntent().putExtras(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu){
	// //getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	private void initTabIndicator() {
		ChangeColorIconWithTextView one = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_one);
		ChangeColorIconWithTextView two = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_two);
		ChangeColorIconWithTextView three = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_three);
		ChangeColorIconWithTextView four = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_four);

		mTabIndicator.add(one);
		mTabIndicator.add(two);
		mTabIndicator.add(three);
		mTabIndicator.add(four);

		Intent intent = getIntent(); // 用于激活它的意图对象
		boolean mesmark = intent.getBooleanExtra("mesmark", false);

		one.setOnClickListener(this);
		if (mesmark == true) {
			two.setOnClickListener(this);
		}
		three.setOnClickListener(this);
		four.setOnClickListener(this);

		one.setIconAlpha(1.0f);
	}

	@Override
	public void onPageSelected(int arg0) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

		if (positionOffset > 0) {
			ChangeColorIconWithTextView left = mTabIndicator.get(position);
			ChangeColorIconWithTextView right = mTabIndicator.get(position + 1);

			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onClick(View v) {
		resetOtherTabs();

		switch (v.getId()) {
		case R.id.id_indicator_one:
			mTabIndicator.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			title.setText("定位");
			findaddr.setVisibility(View.VISIBLE);
			break;
		case R.id.id_indicator_two:
			Log.e("监听到点击事件", "监听到点击事件");
			mTabIndicator.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			title.setText("健康");
			findaddr.setVisibility(View.INVISIBLE);
			break;
		case R.id.id_indicator_three:
			mTabIndicator.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			title.setText("报警");
			findaddr.setVisibility(View.INVISIBLE);
			break;
		case R.id.id_indicator_four:
			mTabIndicator.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3, false);
			title.setText("功能");
			findaddr.setVisibility(View.INVISIBLE);
			break;
		}
	}

	/**
	 * 重置其他的Tab
	 */
	private void resetOtherTabs() {
		for (int i = 0; i < mTabIndicator.size(); i++) {
			mTabIndicator.get(i).setIconAlpha(0);
		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	private void setOverflowShowingAlways() {
		try {
			// true if a permanent menu key is present, false otherwise.
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
