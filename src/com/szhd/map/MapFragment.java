package com.szhd.map;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BikingRouteOverlay;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.szhd.bean.ElectricFenceBean;
import com.szhd.constant.SetBikingXml;
import com.szhd.constant.SetDrivingXml;
import com.szhd.constant.SetTransitXml;
import com.szhd.constant.SetWalkingXml;
import com.szhd.guardian.HistoricalPath;
import com.szhd.guardian.R;
import com.szhd.util.MatchString;
import com.szhd.util.MyApplication;
import com.szhd.webservice.Communicate;

public class MapFragment extends Fragment implements
		OnGetGeoCoderResultListener, OnGetRoutePlanResultListener {

	BaiduMap mBaiduMap = null;
	MapView mMapView = null;
	public MapStatus mMapStatus;
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	RoutePlanSearch mRSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private Button zoomInBtn;
	private Button zoomOutBtn;
	private UiSettings mUiSettings;
	private float currentZoomLevel;
	private float maxZoomLevel;
	private float minZoomLevel;
	private View rootview;
	private Button reversegeocode;
	private Button trajectory;
	private String mname = "";
	private static final int DINGWEI = 1;
	// 当前城市
	public static String nowcity = "";
	// 纬度偏移量
	private float latitudedeviation = 0.00374531687912f;
	// 经度偏移量
	private float longitudedeviation = 0.008774687519f;
	// 纬度
	private float latitude = 30.555588737979864f;
	// private float latitude = 30.003746f;
	// 经度
	private float longitu = 104.08059585913333f;

	// private float longitu = 104.00877f;

	// //纬度
	// private float latitude = 30.548768f;
	// //经度
	// private float longitu = 104.065600f;

	LatLng cenpt = new LatLng(latitude, longitu);

	// true为位置定位，false为选点定位
	private boolean locationsource = true;
	// 选择的点的经纬度
	private LatLng choice;
	private Polyline mPolyline;

	private boolean clear = true;
	private Handler myhandler;
	private LatLng carll;

	private void setOnClickListener() {
		zoomInBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				float zoomLevel = mBaiduMap.getMapStatus().zoom;
				if (zoomLevel <= 18) {
					mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
					zoomOutBtn.setEnabled(true);
				} else {
					zoomInBtn.setEnabled(false);
				}
			}
		});
		zoomOutBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				float zoomLevel = mBaiduMap.getMapStatus().zoom;
				if (zoomLevel > 4) {
					mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
					zoomInBtn.setEnabled(true);
				} else {
					zoomOutBtn.setEnabled(false);
				}
			}
		});
		reversegeocode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 先去数据库里面取得手表传来的经纬度信息
				new Thread() {
					public void run() {

						Log.e("发送请求获取实时位置", "发送请求获取实时位置+devioce="
								+ MyApplication.device);
						List<String> s = new ArrayList<String>();
						s.add(MyApplication.device);
						List<String> starttags2 = new ArrayList<String>();
						starttags2.add("<devicenum>");
						Communicate.sss.add("<def1>");
						Communicate.sss.add("</def1>");
						Communicate.sss.add("<position>");
						Communicate.sss.add("</position>");

						Communicate.xmlclassdata = MatchString.addString(
								getContext(), "GetAddress.xml", s, starttags2);
						Communicate.mark = true;
						try {
							Thread.sleep(4500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					};
				}.start();

			}
		});

		trajectory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), HistoricalPath.class);
				intent.putExtra("nowcity", nowcity);
				startActivity(intent);
				// LatLng a1 = new LatLng(30.553093018143446,
				// 104.07958077390704);
				// LatLng a2 = new LatLng(30.552836445140205,
				// 104.07979636722943);
				// LatLng a3 = new LatLng(30.552883094828104,
				// 104.07992213000082);
				// double aa = DistanceUtil.getDistance(a1, a2);
				// double bb = DistanceUtil.getDistance(a1, a3);
				// Log.d("1距离为", aa+"");
				// Log.d("2距离为", bb+"");

				// if(clear == true){
				// // 添加普通折线绘制
				// LatLng p1 = new LatLng(30.552509896690786,
				// 104.07437060194917);
				//
				// List<LatLng> points = new ArrayList<LatLng>();
				// points.add(p1);

				// OverlayOptions ooPolyline = new PolylineOptions().width(10)
				// .color(0xAAFF0000).points(points);
				// mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
				// mBaiduMap.addOverlay(new MarkerOptions().position(p1)
				// .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_start)));
				// mBaiduMap.addOverlay(new MarkerOptions().position(p33)
				// .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_en)));
				// MapStatus mms = new MapStatus.Builder()
				// .target(p1)
				// .zoom(18)
				// .build();
				// //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
				// MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				// .newMapStatus(mms);
				// // 改变地图状态
				// mBaiduMap.setMapStatus(mMapStatusUpdate);
				// clear = false;
				// }else{
				// //定位
				// dingwei();
				// clear = true;
				// }
			}
		});

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getActivity().getApplicationContext());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != rootview) {
			ViewGroup parent = (ViewGroup) rootview.getParent();
			if (null != parent) {
				parent.removeView(rootview);
			}
		} else {
			rootview = inflater.inflate(R.layout.activity_geocoder, container,
					false);
			// 地图初始化
			mMapView = (MapView) rootview.findViewById(R.id.bmapView);
			zoomInBtn = (Button) rootview.findViewById(R.id.zoomin);
			zoomOutBtn = (Button) rootview.findViewById(R.id.zoomout);
			reversegeocode = (Button) rootview
					.findViewById(R.id.reversegeocode);
			// 轨迹回放
			trajectory = (Button) rootview.findViewById(R.id.trajectory);
			// 隐藏地图上百度地图logo图标
			mMapView.removeViewAt(1);
			mBaiduMap = mMapView.getMap();
			// 设定中心点坐标
			// 定义地图状态
			mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18).build();
			// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
			MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
					.newMapStatus(mMapStatus);
			// 改变地图状态
			mBaiduMap.setMapStatus(mMapStatusUpdate);
			mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {
				// 地图长按事件监听回调函数
				public void onMapLongClick(final LatLng point) {
					Log.d("地图选点", point.latitude + "--" + point.longitude);
					// 画出坐标杆图，添加覆盖物
					onLongDingWei(point);
				}
			});

			setOnClickListener();

			BaiduMap.OnMapStatusChangeListener mapStatusChangeListener = new OnMapStatusChangeListener() {
				@Override
				public void onMapStatusChange(MapStatus arg0) {

					maxZoomLevel = mBaiduMap.getMaxZoomLevel();
					minZoomLevel = mBaiduMap.getMinZoomLevel();

					currentZoomLevel = arg0.zoom;

					if (currentZoomLevel >= maxZoomLevel) {
						currentZoomLevel = maxZoomLevel;
					} else if (currentZoomLevel <= minZoomLevel) {
						currentZoomLevel = minZoomLevel;
					}

					if (currentZoomLevel == maxZoomLevel) {
						// 设置地图缩放等级为上限
						MapStatusUpdate u = MapStatusUpdateFactory
								.zoomTo(currentZoomLevel);
						mBaiduMap.animateMapStatus(u);
						zoomInBtn.setEnabled(false);
					} else if (currentZoomLevel == minZoomLevel) {
						// 设置地图缩放等级为下限
						MapStatusUpdate u = MapStatusUpdateFactory
								.zoomTo(currentZoomLevel);
						mBaiduMap.animateMapStatus(u);
						zoomOutBtn.setEnabled(false);
					} else {
						if (!zoomInBtn.isEnabled() || !zoomOutBtn.isEnabled()) {
							zoomInBtn.setEnabled(true);
							zoomOutBtn.setEnabled(true);
						}
					}

				}

				@Override
				public void onMapStatusChangeFinish(MapStatus arg0) {

				}

				@Override
				public void onMapStatusChangeStart(MapStatus arg0) {

				}
			};
			mBaiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);

			// 设置比例尺位置
			mBaiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
				@Override
				public void onMapLoaded() {
					int[] location = new int[2];
					reversegeocode.getLocationOnScreen(location);

					// DisplayMetrics dm = getResources().getDisplayMetrics();
					Point size = new Point();
					// size.x = dm.widthPixels/5;
					// size.y = (int) (dm.heightPixels-(0.9*dm.ydpi));

					size.x = location[0] + dp2px(60);
					size.y = location[1] - dp2px(50);

					// 设置比例尺控件的位置，在onMapLoadFinish后生效
					mMapView.setScaleControlPosition(size);
				}
			});
			// 隐藏自带的地图缩放控件
			mMapView.showZoomControls(false);
			// 获得UI设置
			mUiSettings = mBaiduMap.getUiSettings();
			// 是否启用旋转手势
			mUiSettings.setRotateGesturesEnabled(false);
			// 是否启用俯视手势
			mUiSettings.setOverlookingGesturesEnabled(false);
			// 是否启用指南针图层
			mUiSettings.setCompassEnabled(false);
			// 初始化搜索模块，注册事件监听
			mSearch = GeoCoder.newInstance();
			mSearch.setOnGetGeoCodeResultListener(this);
			// 初始化搜索模块，注册事件监听
			mRSearch = RoutePlanSearch.newInstance();
			mRSearch.setOnGetRoutePlanResultListener(this);
			// 定位
			dingwei();

			myhandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					switch (msg.what) {
					case DINGWEI:

						String[] strarr = (String[]) msg.obj;
						String jindu = strarr[0];
						String weidu = strarr[1];
						long Longjindu = Long.parseLong(jindu, 16);
						long Longweidu = Long.parseLong(weidu, 16);
						longitu = Longjindu / 100000 + longitudedeviation;
						latitude = Longweidu / 100000 + latitudedeviation;

						Log.e("开始定位", "longitu=" + longitu + "latitude"
								+ latitude);
						cenpt = new LatLng(latitude, longitu);

						dingwei();
						break;

					default:
						break;
					}
				};
			};
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true) {
						if (Communicate.STATE == true) {
							if (Communicate.RESULT.get(0)
									.equals("GetAddressOK")) {
								Log.e("接受到获取地址的消息", "接受到获取地址的消息");
								Message msg = Message.obtain();
								msg.obj = Communicate.RESULT.get(1);

								String JWD = Communicate.RESULT.get(1);
								// 把经纬度的数值截取出来
								String longitude = JWD.substring(
										JWD.indexOf("longitude\":\"") + 13,
										JWD.indexOf("\",\"latitude"));
								String latitude = JWD.substring(
										JWD.indexOf("\"latitude\":") + 12,
										JWD.length() - 3);
								String[] arrStrings = new String[2];
								arrStrings[0] = longitude;
								arrStrings[1] = latitude;
								msg.what = DINGWEI;
								msg.obj = arrStrings;
								myhandler.sendMessage(msg);
								Communicate.STATE = false;
							}
						}
					}
				}
			}).start();

		}
		return rootview;
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics());
	}

	private void onLongDingWei(LatLng mcenpt) {
		choice = mcenpt;
		locationsource = false;
		// 反Geo搜索
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mcenpt));
	}

	private void dingwei() {
		locationsource = true;
		// 反Geo搜索
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(cenpt));
	}

	public void showPop(String s) {
		// 创建InfoWindow展示的view
		View popup = View.inflate(getActivity(), R.layout.window, null);
		// ImageView nowenclosure = (ImageView)
		// popup.findViewById(R.id.nowenclosure);
		// nowenclosure.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// final String[] s = {"周一","周二","周三","周四","周五","周六","周日"};
		// Intent intent = new Intent(getActivity(), PolygonMap.class);
		// ElectricFenceBean efb = new ElectricFenceBean();
		// efb.setOPERATIONFLAG(true);
		// List<String> list = new ArrayList<String>();
		// for(int i=0; i<7; i++){
		// list.add(s[i]);
		// }
		// efb.setDATE(list);
		// intent.putExtra("AddElectricFenceActivity", efb);
		// startActivity(intent);
		// }
		// });

		TextView name = (TextView) popup.findViewById(R.id.name_wind);
		name.setText(mname);
		TextView title = (TextView) popup.findViewById(R.id.textView_wind);
		title.setText(s);
		// 定义用于显示该InfoWindow的坐标点
		// 创建InfoWindow
		InfoWindow mInfoWindow = new InfoWindow(popup, cenpt, 0);
		// 显示InfoWindow
		mBaiduMap.showInfoWindow(mInfoWindow);
	}

	public void showPop2(String s) {
		// 创建InfoWindow展示的view
		View popup = View.inflate(getActivity(), R.layout.choiceaddr, null);
		// 安全路径
		ImageView safetypoint = (ImageView) popup
				.findViewById(R.id.safetypoint);
		safetypoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(getActivity()).setItems(
						new String[] { "驾车", "骑行", "公交", "步行" },
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									mBaiduMap.clear();
									// 定位
									dingwei();
									PlanNode stNode = PlanNode
											.withLocation(cenpt);
									PlanNode enNode = PlanNode
											.withLocation(choice);
									mRSearch.drivingSearch((new DrivingRoutePlanOption())
											.from(stNode).to(enNode));
								} else if (which == 1) {
									mBaiduMap.clear();
									// 定位
									dingwei();
									PlanNode stNode = PlanNode
											.withLocation(cenpt);
									PlanNode enNode = PlanNode
											.withLocation(choice);
									mRSearch.bikingSearch((new BikingRoutePlanOption())
											.from(stNode).to(enNode));
								} else if (which == 2) {
									mBaiduMap.clear();
									// 定位
									dingwei();
									PlanNode stNode = PlanNode
											.withLocation(cenpt);
									PlanNode enNode = PlanNode
											.withLocation(choice);
									mRSearch.transitSearch((new TransitRoutePlanOption())
											.from(stNode).city(nowcity)
											.to(enNode));
								} else if (which == 3) {
									mBaiduMap.clear();
									// 定位
									dingwei();
									PlanNode stNode = PlanNode
											.withLocation(cenpt);
									PlanNode enNode = PlanNode
											.withLocation(choice);
									mRSearch.walkingSearch((new WalkingRoutePlanOption())
											.from(stNode).to(enNode));
								}
								dialog.dismiss();
							}
						}).show();
			}
		});
		// 电子围栏
		ImageView choiceview = (ImageView) popup.findViewById(R.id.enclosure);
		choiceview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] s = { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
				Intent intent = new Intent(getActivity(), PolygonMap.class);
				ElectricFenceBean efb = new ElectricFenceBean();
				efb.setOPERATIONFLAG(true);
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < 7; i++) {
					list.add(s[i]);
				}
				efb.setDATE(list);
				intent.putExtra("AddElectricFenceActivity", efb);
				startActivity(intent);
			}
		});
		// 定义用于显示该InfoWindow的坐标点
		// 创建InfoWindow
		InfoWindow mInfoWindow = new InfoWindow(popup, choice, -85);
		// 显示InfoWindow
		mBaiduMap.showInfoWindow(mInfoWindow);
	}

	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		mMapView.onResume();
		super.onResume();
		if (getActivity().getIntent().getStringExtra("name") != null)
			mname = getActivity().getIntent().getStringExtra("name");
		// 第一次进入这个页面，下面的方法是不会执行的，因为ringName是0
		double latitude = getActivity().getIntent().getDoubleExtra("latitude",
				0);
		double longitude = getActivity().getIntent().getDoubleExtra(
				"longitude", 0);
		if (latitude != 0 || longitude != 0) {
			LatLng latlng = new LatLng(latitude, longitude);
			onLongDingWei(latlng);
		}
	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		mSearch.destroy();
		mRSearch.destroy();
		super.onDestroy();
	}

	private boolean compare(double d1, double d2) {
		if (new BigDecimal(d1).compareTo(new BigDecimal(d2)) == 0) {
			return true;
		} else {
			return false;
		}
	}

	private void addDeleteInfoWindow(LatLng ll) {
		// 创建InfoWindow展示的view
		View popup = View.inflate(getActivity(), R.layout.deleteinfowindow,
				null);
		RelativeLayout de_tv = (RelativeLayout) popup
				.findViewById(R.id.de_imageview);
		de_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 删除安全路径

			}
		});
		// 创建InfoWindow
		InfoWindow mInfoWindow = new InfoWindow(popup, ll, 0);
		// 显示InfoWindow
		mBaiduMap.showInfoWindow(mInfoWindow);
	}

	private static Bitmap small(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(0.5f, 0.5f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	// 驾车
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
			DrivingRouteLine mRouteLine = result.getRouteLines().get(0);
			overlay.setData(mRouteLine);
			overlay.addToMap();
			overlay.zoomToSpan();

			if (mRouteLine.getTerminal() != null) {
				carll = mRouteLine.getTerminal().getLocation();
				// 终点坐标
				OverlayOptions overlayOptions = new MarkerOptions().position(
						carll).icon(
						BitmapDescriptorFactory
								.fromResource(R.drawable.icon_marka));
				mBaiduMap.addOverlay(overlayOptions);
			}

			mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker marker) {
					if (compare(carll.latitude, marker.getPosition().latitude)
							&& compare(carll.longitude,
									marker.getPosition().longitude)) {
						addDeleteInfoWindow(carll);
					}
					return false;
				}
			});

			try {
				SetDrivingXml.setSetSecurePath(getActivity(), mRouteLine);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 开始发送
			Communicate cc = new Communicate();
			cc.start();
			Communicate.mark = true;

		}
	}

	// 骑行
	@Override
	public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
		if (bikingRouteResult == null
				|| bikingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (bikingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (bikingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
			BikingRouteOverlay overlay = new MyBikingRouteOverlay(mBaiduMap);
			BikingRouteLine mRouteLine = bikingRouteResult.getRouteLines().get(
					0);
			overlay.setData(mRouteLine);
			overlay.addToMap();
			overlay.zoomToSpan();

			if (mRouteLine.getTerminal() != null) {
				carll = mRouteLine.getTerminal().getLocation();
				// 终点坐标
				OverlayOptions overlayOptions = new MarkerOptions().position(
						carll).icon(
						BitmapDescriptorFactory
								.fromResource(R.drawable.icon_marka));
				mBaiduMap.addOverlay(overlayOptions);
			}

			mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker marker) {
					if (compare(carll.latitude, marker.getPosition().latitude)
							&& compare(carll.longitude,
									marker.getPosition().longitude)) {
						addDeleteInfoWindow(carll);
					}
					return false;
				}
			});

			try {
				SetBikingXml.setSetSecurePath(getActivity(), mRouteLine);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 开始发送
			Communicate cc = new Communicate();
			cc.start();
			Communicate.mark = true;

		}
	}

	// 公交
	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
			TransitRouteLine mRouteLine = result.getRouteLines().get(0);
			overlay.setData(mRouteLine);
			overlay.addToMap();
			overlay.zoomToSpan();

			if (mRouteLine.getTerminal() != null) {
				carll = mRouteLine.getTerminal().getLocation();
				// 终点坐标
				OverlayOptions overlayOptions = new MarkerOptions().position(
						carll).icon(
						BitmapDescriptorFactory
								.fromResource(R.drawable.icon_marka));
				mBaiduMap.addOverlay(overlayOptions);
			}

			mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker marker) {
					if (compare(carll.latitude, marker.getPosition().latitude)
							&& compare(carll.longitude,
									marker.getPosition().longitude)) {
						addDeleteInfoWindow(carll);
					}
					return false;
				}
			});

			try {
				SetTransitXml.setSetSecurePath(getActivity(), mRouteLine);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 开始发送
			// Communicate cc = new Communicate();
			// cc.start();
			// Communicate.mark = true;

		}
	}

	// 步行
	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
			WalkingRouteLine mRouteLine = result.getRouteLines().get(0);
			overlay.setData(mRouteLine);
			overlay.addToMap();
			overlay.zoomToSpan();

			if (mRouteLine.getTerminal() != null) {
				carll = mRouteLine.getTerminal().getLocation();
				// 终点坐标
				OverlayOptions overlayOptions = new MarkerOptions().position(
						carll).icon(
						BitmapDescriptorFactory
								.fromResource(R.drawable.icon_marka));
				mBaiduMap.addOverlay(overlayOptions);
			}

			mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker marker) {
					if (compare(carll.latitude, marker.getPosition().latitude)
							&& compare(carll.longitude,
									marker.getPosition().longitude)) {
						addDeleteInfoWindow(carll);
					}
					return false;
				}
			});

			try {
				SetWalkingXml.setSetSecurePath(getActivity(), mRouteLine);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 开始发送
			Communicate cc = new Communicate();
			cc.start();
			Communicate.mark = true;

		}
	}

	// 定制RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {
		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_now);
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
		}

	}

	private class MyBikingRouteOverlay extends BikingRouteOverlay {
		public MyBikingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_now);
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
		}

	}

	private class MyTransitRouteOverlay extends TransitRouteOverlay {
		public MyTransitRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_now);
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
		}

	}

	private class MyWalkingRouteOverlay extends WalkingRouteOverlay {
		public MyWalkingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_now);
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
		}

	}

	// 地理编码查询结果回调函数
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {

	}

	// 反地理编码查询结果回调函数
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (locationsource == true) {
			mBaiduMap.clear();
			mBaiduMap.addOverlay(new MarkerOptions().position(
					result.getLocation()).icon(
					BitmapDescriptorFactory.fromResource(R.drawable.icon_now)));

			// 获取路径描点的经纬度数组
			List<PoiInfo> l = result.getPoiList();
			List<LatLng> lll = new ArrayList<LatLng>();
			for (PoiInfo p : l) {
				LatLng ll = p.location;
				lll.add(ll);
			}

			// Toast.makeText(getActivity(), lll.toString(),
			// Toast.LENGTH_LONG).show();
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
					.getLocation()));
			// 添加弹出框覆盖物
			showPop(result.getAddress());
			// 获取当前城市
			nowcity = result.getAddressDetail().city;
		}
		if (locationsource == false) {
			mBaiduMap.clear();
			// mBaiduMap.addOverlay(new MarkerOptions().position(cenpt)
			// .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)));
			// mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(cenpt));
			mBaiduMap.addOverlay(new MarkerOptions().position(
					result.getLocation())
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.icon_marka)));
			// 添加弹出框覆盖物
			showPop2(result.getAddress());
		}

	}

}
