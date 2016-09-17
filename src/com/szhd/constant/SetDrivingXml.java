package com.szhd.constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;
import com.szhd.guardian.LoginActivity;
import com.szhd.map.Alert;
import com.szhd.ui.FunctionActivity;
import com.szhd.util.MatchString;
import com.szhd.webservice.Communicate;

public class SetDrivingXml {
	
	private static StringBuffer sb = new StringBuffer();
	
	public static void setSetSecurePath(Context ctx, DrivingRouteLine mRouteLine) throws IOException{
		
		//起始点
		LatLng starting = mRouteLine.getStarting().getLocation();
	    sb.append("|"+starting.longitude+":"+starting.latitude);
	    
	    
	    List<DrivingStep> steps = mRouteLine.getAllStep();
        int stepNum = steps.size();
        List<LatLng> points = new ArrayList<LatLng>();
        for (int i = 0; i < stepNum ; i++) {
            if (i == stepNum - 1) {
                points.addAll(steps.get(i).getWayPoints());
            } else {
                points.addAll(steps.get(i).getWayPoints().subList(0, steps.get(i).getWayPoints().size() - 1));
            }
        } 
        
        for(LatLng l : points){
        	sb.append("|"+l.longitude+":"+l.latitude);
        }
        
//		for(DrivingRouteLine.DrivingStep step : mRouteLine.getAllStep()){
//		
//			 //入口点
//			 if (step.getEntrance() != null) {
//				 LatLng entrance = step.getEntrance().getLocation();
//				 sb.append("|"+entrance.longitude+":"+entrance.latitude);
//             }
//             // 最后路段绘制出口点
//             if (mRouteLine.getAllStep().indexOf(step) == (mRouteLine
//                     .getAllStep().size() - 1) && step.getExit() != null) {
//            	 LatLng exit = step.getExit().getLocation();
//            	 sb.append("|"+exit.longitude+":"+exit.latitude);
//             }
//			
//		}
		
	    //结束点
		LatLng terminal = mRouteLine.getTerminal().getLocation();
		sb.append("|"+terminal.longitude+":"+terminal.latitude);
		sb.append("|");
		
		//设置要插入的数据
		List<String> s = new ArrayList<String>();
		s.add(sb.toString());
		Toast.makeText(ctx, sb.toString(),
				Toast.LENGTH_LONG).show();
		Log.d("路径", sb.toString());
		
		
		Intent intent = new Intent(ctx, Alert.class);
		intent.putExtra("lujing", sb.toString());
		//ctx.startActivity(intent);
		
		
		//设置插入的标签
		List<String> starttags = new ArrayList<String>();
		starttags.add("<content>");
		//设置解析的标签
		Communicate.sss.add("<SetSecurePathResult>");
		Communicate.sss.add("</SetSecurePathResult>");
		//设置发送数据
		Communicate.xmlclassdata = MatchString.addString(ctx, "SetSecurePath.xml", s, starttags);
		
	}
	
	
}
