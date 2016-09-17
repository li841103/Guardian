package com.szhd.util;

/**
 * 
 * @author 卢文王
 *
 * @time 2016.03.02
 *
 * @description 防止连续点击
 *
 */
public class PreventContinuousClick {

   private static long lastClickTime;
   
   public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();   
        if ( time - lastClickTime < 500) {   
            return true;   
        }   
        lastClickTime = time;   
        return false;   
   }
	
}
