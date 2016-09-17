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

public class SportMoreActivity extends Activity{

	private List<MessageItem> meslist = new ArrayList<SportMoreActivity.MessageItem>();
	private ListView lv;
	private SportMoreAdapter sa;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sportmore);
		init();
	}

	private void init() {
		lv = (ListView) findViewById(R.id.sportmorelistView);
    	for(int i=0; i<15; i++){
    		MessageItem item = new MessageItem();
    		item.sportmoretime = "18:00";
    		item.sportmoretimeaddr = "天府三街新希望国际";
    		item.sportmorenum = "11241步";
    		item.sportmoreper = "10%";
    		meslist.add(item);
    	}
    	sa = new SportMoreAdapter(SportMoreActivity.this);
    	lv.setAdapter(sa);
	}
	
	
	private class SportMoreAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
        
        public SportMoreAdapter(Context context){
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
        		convertView = mInflater.inflate(R.layout.sportmoreitem, parent, false);
        		holder = new ViewHolder(convertView);
        		convertView.setTag(holder);
            //如果不为空则将之前回收的view重新利用
            } else {
              holder = (ViewHolder) convertView.getTag();
            }  
        	MessageItem item = meslist.get(position);
        	holder.tv1.setText(item.sportmoretime);
        	holder.tv2.setText(item.sportmoretimeaddr);
        	holder.tv3.setText(item.sportmorenum);
        	holder.tv4.setText(item.sportmoreper);
			return convertView;
		}
		
	}
	
    public class MessageItem {
    	public String sportmoretime;
    	public String sportmoretimeaddr;
    	public String sportmorenum;
    	public String sportmoreper;
    }
	
	private class ViewHolder{
		private TextView tv1;
		private TextView tv2;
		private TextView tv3;
		private TextView tv4;
		
		public ViewHolder(View view){
			tv1 = (TextView) view.findViewById(R.id.sportmoretime);
			tv2 = (TextView) view.findViewById(R.id.sportmoretimeaddr);
			tv3 = (TextView) view.findViewById(R.id.sportmorenum);
			tv4 = (TextView) view.findViewById(R.id.sportmoreper);
		}
	}
	
	
}
