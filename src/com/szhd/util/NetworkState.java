package com.szhd.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkState {

	    /** 
	     * @param activity 
	     * @return false 为没有可用网络  true 为当前网络正常 
	     */  
	    public static boolean hasInternet(Activity activity) {  
	  
	    	ConnectivityManager manager = (ConnectivityManager) activity  
	    			.getSystemService(Context.CONNECTIVITY_SERVICE);  
	    	NetworkInfo info = manager.getActiveNetworkInfo();  
		    if (info == null || !info.isAvailable()) {  
		    	return false;  
		    }  
		    if (info.isRoaming()) {  
		    	return true;  
		    }  
		    return true;  
	  
	    }  
	
	

}
