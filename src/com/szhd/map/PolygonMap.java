package com.szhd.map;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.szhd.guardian.R;
import com.szhd.ui.ElectricFenceActivity;

public class PolygonMap extends Activity implements OnMapClickListener,
		OnMapLongClickListener, OnMarkerClickListener, OnMarkerDragListener {

	private BaiduMap mbaidumap;
	private MapView mmapview;
	private UiSettings mUiSettings;
	private Double mlongitude;
	private List<Marker> arraymarker = new ArrayList<Marker>();
	private List<LatLng> arraylatlng = new ArrayList<LatLng>();
	private List<LatLng> twolatlng = new ArrayList<LatLng>();
	private RelativeLayout electronic_fence;
	private RelativeLayout re_selection;
	private ImageView electronic_return;
	private TextView textView4;
	private LocationClient mLocationClient;
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_local);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.electronic_map);
		SDKInitializer.initialize(getApplicationContext());
		init();
	}

	private void init() {

		// 获取LocationClient
		mLocationClient = new LocationClient(this);

		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		mLocationClient.setLocOption(option);

		// 初始化地图
		mmapview = (MapView) findViewById(R.id.textbmapView);
		// 隐藏地图上百度地图logo图标
		mmapview.removeViewAt(1);
		// 隐藏自带的地图缩放控件
		mmapview.showZoomControls(false);
		mbaidumap = mmapview.getMap();

		mbaidumap.setMyLocationEnabled(true);
		MyLocationListener mListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mListener);
		mLocationClient.start();

		// 获得UI设置
		mUiSettings = mbaidumap.getUiSettings();
		// 是否启用旋转手势
		mUiSettings.setRotateGesturesEnabled(false);
		// 是否启用俯视手势
		mUiSettings.setOverlookingGesturesEnabled(false);
		// 是否启用指南针图层
		mUiSettings.setCompassEnabled(false);
		mbaidumap.setOnMapClickListener(this);
		mbaidumap.setOnMapLongClickListener(this);
		mbaidumap.setOnMarkerClickListener(this);
		mbaidumap.setOnMarkerDragListener(this);
		electronic_fence = (RelativeLayout) findViewById(R.id.Electronic_fence);
		re_selection = (RelativeLayout) findViewById(R.id.Re_selection);
		electronic_return = (ImageView) findViewById(R.id.Electronic_return);
		textView4 = (TextView) findViewById(R.id.textView4);

		electronic_fence.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PolygonMap.this,
						ElectricFenceActivity.class);
				startActivity(intent);
			}
		});

		re_selection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearClick();
				arraymarker.clear();
				arraylatlng.clear();
				twolatlng.clear();
				mbaidumap.setOnMapClickListener(PolygonMap.this);
			}
		});

		electronic_return.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		textView4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			MyLocationData locData = new MyLocationData.Builder()
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();

			// 设置图标在地图上的位置
			mbaidumap.setMyLocationData(locData);

			// 开始移动百度地图的定位地点到中心位置
			LatLng ll = new LatLng(location.getLatitude(),
					location.getLongitude());
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16.0f);
			mbaidumap.animateMapStatus(u);
		}

	}

	// 画多边形
	private void drawPolygon(List<LatLng> pts) {
		// 添加多边形
		OverlayOptions ooPolygon = new PolygonOptions().points(pts)
				.stroke(new Stroke(3, 0xFF12B7F5)).fillColor(0x6612B7F5);
		mbaidumap.addOverlay(ooPolygon);
	}

	// 画线
	private void drawLine(List<LatLng> pts) {
		if (pts.size() > 1) {
			// 添加普通折线绘制
			OverlayOptions ooPolyline = new PolylineOptions().width(4)
					.color(0xFF12B7F5).points(pts);
			Polyline mpolyline = (Polyline) mbaidumap.addOverlay(ooPolyline);
		}
	}

	// 画maker
	private Marker drawMaker(LatLng ll) {
		MarkerOptions ooA = new MarkerOptions().position(ll).icon(bdA)
				.zIndex(9).draggable(true);
		return (Marker) (mbaidumap.addOverlay(ooA));
	}

	public void clearClick() {
		// 清除所有图层
		mmapview.getMap().clear();
	}

	// 重绘
	public void reDraw() {
		for (LatLng ll : arraylatlng) {
			drawMaker(ll);
		}
		drawLine(arraylatlng);
	}

	@Override
	public void onMapClick(LatLng ll) {
		arraymarker.add(drawMaker(ll));
		arraylatlng.add(ll);
		// 取出list中最后两个点
		if (arraylatlng.size() > 1) {
			twolatlng.add(arraylatlng.get(arraylatlng.size() - 1));// 最后一个点
			twolatlng.add(arraylatlng.get(arraylatlng.size() - 2));// 倒数第二个点
			drawLine(twolatlng);
			twolatlng.clear();
		}
	}

	@Override
	public boolean onMapPoiClick(MapPoi poi) {
		return false;
	}

	@Override
	public void onMapLongClick(LatLng arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		// 判断是否为起始点,地图上的点是否可以画出一个图形
		if (compareLatLng(marker.getPosition(), arraymarker.get(0)
				.getPosition())
				&& arraymarker.size() > 2) {
			clearClick();
			// 画多边形
			drawPolygon(arraylatlng);
			// 屏蔽map点击事件
			mbaidumap.setOnMapClickListener(new OnMapClickListener() {
				@Override
				public boolean onMapPoiClick(MapPoi arg0) {
					return false;
				}

				@Override
				public void onMapClick(LatLng arg0) {
				}
			});
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					PolygonMap.this);
			builder.setTitle("提示").setMessage("是否删除此坐标点？")
					.setNegativeButton("否", null);
			builder.setPositiveButton("是",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							for (int i = 0; i < arraymarker.size(); i++) {
								if (compareLatLng(marker.getPosition(),
										arraymarker.get(i).getPosition())) {
									arraymarker.remove(i);
									arraylatlng.remove(i);
									clearClick();
									reDraw();
								}
							}
						}
					});
			builder.show();
		}
		return false;
	}

	@Override
	public void onMarkerDrag(Marker marker) {
		Log.d("onMarkerDrag", marker.getPosition().latitude + "");
		Log.d("onMarkerDrag", marker.getPosition().longitude + "");
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		// 判断移动点是否为终点,地图上的点是否可以画出一个图形,判断与起始点的距离,如果距离足够小，绘制多边形
		if (compareToDouble(mlongitude,
				arraylatlng.get(arraylatlng.size() - 1).longitude)
				&& arraymarker.size() > 3
				&& DistanceUtil.getDistance(marker.getPosition(),
						arraylatlng.get(0)) < 1000) {
			clearClick();
			// 画多边形
			drawPolygon(arraylatlng.subList(0, arraylatlng.size() - 1));
			// 屏蔽map点击事件
			mbaidumap.setOnMapClickListener(new OnMapClickListener() {
				@Override
				public boolean onMapPoiClick(MapPoi arg0) {
					return false;
				}

				@Override
				public void onMapClick(LatLng arg0) {
				}
			});
		} else {
			Log.d("onMarkerDragEnd", marker.getPosition().latitude + "");
			Log.d("onMarkerDragEnd", marker.getPosition().longitude + "");
			for (int i = 0; i < arraymarker.size(); i++) {
				if (compareToDouble(mlongitude, arraylatlng.get(i).longitude)) {
					arraymarker.set(i, marker);
					arraylatlng.set(i, marker.getPosition());
					clearClick();
					reDraw();
				}
			}
		}
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		mlongitude = marker.getPosition().longitude;
		Log.d("已有的", arraymarker.get(1).getPosition().latitude + "");
		Log.d("已有的", arraymarker.get(1).getPosition().longitude + "");
		Log.d("onMarkerDragStart", marker.getPosition().latitude + "");
		Log.d("onMarkerDragStart", marker.getPosition().longitude + "");
	}

	private boolean compareLatLng(LatLng l1, LatLng l2) {
		boolean result = false;
		if (compareToDouble(l1.latitude, l2.latitude)) {
			if (compareToDouble(l1.longitude, l2.longitude)) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		return result;
	}

	private boolean compareToDouble(Double d1, Double d2) {
		if (new BigDecimal(d1).compareTo(new BigDecimal(d2)) == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onPause() {
		mmapview.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mmapview.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mmapview.onDestroy();
		mLocationClient.stop();
		super.onDestroy();
	}

}
