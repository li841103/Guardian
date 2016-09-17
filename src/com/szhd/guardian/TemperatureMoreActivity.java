package com.szhd.guardian;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TemperatureMoreActivity extends Activity{

	
	private List<MessageItem> meslist = new ArrayList<TemperatureMoreActivity.MessageItem>();
	private ListView lv;
	private TemperatureMoreAdapter sa;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temperaturemore);
		init();
	}

	private void init() {
		lv = (ListView) findViewById(R.id.temperaturemorelistview);
    	for(int i=0; i<15; i++){
    		MessageItem item = new MessageItem();
    		item.temperaturemorename = "02:02";
    		item.temperaturemoretime = "36.5℃";
    		meslist.add(item);
    	}
    	sa = new TemperatureMoreAdapter(TemperatureMoreActivity.this);
    	lv.setAdapter(sa);
	}
	
	private class TemperatureMoreAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
        
        public TemperatureMoreAdapter(Context context){
        	this.mInflater = LayoutInflater.from(context);
        }
		
		@Override
		public int getCount() {
			return meslist.size();
		}

		@Override
		public Object getItem(int position) {
			return meslist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
	        //如果为空则创建新的item
            if (convertView == null) {
        		convertView = mInflater.inflate(R.layout.temperaturemoreitem, parent, false);
        		holder = new ViewHolder(convertView);
        		convertView.setTag(holder);
            //如果不为空则将之前回收的view重新利用
            } else {
              holder = (ViewHolder) convertView.getTag();
            }  
        	MessageItem item = meslist.get(position);
        	holder.tv1.setText(item.temperaturemorename);
        	holder.tv2.setText(item.temperaturemoretime);
			return convertView;
		}
		
	}
	
    public class MessageItem {
    	public String temperaturemorename;
    	public String temperaturemoretime;
    }
	
	private class ViewHolder{
		private TextView tv1;
		private TextView tv2;
		
		public ViewHolder(View view){
			tv1 = (TextView) view.findViewById(R.id.temperaturemorename);
			tv2 = (TextView) view.findViewById(R.id.temperaturemoretime);
		}
	}
	
	
}
