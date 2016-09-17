package com.szhd.constant;

import java.util.ArrayList;
import java.util.List;

import com.szhd.util.MatchString;
import com.szhd.webservice.Communicate;

import android.content.Context;

public class UploadImageXml {
	
	public static StringBuffer sb = new StringBuffer();
	public static StringBuffer sb2 = new StringBuffer();
	
	public static void setImageView(Context ctx){
		//设置要插入的数据
		List<String> s = new ArrayList<String>();
		s.add(sb.toString());
		s.add(sb2.toString());
		//设置插入的标签
		List<String> starttags = new ArrayList<String>();
		starttags.add("<filename>");
		starttags.add("<image>");
		//设置解析的标签
		Communicate.sss.add("<uploadImageResult>");
		Communicate.sss.add("</uploadImageResult>");
		//设置发送数据
		Communicate.xmlclassdata = MatchString.addString(ctx, "UploadImage.xml", s, starttags);
		
		sb.setLength(0);
		sb2.setLength(0);
	}
	
	
}
