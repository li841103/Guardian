package com.szhd.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szhd.guardian.R;
import com.szhd.guardian.WarningListView;
import com.szhd.guardian.WarningSlideView;
import com.szhd.impl.OnSlideListener;

public class WarningFragment extends Fragment implements OnSlideListener, OnClickListener{

	
	private View rootview;
	private WarningListView wlv;
	private List<MessageItem> mMessageItems = new ArrayList<WarningFragment.MessageItem>();
	private WarningAdapter wa;
	private WarningSlideView mLastSlideViewWithStatusOn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    if (null != rootview) {
            ViewGroup parent = (ViewGroup) rootview.getParent();
            if (null != parent) {
                parent.removeView(rootview);
            }
        } else {
        	rootview = inflater.inflate(R.layout.warning,container,false);  
        	wlv = (WarningListView) rootview.findViewById(R.id.warninglist);
            for (int i = 0; i < 10; i++) {
                MessageItem item = new MessageItem();
                item.warningtimev = "12:00:00";
                item.warningtypev = "电子围栏报警";
                mMessageItems.add(item);
            }
        	wa = new WarningAdapter(getActivity());
        	wlv.setAdapter(wa);
        }
		return rootview;
	}
	
	
	public class MessageItem{
		public WarningSlideView wsv;
		public LinearLayout warningitemid;
		public TextView warningtime;
		public TextView warningtype;
		public TextView warning_bbb;
		public ViewGroup warning_left_holder;
		public String warningtimev;
		public String warningtypev;
	}
	
	
	
	private class ViewHolder{
		public TextView warningtime;
		public TextView warningtype;
		public LinearLayout warningitemid;
		public TextView warning_bbb;
		public ViewGroup warning_left_holder;
		public ViewHolder(View view) {
			warningtime = (TextView) view.findViewById(R.id.warningtime);
			warningtype = (TextView) view.findViewById(R.id.warningtype);
			warning_bbb = (TextView) view.findViewById(R.id.warning_bbb);
			warningitemid = (LinearLayout) view.findViewById(R.id.warningitemid);
			warning_left_holder = (ViewGroup)view.findViewById(R.id.warning_left_holder);
		}
	}
	
	
	private class WarningAdapter extends BaseAdapter{
		
		
		private LayoutInflater mInflater;
        
        public WarningAdapter(Context context){
        	this.mInflater = LayoutInflater.from(context);
        }
		

		@Override
		public int getCount() {
			return mMessageItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mMessageItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			WarningSlideView wsv = (WarningSlideView) convertView;
			if(wsv == null){
				//获得界面充气机
				View itemView = mInflater.inflate(R.layout.warningitem, parent, false);
				wsv = new WarningSlideView(getActivity());
				wsv.setContentView(itemView);
				wsv.setOnSlideListener(WarningFragment.this);
				holder = new ViewHolder(wsv);
				wsv.setTag(holder);
			}else{
				holder = (ViewHolder) wsv.getTag();
			}
			MessageItem item = mMessageItems.get(position);
			item.wsv = wsv;
			item.warningtime = holder.warningtime;
			item.warningtype = holder.warningtype;
			item.warning_bbb = holder.warning_bbb;
			item.warningitemid = holder.warningitemid;
			item.warning_left_holder = holder.warning_left_holder;
			item.wsv.reset();
			holder.warningtime.setText(item.warningtimev);
			holder.warningtype.setText(item.warningtypev);
			holder.warning_left_holder.setOnClickListener(WarningFragment.this);
			return wsv;
		}
		
		
	}


	@Override
	public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }
        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (WarningSlideView) view;
        }
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.warning_left_holder) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("提示").setMessage("标记为已读？").setNegativeButton("否", null);
			builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), "点击了" + wlv.getPosition(), Toast.LENGTH_SHORT ).show();
					mMessageItems.get(wlv.getPosition()).warningitemid.setBackgroundColor(Color.WHITE);
					mMessageItems.get(wlv.getPosition()).warningtime.setTextColor(Color.BLACK);
					mMessageItems.get(wlv.getPosition()).warningtype.setTextColor(Color.BLACK);
					mMessageItems.get(wlv.getPosition()).warning_bbb.setText("已读");
				}
			});
			builder.show();
        }
	}


	
	
	
	
}
