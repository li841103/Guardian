package com.szhd.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.szhd.guardian.R;
import com.szhd.ui.AutoListView.OnLoadListener;

public class FindAddrActivity extends Activity implements OnItemClickListener, OnGetPoiSearchResultListener, OnLoadListener {

	private ImageView queryback;
	private EditText queryaddr;
	private Button querydown;
	private String city;
	
	private PoiSearch mPoiSearch = null;
	private int load_Index = 0;
	
	private List<MessageItem> list = new ArrayList<FindAddrActivity.MessageItem>();
	private QueryAddrAdapter qa;
	private AutoListView addrrequest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findaddr);  
		init();
	}

	
	private void init() {
		queryback = (ImageView) findViewById(R.id.queryback);
		//取消键
		queryback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		queryaddr = (EditText) findViewById(R.id.queryaddr);
		
		querydown = (Button) findViewById(R.id.querydown);
		querydown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//开始搜索
				queryAddr();
			}
		});
		
		Intent intent = getIntent(); //用于激活它的意图对象
		if(intent.getStringExtra("city") != null && intent.getStringExtra("city").length() != 0)
			city = intent.getStringExtra("city");
		
		
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		
		addrrequest = (AutoListView) findViewById(R.id.addrrequest);
		addrrequest.setOnItemClickListener(this);
		addrrequest.setOnLoadListener(this);
		qa = new QueryAddrAdapter(this);
		list.clear();
		addrrequest.setResultSize(list.size());
		addrrequest.setAdapter(qa);
	}
	
	
	public class MessageItem {
    	public String addritem;
    	public LatLng location;
    }
	
	
	private class ViewHolder{
		private TextView addritem;
		public ViewHolder(View view){
			addritem = (TextView) view.findViewById(R.id.addritem);
		}
	}
	
	
	
	private class QueryAddrAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		public QueryAddrAdapter(Context context){
        	this.mInflater = LayoutInflater.from(context);
        }
		
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				//获得界面充气机
				convertView = mInflater.inflate(R.layout.addrrequest, parent, false);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			MessageItem item = list.get(position);
			holder.addritem.setText(item.addritem);
			return convertView;
		}
		
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(position < list.size()){
			Intent intent = new Intent();
			intent.setClass(FindAddrActivity.this, FunctionActivity.class);
			intent.putExtra("latitude", list.get(position).location.latitude);  
			intent.putExtra("longitude", list.get(position).location.longitude);  
			startActivity(intent);
			finish();
		}
	}
	
	
	//搜索
	protected void queryAddr() {
		load_Index = 0;
		mPoiSearch.searchInCity((new PoiCitySearchOption())
				.city(city)
				.keyword(queryaddr.getText().toString())
				.pageNum(load_Index));
	}
	
	
	@Override
	public void onLoad() {
		load_Index++;
		mPoiSearch.searchInCity((new PoiCitySearchOption())
				.city(city)
				.keyword(queryaddr.getText().toString())
				.pageNum(load_Index));
	}
	
	
	@Override
	protected void onDestroy() {
		mPoiSearch.destroy();
		super.onDestroy();
	}
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}


	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(FindAddrActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
				.show();
		} else {
			Toast.makeText(FindAddrActivity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
				.show();
		}
	}


	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(FindAddrActivity.this, "未找到结果", Toast.LENGTH_LONG)
				.show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			if(load_Index == 0){
				list.clear();
			}
			addrrequest.onLoadComplete();
			for (PoiInfo poiInfo : result.getAllPoi()) {
				MessageItem item = new MessageItem();
				item.addritem = poiInfo.address;
				item.location = poiInfo.location;
				list.add(item);
			}
			addrrequest.setResultSize(list.size());
			qa.notifyDataSetChanged();
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			String strInfo = "在";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "找到结果";
			Toast.makeText(FindAddrActivity.this, strInfo, Toast.LENGTH_LONG)
				.show();
		}
		
	}
	
}
