package com.szhd.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.szhd.guardian.R;

public class SetDialog extends Dialog implements android.view.View.OnClickListener{

	
	private TextView clear;
	private TextView update;
	private TextView quite;
	private MyDialogListener listener;
	
	
	public interface MyDialogListener{   
        public void onClick(View view);   
    }  
	
	
	public SetDialog(Context context, int theme) {
		super(context, theme);
	}

	
	public SetDialog(Context context) {
		super(context);
	}
	
	
	public SetDialog(Context context, int theme, MyDialogListener leaveMyDialogListener) {
        super(context,theme);
        this.listener = leaveMyDialogListener;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.setdialog);
	    clear = (TextView) findViewById(R.id.clear);
	    clear.setOnClickListener(this);
	    update = (TextView) findViewById(R.id.update);
	    update.setOnClickListener(this);
	    quite = (TextView) findViewById(R.id.quite);
	    quite.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		 listener.onClick(v);
	}
	
	
}
