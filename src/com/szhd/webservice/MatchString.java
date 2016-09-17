package com.szhd.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;

public class MatchString {
	
	public static String matchIntex(InputStream ism, List<String> tag){
		StringBuffer out = new StringBuffer(); 
        byte[] b = new byte[4096]; 
        try {
			for(int n; (n = ism.read(b)) != -1;){ 
			    out.append(new String(b,0,n)); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
        int index[] = {-1,-1};
        for(int j=0; j<2; j++){
        	for(int i=0; i<out.length()-tag.get(j).length(); i++){
        		if(out.substring(i, tag.get(j).length()+i).equals(tag.get(j))){
        			index[j] = i;
        		}
        	}
        }
        if(index[0] == -1 || index[1] == -1){
        	return null;
        }else{
        	return out.substring(index[0]+tag.get(0).length(),index[1]);
        }
	}
	
	/**
	 * 浼犲叆鐨勫�艰鍜屾爣绛句竴涓�瀵瑰簲
	 */
	public static String addString(Context ctx, String path, List<String> s, List<String> starttags){
		StringBuffer out = new StringBuffer(); 
		byte[] b = new byte[4096]; 
        try {
        	InputStream ism = ctx.getAssets().open(path);
			for(int n; (n = ism.read(b)) != -1;){ 
			    out.append(new String(b,0,n)); 
			}
        } catch (IOException e) {
        	e.printStackTrace();
        } 
        if(s != null && starttags != null){
        	for(String tag : starttags){
        		int index = -1;
        		for(int i=0; i<out.length()-tag.length(); i++){
        			if(out.substring(i, tag.length()+i).equals(tag)){
        				index = i;
        			}
        		}
        		out.insert(index+tag.length(), s.get(starttags.indexOf(tag)));
        	}
        }
		return out.toString();
	}
	
	
	
}
