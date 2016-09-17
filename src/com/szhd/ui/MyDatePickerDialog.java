package com.szhd.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * 
 * @author 卢文王
 *
 * @time 2016.03.02
 *
 * @description	禁止MyDatePickerDialog弹出软键盘
 *
 */
public class MyDatePickerDialog extends DatePickerDialog {  
	  
    public MyDatePickerDialog(Context context,   
            OnDateSetListener callBack, int year, int monthOfYear,  
            int dayOfMonth) {  
        super(context, callBack, year, monthOfYear, dayOfMonth);  
    }  

    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);  
    }  
    
    
}
