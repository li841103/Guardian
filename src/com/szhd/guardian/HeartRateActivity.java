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

public class HeartRateActivity extends Activity{

	private List<MessageItem> meslist = new ArrayList<HeartRateActivity.MessageItem>();
	private ListView lv;
	private HeartAdapter sa;
	private ImageView heartmorerate;
	private ImageView heartmoreratenow;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heartrate);
		init();
	}

	private void init() {
		heartmoreratenow = (ImageView) findViewById(R.id.heartmoreratenow);
		heartmoreratenow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HeartRateActivity.this,
						CountDown.class);
				startActivity(intent);
			}
		});
		
		heartmorerate = (ImageView) findViewById(R.id.heartmorerate);
		heartmorerate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HeartRateActivity.this,
						HeartMoreRateActivity.class);
    			startActivity(intent);
			}
		});
		lv = (ListView) findViewById(R.id.heartlistview);
    	for(int i=0; i<5; i++){
    		MessageItem item = new MessageItem();
    		item.heartname = "心率升高";
    		item.hearttime = "02:02";
    		meslist.add(item);
    	}
    	sa = new HeartAdapter(HeartRateActivity.this);
    	lv.setAdapter(sa);
	}
	
	
	
	private class HeartAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
        
        public HeartAdapter(Context context){
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
        		convertView = mInflater.inflate(R.layout.heartitem, parent, false);
        		holder = new ViewHolder(convertView);
        		convertView.setTag(holder);
            //如果不为空则将之前回收的view重新利用
            } else {
              holder = (ViewHolder) convertView.getTag();
            }  
        	MessageItem item = meslist.get(position);
        	holder.tv1.setText(item.heartname);
        	holder.tv2.setText(item.hearttime);
			return convertView;
		}
		
	}
	
    public class MessageItem {
    	public String heartname;
    	public String hearttime;
    }
	
	private class ViewHolder{
		private TextView tv1;
		private TextView tv2;
		
		public ViewHolder(View view){
			tv1 = (TextView) view.findViewById(R.id.heartname);
			tv2 = (TextView) view.findViewById(R.id.hearttime);
		}
	}
	
	
	
}
