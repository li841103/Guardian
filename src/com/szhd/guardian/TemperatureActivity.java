package com.szhd.guardian;

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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TemperatureActivity extends Activity{

	private List<MessageItem> meslist = new ArrayList<TemperatureActivity.MessageItem>();
	private ListView lv;
	private TemperatureAdapter sa;
	private ImageView temperaturemore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temperature);
		init();
	}

	private void init() {
		temperaturemore = (ImageView) findViewById(R.id.temperaturemore);
		temperaturemore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TemperatureActivity.this,
						TemperatureMoreActivity.class);
    			startActivity(intent);
			}
		});
		lv = (ListView) findViewById(R.id.temperaturelistview);
    	for(int i=0; i<5; i++){
    		MessageItem item = new MessageItem();
    		item.temperaturename = "体温升高";
    		item.temperaturetime = "02:02";
    		meslist.add(item);
    	}
    	sa = new TemperatureAdapter(TemperatureActivity.this);
    	lv.setAdapter(sa);
	}
	
	private class TemperatureAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
        
        public TemperatureAdapter(Context context){
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
        		convertView = mInflater.inflate(R.layout.temperatureitem, parent, false);
        		holder = new ViewHolder(convertView);
        		convertView.setTag(holder);
            //如果不为空则将之前回收的view重新利用
            } else {
              holder = (ViewHolder) convertView.getTag();
            }  
        	MessageItem item = meslist.get(position);
        	holder.tv1.setText(item.temperaturename);
        	holder.tv2.setText(item.temperaturetime);
			return convertView;
		}
		
	}
	
    public class MessageItem {
    	public String temperaturename;
    	public String temperaturetime;
    }
	
	private class ViewHolder{
		private TextView tv1;
		private TextView tv2;
		
		public ViewHolder(View view){
			tv1 = (TextView) view.findViewById(R.id.temperaturename);
			tv2 = (TextView) view.findViewById(R.id.temperaturetime);
		}
	}
	
}
