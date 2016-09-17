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

public class SportActivity extends Activity{

	private List<MessageItem> meslist = new ArrayList<SportActivity.MessageItem>();
	private ListView lv;
	private SportAdapter sa;
	private ImageView sportmore;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sportactivity);
		init();
	}

	private void init() {
		sportmore = (ImageView) findViewById(R.id.sportmore);
		sportmore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SportActivity.this,
						SportMoreActivity.class);
				startActivity(intent);
			}
		});
		
		lv = (ListView) findViewById(R.id.sportlistview);
    	for(int i=0; i<5; i++){
    		MessageItem item = new MessageItem();
    		item.sportname = "新希望国际";
    		item.sporttime = "01:04-02:02";
    		meslist.add(item);
    	}
    	sa = new SportAdapter(SportActivity.this);
    	lv.setAdapter(sa);
	}
	
	private class SportAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
        
        public SportAdapter(Context context){
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
        		convertView = mInflater.inflate(R.layout.sportitem, parent, false);
        		holder = new ViewHolder(convertView);
        		convertView.setTag(holder);
            //如果不为空则将之前回收的view重新利用
            } else {
              holder = (ViewHolder) convertView.getTag();
            }  
        	MessageItem item = meslist.get(position);
        	holder.tv1.setText(item.sportname);
        	holder.tv2.setText(item.sporttime);
			return convertView;
		}
		
	}
	
    public class MessageItem {
    	public String sportname;
    	public String sporttime;
    }
	
	private class ViewHolder{
		private TextView tv1;
		private TextView tv2;
		
		public ViewHolder(View view){
			tv1 = (TextView) view.findViewById(R.id.sportname);
			tv2 = (TextView) view.findViewById(R.id.sporttime);
		}
	}
	
	
}
