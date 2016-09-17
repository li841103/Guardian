package com.szhd.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

public class TemperatureInfo implements Serializable {

	
	/**
     * 测量时间
     * */
    private Date date;
    
    /**
     * 体温值
     * */
    private float temperature;
   
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat formatter3 = new SimpleDateFormat("HH:mm:ss");
    
    
    public Date getDate(){
        return this.date;
    }
    
    
    public String getVitalTimeString(){
        if(this.date==null)return null;
        return formatter.format(this.date);
    }
    
    
    /**
     * 获取测量时间 格式：HH:mm
     * */
    @SuppressLint("SimpleDateFormat") 
    public String getVitalTimeHmStr(){
        if(this.date==null)return null;
        return formatter2.format(this.date);
    }
    
    
    @SuppressLint("SimpleDateFormat")
    public void setDateByString(String time){
        try{
            this.date=formatter.parse(time);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }
    
    
    /**
     * 获取测量时间 格式HH：mm
     * */
    @SuppressLint("SimpleDateFormat")
    public String getViatlTimeForHm(){
        if(this.date==null)return null;
        return formatter3.format(this.date);
    }
    
    
    public void setDate(Date date){
        this.date = date;
    }
    
    
    public float getTemperature()
    {
        return temperature;
    }
    
    
    /**
     * 获取温度值
     * */
    public String getTemperatureStr(){
        return temperature+"℃";
    }
    
    
    public void setTemperature(float temperature){
        this.temperature = temperature;
    }
	
    
    
}
