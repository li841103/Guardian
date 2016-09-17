package com.szhd.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.szhd.guardian.R;
import com.szhd.util.TemperatureInfo;

public class HeartDialogActivity extends Activity{

	private ListView lv;
	private HeartData hd;
	private List<MessageItem> milist = new ArrayList<HeartDialogActivity.MessageItem>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heartdialogactivity);  
		initView();
		initData();
	}

	
	private void initData() {
		Intent intent = getIntent(); //用于激活它的意图对象
		List<TemperatureInfo> list = (List<TemperatureInfo>)intent.getSerializableExtra("list");  
	    for(int i=0;i<list.size();i++){  
	    	TemperatureInfo t = list.get(i);  
	    	MessageItem item = new MessageItem();
	    	item.time = t.getViatlTimeForHm();
	    	item.temperature = t.getTemperatureStr();
	    	milist.add(item);
	    }  
	}


	private void initView() {
		lv = (ListView) findViewById(R.id.heartlistdata);
		hd = new HeartData(this);
		lv.setAdapter(hd);
	}
	
	

	private class MessageItem{
		public String time;
		public String temperature;
	}
	
	
	
	private class ViewHolder{
		public TextView time;
		public TextView temperature;
		public ViewHolder(View view) {
			time = (TextView) view.findViewById(R.id.time);
			temperature = (TextView) view.findViewById(R.id.temperature);
		}
	}
	
	

	private class HeartData extends BaseAdapter{

		private LayoutInflater mInflater;
        
        public HeartData(Context context){
        	this.mInflater = LayoutInflater.from(context);
        }
		
		@Override
		public int getCount() {
			//数据总条数
			return milist.size();
		}

		@Override
		public Object getItem(int position) {
			return milist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.heartdata, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			MessageItem item = milist.get(position);
			holder.time.setText(item.time);
			holder.temperature.setText(item.temperature);
			return convertView;
		}
		
	}
	
	
	
	
}
