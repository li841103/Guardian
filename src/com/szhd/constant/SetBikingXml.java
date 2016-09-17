package com.szhd.constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.szhd.util.MatchString;
import com.szhd.webservice.Communicate;

public class SetBikingXml {

private static StringBuffer sb = new StringBuffer();
	
	public static void setSetSecurePath(Context ctx, BikingRouteLine mRouteLine) throws IOException{
		
		//起始点
		LatLng starting = mRouteLine.getStarting().getLocation();
	    sb.append("|"+starting.longitude+":"+starting.latitude);
		
		for(BikingRouteLine.BikingStep step : mRouteLine.getAllStep()){
		
			 //入口点
			 if (step.getEntrance() != null) {
				 LatLng entrance = step.getEntrance().getLocation();
				 sb.append("|"+entrance.longitude+":"+entrance.latitude);
             }
             // 最后路段绘制出口点
             if (mRouteLine.getAllStep().indexOf(step) == (mRouteLine
                     .getAllStep().size() - 1) && step.getExit() != null) {
            	 LatLng exit = step.getExit().getLocation();
            	 sb.append("|"+exit.longitude+":"+exit.latitude);
             }
			
		}
		
	    //结束点
		LatLng terminal = mRouteLine.getTerminal().getLocation();
		sb.append("|"+terminal.longitude+":"+terminal.latitude);
		sb.append("|");
		
		//设置要插入的数据
		List<String> s = new ArrayList<String>();
		s.add(sb.toString());
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
