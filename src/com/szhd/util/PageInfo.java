package com.szhd.util;

import java.util.List;

import java.util.ArrayList;


/**
 * 分页类
 * */
public class PageInfo{
	
	
    /** 
      * 总页数 
      */
    private int totalPage;
    
    /** 
    * 总行数 
    */
    private int totalRows;
    
    /** 
       * 每页显示条数 
    */
    private int avgRows = 9;
    
    
    
    public int getTotalPage(){
        return totalPage;
    }
    
    public int getTotalRows(){
        return totalRows;
    }
    
    public int getAvgRows(){
        return avgRows;
    }
    
    public List<TemperatureInfo> getAllList(){
        return allList;
    }
    
    public void setAllList(List<TemperatureInfo> allList){
        this.allList = allList;
        initalPageInfo();
    }
    
    /** 
    * 原集合 
    */
    private List<TemperatureInfo> allList;
    
    public PageInfo(int avgRows, List<TemperatureInfo> list){
        this.avgRows = avgRows;
        this.allList = list;
        initalPageInfo();
    }
    
    /**
     * 初始化页面大小
     * */
    private void initalPageInfo(){
        if(allList != null){
            this.totalRows = allList.size();
            int tmp = totalRows % avgRows;
            this.totalPage = (totalRows / avgRows) + (tmp > 0 ? 1 : 0);
        }
    }
    
    /**
     * 获取当前分页数据
     * @param pageIndex 要取数的页码
     * */
    public List<TemperatureInfo> getPageDetail(int pageIndex){
        List<TemperatureInfo> list=new ArrayList<TemperatureInfo>();
        int startindex=(pageIndex-1)*avgRows;
        int endindex=startindex+avgRows;
        for(int i=startindex;i<totalRows&&i<endindex;i++)
        {
            list.add(allList.get(i)); 
        }
        return list;
    }
    
    
}
