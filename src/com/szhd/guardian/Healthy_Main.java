package com.szhd.guardian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.szhd.ui.AnnularUI;
import com.szhd.ui.SmallSingleAnnularUI;

public class Healthy_Main extends Fragment{
	
	private AnnularUI annularui;
	private View rootview;
	private RelativeLayout relativeLayout2;
	private RelativeLayout relativeLayout3;
	private RelativeLayout relativeLayout4;
	private RelativeLayout relativeLayout5;
	private SmallSingleAnnularUI smallsingleannularui1;
	private SmallSingleAnnularUI smallsingleannularui2;
	private SmallSingleAnnularUI smallsingleannularui3;
	private SmallSingleAnnularUI smallsingleannularui4;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != rootview) {
            ViewGroup parent = (ViewGroup) rootview.getParent();
            if (null != parent) {
                parent.removeView(rootview);
            }
        } else {
        	rootview = inflater.inflate(R.layout.healthy_main,container,false); 
        	annularui = (AnnularUI) rootview.findViewById(R.id.annularui);
        	annularui.setSleepscore(90);
        	annularui.setSportscore(80);
        	annularui.setHeartratescore(60);
        	annularui.setTemperaturescore(50);
		
        	relativeLayout2 = (RelativeLayout) rootview.findViewById(R.id.relativeLayout2);
        	relativeLayout2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getContext(),
							SleepActivity.class);
					startActivity(intent);
				}
			});
        	smallsingleannularui1 = (SmallSingleAnnularUI) rootview.findViewById(R.id.smallsingleannularui1);
        	smallsingleannularui1.setScore(50f);
        	
        	
        	
        	relativeLayout3 = (RelativeLayout) rootview.findViewById(R.id.relativeLayout3);
        	relativeLayout3.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View v) {
        			Intent intent = new Intent(getContext(),
        					SportActivity.class);
        			startActivity(intent);
        		}
        	});
        	smallsingleannularui2 = (SmallSingleAnnularUI) rootview.findViewById(R.id.smallsingleannularui2);
        	smallsingleannularui2.setScore(80f);
        	
        	
        	
        	relativeLayout4 = (RelativeLayout) rootview.findViewById(R.id.relativeLayout4);
        	relativeLayout4.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View v) {
        			Intent intent = new Intent(getContext(),
        					HeartRateActivity.class);
        			startActivity(intent);
        		}
        	});
        	smallsingleannularui3 = (SmallSingleAnnularUI) rootview.findViewById(R.id.smallsingleannularui3);
        	smallsingleannularui3.setScore(70f);
        	
        	
        	
        	relativeLayout5 = (RelativeLayout) rootview.findViewById(R.id.relativeLayout5);
        	relativeLayout5.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View v) {
        			Intent intent = new Intent(getContext(),
        					TemperatureActivity.class);
        			startActivity(intent);
        		}
        	});
        	smallsingleannularui4 = (SmallSingleAnnularUI) rootview.findViewById(R.id.smallsingleannularui4);
        	smallsingleannularui4.setScore(60f);
		
		
        }
		return rootview;
	}
	

	
}
