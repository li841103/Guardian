package com.szhd.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.szhd.bean.ClockBean;
import com.szhd.bean.ElectricFenceBean;
import com.szhd.guardian.MainActivity;
import com.szhd.guardian.R;
import com.szhd.map.PolygonMap;
import com.szhd.ui.SwitchView.OnStateChangedListener;

public class FunctionFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener{


	private View rootview;
	private List<MessageItem> meslist = new ArrayList<FunctionFragment.MessageItem>();
	private List<MessageItemele> meslistele = new ArrayList<FunctionFragment.MessageItemele>();
	private List<MessageItemclock> meslistclock = new ArrayList<FunctionFragment.MessageItemclock>();
	private FunctionAdapter fa;
	private ElectricFence ef;
	private ClockTime ct;
	private ListViewForScrollViewClock functionlistView;
	private ListViewForScrollView fencelistView;
	private ListViewForScrollViewSleep sleeplistview;

    //监控范围
    private LinearLayout close;
    private ImageView addclock;
    private ImageView addsleeptime;
    private SwitchView splitbutton2;
    private final String[] s = {"周一","周二","周三","周四","周五","周六","周日"}; 
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Handler mhandler;
    
    private Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			while(true){
				if(mBluetoothAdapter != null){
					if(!mBluetoothAdapter.isEnabled()){
						if(splitbutton2.getState() == 4){
							Message ms = new Message();
							ms.what = 9991;
							mhandler.sendMessage(ms);
						}
		            }else{
		            	if(splitbutton2.getState() == 1){
		            		Message ms = new Message();
		            		ms.what = 9992;
		            		mhandler.sendMessage(ms);
		            	}
		            }
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	});


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    if (null != rootview) {
            ViewGroup parent = (ViewGroup) rootview.getParent();
            if (null != parent) {
                parent.removeView(rootview);
            }
        } else {
        	rootview = inflater.inflate(R.layout.function,container,false); 
        	
        	close = (LinearLayout) rootview.findViewById(R.id.close);
        	addclock = (ImageView) rootview.findViewById(R.id.addclock);
        	addsleeptime = (ImageView) rootview.findViewById(R.id.addsleeptime);
        	splitbutton2 = (SwitchView) rootview.findViewById(R.id.splitbutton2);
        	// 设置初始状态。true为开;false为关[默认]。set up original status. true for open and false for close[default]
        	splitbutton2.setState(false);
        	splitbutton2.setOnStateChangedListener(new OnStateChangedListener() {
        	    @Override 
        	    public void toggleToOn() {
        	        // 原本为关闭的状态，被点击后 originally present close status after clicking
        	        // 执行一些耗时的业务逻辑操作 implement some time-consuming logic operation
        	    	//以动画效果切换到打开的状态 through changing animation effect to open status
        	        //如果为null,说明没有蓝牙设备
                    if (mBluetoothAdapter == null) {
                        Toast.makeText(getContext().getApplicationContext(), "没有蓝牙设备", Toast.LENGTH_LONG).show();
                        splitbutton2.toggleSwitch(false);
                    }else{
                    	if(!mBluetoothAdapter.isEnabled()){
                    		mBluetoothAdapter.enable();
                    		Toast.makeText(getContext().getApplicationContext(), "蓝牙已开启", Toast.LENGTH_SHORT).show();
                    		//开始搜索
                    		Message ms = new Message();
		            		ms.what = 9993;
		            		mhandler.sendMessage(ms);
                    	}
                    }
        	    }
        	    @Override 
        	    public void toggleToOff() {
        	    	if(mBluetoothAdapter.isEnabled()){
        	    		mBluetoothAdapter.disable();
        	    		splitbutton2.toggleSwitch(false);
        	    		Toast.makeText(getContext().getApplicationContext(), "蓝牙已关闭", Toast.LENGTH_SHORT).show();
        	    	}
        	    }
        	});

        	close.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle("提示").setMessage("是否进入休眠状态？").setNegativeButton("否", null);
					builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							
							
						}
					});
					builder.show();
				}
			});
        	
        	addclock.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), ClockActivity.class);
					ClockBean cb = new ClockBean();
					cb.setOPERATIONFLAG(true);
					List<String> list = new ArrayList<String>();
					for(int i=0; i<7; i++){
						list.add(s[i]);
					}
					cb.setDATE(list);
					intent.putExtra("AddClockBean", cb);
					startActivity(intent);
				}
			});
        	
        	addsleeptime.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View v) {
        			Intent intent = new Intent(getActivity(), SleepClockActivity.class);
        			ElectricFenceBean cb = new ElectricFenceBean();
        			cb.setOPERATIONFLAG(true);
        			List<String> list = new ArrayList<String>();
        			for(int i=0; i<7; i++){
        				list.add(s[i]);
        			}
        			cb.setDATE(list);
        			intent.putExtra("AddSleepClockBean", cb);
        			startActivity(intent);
        		}
        	});
        	
        	thread.start();
        	fencelistView = (ListViewForScrollView) rootview.findViewById(R.id.elelistView);
        	fencelistView.setOnItemClickListener(this);
        	fencelistView.setOnItemLongClickListener(this);
        	MessageItemele itemele = new MessageItemele();
        	itemele.rangeele = "1000";
        	for(int i=0; i<7; i++){
        		itemele.date.add(s[i]);
        	}
        	itemele.timesection= "00:00-00:00";
        	meslistele.add(itemele);
        	ef = new ElectricFence(getActivity());
        	fencelistView.setAdapter(ef);
        	
        	
        	sleeplistview = (ListViewForScrollViewSleep) rootview.findViewById(R.id.functionsleeptimelistView);
        	sleeplistview.setOnItemClickListener(this);
        	sleeplistview.setOnItemLongClickListener(this);
        	MessageItemclock itemclock = new MessageItemclock();
        	itemclock.rangeclock = "1000";
        	for(int i=0; i<7; i++){
        		itemclock.date.add(s[i]);
        	}
        	itemclock.timesection= "00:00-00:00";
        	meslistclock.add(itemclock);
        	ct = new ClockTime(getActivity());
        	sleeplistview.setAdapter(ct);
        	
        	
        	functionlistView = (ListViewForScrollViewClock) rootview.findViewById(R.id.functionlistView);
        	functionlistView.setOnItemClickListener(this);
        	functionlistView.setOnItemLongClickListener(this);
        	MessageItem item = new MessageItem();
        	item.clocktime = "00:00";
        	for(int i=0; i<7; i++){
        		item.date.add(s[i]);
        	}
        	meslist.add(item);
        	fa = new FunctionAdapter(getActivity());
        	functionlistView.setAdapter(fa);
        	
        	
        	mhandler = new Handler(){
        		@Override
        		public void handleMessage(Message msg) {
        			switch (msg.what) {
        			case 9991:
        				splitbutton2.toggleSwitch(false);
        				break;
        			case 9992:
        				splitbutton2.toggleSwitch(true);
        				break;
        			}
        		}
        	};
        }
		return rootview;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		ElectricFenceBean efb = (ElectricFenceBean) getActivity().getIntent().getSerializableExtra("BackElectricFenceActivity");
		if(efb != null){
			if(efb.isOPERATIONFLAG() == false){
				MessageItemele mi = new MessageItemele();
				mi.rangeele = efb.getRANGE();
				mi.timesection = efb.getTIMEPARAGRAPH();
				mi.date = efb.getDATE();
				meslistele.set(efb.getPOSITION(), mi);
				ef.notifyDataSetChanged();
			}else{
				MessageItemele mi = new MessageItemele();
				mi.rangeele = efb.getRANGE();
				mi.timesection = efb.getTIMEPARAGRAPH();
				mi.date = efb.getDATE();
				meslistele.add(mi);
				ef.notifyDataSetChanged();
			}
		}
		
		
		ElectricFenceBean scb = (ElectricFenceBean) getActivity().getIntent().getSerializableExtra("BackSleepClockActivity");
		if(scb != null){
			if(scb.isOPERATIONFLAG() == false){
				MessageItemclock mi = new MessageItemclock();
				mi.rangeclock = scb.getRANGE();
				mi.timesection = scb.getTIMEPARAGRAPH();
				mi.date = scb.getDATE();
				meslistclock.set(scb.getPOSITION(), mi);
				ct.notifyDataSetChanged();
			}else{
				MessageItemclock mi = new MessageItemclock();
				mi.rangeclock = scb.getRANGE();
				mi.timesection = scb.getTIMEPARAGRAPH();
				mi.date = scb.getDATE();
				meslistclock.add(mi);
				ct.notifyDataSetChanged();
			}
		}
		
		
		
		
		
		ClockBean cb =  (ClockBean) getActivity().getIntent().getSerializableExtra("BackClockActivity");
		if(cb != null){
			if(cb.isOPERATIONFLAG() == false){
				MessageItem mi = new MessageItem();
				mi.clocktime = cb.getTIME();
				mi.date = cb.getDATE();
				meslist.set(cb.getPOSITION(), mi);
				fa.notifyDataSetChanged();
			}else{
				MessageItem mi = new MessageItem();
				mi.clocktime = cb.getTIME();
				mi.date = cb.getDATE();
				meslist.add(mi);
				fa.notifyDataSetChanged();
			}
		}
		//intent接收后会一直存在，切换其它Fragment后会重复加载，所以在这用过后移除
		getActivity().getIntent().removeExtra("BackClockActivity");
		getActivity().getIntent().removeExtra("BackElectricFenceActivity");
		getActivity().getIntent().removeExtra("BackSleepClockActivity");
		
	}
	
	
	
	
	
    public class MessageItem {
    	public String clocktime;
    	public List<String> date = new ArrayList<String>();
    }
    
    
    public class MessageItemele {
    	public String rangeele;
    	public List<String> date = new ArrayList<String>();
    	public String timesection;
    }
    
    public class MessageItemclock {
    	public String rangeclock;
    	public List<String> date = new ArrayList<String>();
    	public String timesection;
    }
	
	
	private class ViewHolder{
		private TextView tv1;
		private TextView tv2;
		private SwitchView sb;
		
		public ViewHolder(View view){
			tv1 = (TextView) view.findViewById(R.id.clocktime);
			tv2 = (TextView) view.findViewById(R.id.date);
			sb = (SwitchView) view.findViewById(R.id.functionclockbtn);
	       	sb.setOnStateChangedListener(new OnStateChangedListener() {
				@Override
				public void toggleToOn() {
					sb.toggleSwitch(true);
				}
				@Override
				public void toggleToOff() {
					sb.toggleSwitch(false);
				}
			});
		}
	}
	
	private class ViewHolderele{
		private TextView tv2;
		private TextView timesection;
		private SwitchView sb;
		
		public ViewHolderele(View view){
			tv2 = (TextView) view.findViewById(R.id.dateele);
			timesection = (TextView) view.findViewById(R.id.timesection);
			sb = (SwitchView) view.findViewById(R.id.functionclockbtnele);
			sb.setOnStateChangedListener(new OnStateChangedListener() {
				@Override
				public void toggleToOn() {
					sb.toggleSwitch(true);
				}
				@Override
				public void toggleToOff() {
					sb.toggleSwitch(false);
				}
			});
		}
	}
	
	private class ViewHolderclock{
		private TextView tv2;
		private TextView timesection;
		private SwitchView sb;
		
		public ViewHolderclock(View view){
			tv2 = (TextView) view.findViewById(R.id.datesleep);
			timesection = (TextView) view.findViewById(R.id.sleeptimesection);
			sb = (SwitchView) view.findViewById(R.id.functionsleepbtnele);
			sb.setOnStateChangedListener(new OnStateChangedListener() {
				@Override
				public void toggleToOn() {
					sb.toggleSwitch(true);
				}
				@Override
				public void toggleToOff() {
					sb.toggleSwitch(false);
				}
			});
		}
	}
	
	
	
	private class FunctionAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		private ListView lv;
        
        public FunctionAdapter(Context context){
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
        		convertView = mInflater.inflate(R.layout.functionitem, parent, false);
        		holder = new ViewHolder(convertView);
        		convertView.setTag(holder);
            //如果不为空则将之前回收的view重新利用
            } else {
              holder = (ViewHolder) convertView.getTag();
            }  
        	MessageItem item = meslist.get(position);
        	holder.tv1.setText(item.clocktime);
        	String mdate = "";
        	if(item.date.size()==7){
        		holder.tv2.setText("每天");
        	}else if(item.date.size()==0){
        		holder.tv2.setText("次日");
        	}else{
	        	for(int i=0; i<item.date.size(); i++){
	        		mdate += item.date.get(i)+",";
	        	}
	        	holder.tv2.setText(mdate.substring(0, mdate.length()-1));
        	}
			return convertView;
		}
	}
	
	
	private void delateclock(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("提示").setMessage("是否删除闹钟？").setNegativeButton("否", null);
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				meslist.remove(functionlistView.getPosition());
				fa.notifyDataSetChanged();
			}
		});
		builder.show();
	}
	
	
	private class ElectricFence extends BaseAdapter {

		
		private LayoutInflater mInflater;
		private ListView lv;
        
        public ElectricFence(Context context){
        	this.mInflater = LayoutInflater.from(context);
        }
		
		
		@Override
		public int getCount() {
			return meslistele.size();
		}

		@Override
		public Object getItem(int position) {
			return meslistele.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			 ViewHolderele holderele = null;
		     //如果为空则创建新的item
            if (convertView == null) {
        		convertView = mInflater.inflate(R.layout.electricfenceitem, parent, false);
        		holderele = new ViewHolderele(convertView);
        		convertView.setTag(holderele);
            //如果不为空则将之前回收的view重新利用
            } else {
                holderele = (ViewHolderele) convertView.getTag();
            }
            //设置资源
        	MessageItemele item = meslistele.get(position);
        	String mdate = "";
        	if(item.date.size()==7){
        		holderele.tv2.setText("每天");
        	}else if(item.date.size()==0){
        		holderele.tv2.setText("次日");
        	}else{
	        	for(int i=0; i<item.date.size(); i++){
	        		mdate += item.date.get(i)+",";
	        	}
	        	holderele.tv2.setText(mdate.substring(0, mdate.length()-1));
        	}
        	holderele.timesection.setText(item.timesection);
			return convertView;
		}
		

	}


	private void delate(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("提示").setMessage("是否删除电子围栏？").setNegativeButton("否", null);
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				meslistele.remove(fencelistView.getPosition());
				ef.notifyDataSetChanged();
			}
		});
		builder.show();
	}


	
private class ClockTime extends BaseAdapter {

		
		private LayoutInflater mInflater;
		private ListView lv;
        
        public ClockTime(Context context){
        	this.mInflater = LayoutInflater.from(context);
        }
		
		
		@Override
		public int getCount() {
			return meslistclock.size();
		}

		@Override
		public Object getItem(int position) {
			return meslistclock.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			 ViewHolderclock holderele = null;
		     //如果为空则创建新的item
            if (convertView == null) {
        		convertView = mInflater.inflate(R.layout.funsleeptimeitem, parent, false);
        		holderele = new ViewHolderclock(convertView);
        		convertView.setTag(holderele);
            //如果不为空则将之前回收的view重新利用
            } else {
                holderele = (ViewHolderclock) convertView.getTag();
            }
            //设置资源
        	MessageItemclock item = meslistclock.get(position);
        	String mdate = "";
        	if(item.date.size()==7){
        		holderele.tv2.setText("每天");
        	}else if(item.date.size()==0){
        		holderele.tv2.setText("次日");
        	}else{
	        	for(int i=0; i<item.date.size(); i++){
	        		mdate += item.date.get(i)+",";
	        	}
	        	holderele.tv2.setText(mdate.substring(0, mdate.length()-1));
        	}
        	holderele.timesection.setText(item.timesection);
			return convertView;
		}
		

	}


	private void sleepdelate(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("提示").setMessage("是否删除安睡时间？").setNegativeButton("否", null);
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				meslistclock.remove(sleeplistview.getPosition());
				ct.notifyDataSetChanged();
			}
		});
		builder.show();
	}

	

	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(parent instanceof ListViewForScrollViewClock){
			Intent intent = new Intent(getActivity(), ClockActivity.class);
			ClockBean cb = new ClockBean();
			cb.setPOSITION(position);
			cb.setTIME(meslist.get(position).clocktime);
			List<String> list = new ArrayList<String>();
			for(int i=0; i<meslist.get(position).date.size(); i++){
				list.add(meslist.get(position).date.get(i));
			}
			cb.setDATE(list);
			intent.putExtra("JumpClockBean", cb);
			startActivity(intent);
		}else if(parent instanceof ListViewForScrollViewSleep){
			Intent intent = new Intent(getActivity(), SleepClockActivity.class);
			ElectricFenceBean efb = new ElectricFenceBean();
			efb.setPOSITION(position);
			efb.setRANGE(meslistclock.get(position).rangeclock);
			efb.setTIMEPARAGRAPH(meslistclock.get(position).timesection);
			List<String> list = new ArrayList<String>();
			for(int i=0; i<meslistclock.get(position).date.size(); i++){
				list.add(meslistclock.get(position).date.get(i));
			}
			efb.setDATE(list);
			intent.putExtra("JumpSleepClockActivity", efb);
			startActivity(intent);
		}else{
			Intent intent = new Intent(getActivity(), PolygonMap.class);
			ElectricFenceBean efb = new ElectricFenceBean();
			efb.setPOSITION(position);
			efb.setRANGE(meslistele.get(position).rangeele);
			efb.setTIMEPARAGRAPH(meslistele.get(position).timesection);
			List<String> list = new ArrayList<String>();
			for(int i=0; i<meslistele.get(position).date.size(); i++){
				list.add(meslistele.get(position).date.get(i));
			}
			efb.setDATE(list);
			intent.putExtra("JumpElectricFenceActivity", efb);
			startActivity(intent);
		}
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if(parent instanceof ListViewForScrollViewClock){
			delateclock();
		}else if(parent instanceof ListViewForScrollViewSleep){
			sleepdelate();
		}else{
			delate();
		}
		return true;
	}
	
	
}
