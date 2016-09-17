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

public class HeartMoreRateActivity extends Activity{
	
	private List<MessageItem> meslist = new ArrayList<HeartMoreRateActivity.MessageItem>();
	private ListView lv;
	private HeartMoreRateAdapter sa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heartmorerate);
		init();
	}

	private void init() {
		lv = (ListView) findViewById(R.id.heartmoreratelistView);
    	for(int i=0; i<15; i++){
    		MessageItem item = new MessageItem();
    		item.heartmoreratetime = "18:00";
    		item.heartmoreratenum = "112次/分";
    		meslist.add(item);
    	}
    	sa = new HeartMoreRateAdapter(HeartMoreRateActivity.this);
    	lv.setAdapter(sa);
	}
	
	private class HeartMoreRateAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
        
        public HeartMoreRateAdapter(Context context){
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
        		convertView = mInflater.inflate(R.layout.heartmorerateitem, parent, false);
        		holder = new ViewHolder(convertView);
        		convertView.setTag(holder);
            //如果不为空则将之前回收的view重新利用
            } else {
              holder = (ViewHolder) convertView.getTag();
            }  
        	MessageItem item = meslist.get(position);
        	holder.tv1.setText(item.heartmoreratetime);
        	holder.tv2.setText(item.heartmoreratenum);
			return convertView;
		}
		
	}
	
    public class MessageItem {
    	public String heartmoreratetime;
    	public String heartmoreratenum;
    }
	
	private class ViewHolder{
		private TextView tv1;
		private TextView tv2;
		
		public ViewHolder(View view){
			tv1 = (TextView) view.findViewById(R.id.hmoreratetime);
			tv2 = (TextView) view.findViewById(R.id.hmoreratenum);
		}
	}
	
	
	
}
