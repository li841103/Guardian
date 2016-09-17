package com.szhd.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

public class HeartRateInfo {


	/**
     * 测量时间
     * */
    private Date date;
    
    /**
     * 心率值
     * */
    private int temperature;
   
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    
    
    /**
     * 获取测量时间 格式HH：mm
     * */
    @SuppressLint("SimpleDateFormat")
    public String getViatlTimeForHm(){
        if(date==null)return null;
        return formatter.format(this.date);
    }
    
    
    
    
    public int getTemperature(){
        return temperature;
    }
    
    @SuppressLint("SimpleDateFormat")
    public void setDateByString(String time){
    	try{
    		date=formatter2.parse(time);
    	}catch (ParseException e){
    		e.printStackTrace();
    	}
    }
    
    public void setTemperature(int temperature){
        this.temperature = temperature;
    }
	
	
}
