package com.szhd.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.szhd.guardian.R;

public class AddDialog extends Dialog implements android.view.View.OnClickListener{

	private TextView addperson;
	private TextView addsth;
	private MyDialogListener listener;
	
	
	public interface MyDialogListener{   
        public void onClick(View view);   
    }  
	
	
	public AddDialog(Context context, int theme) {
		super(context, theme);
	}

	
	public AddDialog(Context context) {
		super(context);
	}
	
	
	public AddDialog(Context context, int theme, MyDialogListener leaveMyDialogListener) {
        super(context,theme);
        this.listener = leaveMyDialogListener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.adddialog);
	    addperson = (TextView) findViewById(R.id.addperson);
	    addsth = (TextView) findViewById(R.id.addsth);
	    addperson.setOnClickListener(this);
	    addsth.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		 listener.onClick(v);
	}
	
	
}
