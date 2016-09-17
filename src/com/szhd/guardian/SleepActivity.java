package com.szhd.guardian;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SleepActivity extends Activity{

	private List<MessageItem> meslist = new ArrayList<SleepActivity.MessageItem>();
	private ListView lv;
	private SleepAdapter sa;
	private TextView textView18;
	private ImageView hpback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sleepui);
		init();
	}

	private void init() {
		hpback = (ImageView) findViewById(R.id.hpback);
		hpback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		textView18 = (TextView) findViewById(R.id.textView18);
		textView18.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		
		lv = (ListView) findViewById(R.id.sleeplistview);
    	for(int i=0; i<5; i++){
    		MessageItem item = new MessageItem();
    		item.sleepname = "翻身";
    		item.sleeptime = "01:04";
    		meslist.add(item);
    	}
    	sa = new SleepAdapter(SleepActivity.this);
    	lv.setAdapter(sa);
	}
	
	private class SleepAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
        
        public SleepAdapter(Context context){
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
        		convertView = mInflater.inflate(R.layout.sleepitem, parent, false);
        		holder = new ViewHolder(convertView);
        		convertView.setTag(holder);
            //如果不为空则将之前回收的view重新利用
            } else {
              holder = (ViewHolder) convertView.getTag();
            }  
        	MessageItem item = meslist.get(position);
        	holder.tv1.setText(item.sleepname);
        	holder.tv2.setText(item.sleeptime);
			return convertView;
		}
		
	}
	
    public class MessageItem {
    	public String sleepname;
    	public String sleeptime;
    }
	
	private class ViewHolder{
		private TextView tv1;
		private TextView tv2;
		
		public ViewHolder(View view){
			tv1 = (TextView) view.findViewById(R.id.sleepname);
			tv2 = (TextView) view.findViewById(R.id.sleeptime);
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	
}
