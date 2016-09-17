package com.szhd.ui;

import com.szhd.guardian.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class SingleAnnularUI extends View{

	
	private float xitastart = 0;//角度
	private float xitaend = 0;//角度
	
	private float totalnumber = 360f;
	
	private String colornum = "#0CC2F7";
	//分数
	private float score = 50f;
	
	//小圆画笔
	private Paint paint; 
	//粗线圆弧画笔
	private Paint mpaint; 
    
	//背景环画笔
	private Paint backgroundpaint;
	//背景环温画笔颜色
	private final static int backgroundcolor = Color.parseColor("#F0F0F0");  
	
	
    //设置绘制文字画笔
    private Paint textpaint;
    //文字画笔颜色
    private final static int textcolor = Color.parseColor("#4d4d4d");  
    
    
    //设置绘制文字画笔
    private Paint fftextpaint;
    //文字画笔颜色
    private final static int fftextcolor = Color.parseColor("#8a8a8a");  
	

	public void setScore(float score) {
		this.score = score;
	}

	public void setColornum(String colornum) {
		this.colornum = colornum;
	}

	public SingleAnnularUI(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//获取颜色属性
		TypedArray tArray = context.obtainStyledAttributes(attrs,
				R.styleable.SingleAnnularUI);
		colornum = tArray.getString(R.styleable.SingleAnnularUI_uisclocor);
		init();
	}
	
	public SingleAnnularUI(Context context, AttributeSet attrs) {
		super(context, attrs);
		//获取颜色属性
		TypedArray tArray = context.obtainStyledAttributes(attrs,
				R.styleable.SingleAnnularUI);
		colornum = tArray.getString(R.styleable.SingleAnnularUI_uisclocor);
		init();
	}
	
	public SingleAnnularUI(Context context) {
		super(context);
		init();
	}

	
	private void init() {
		//体温小圆弧画笔设置
		paint = new Paint();
		//设置抗锯齿
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		paint.setColor(Color.parseColor(colornum));
		
		//体温弧线画笔设置
		mpaint = new Paint();
		//设置抗锯齿
		mpaint.setAntiAlias(true);
		mpaint.setStrokeWidth(dip2px(getContext(), 5));
		mpaint.setStyle(Style.STROKE);
		mpaint.setColor(Color.parseColor(colornum));
		
		backgroundpaint = new Paint();
		//设置抗锯齿
		backgroundpaint.setAntiAlias(true);
		backgroundpaint.setStrokeWidth(dip2px(getContext(), 5));
		backgroundpaint.setStyle(Style.STROKE);
		backgroundpaint.setColor(backgroundcolor);
		
		
		//文字画笔设置
		textpaint = new Paint();
		//设置抗锯齿
		textpaint.setAntiAlias(true);
		textpaint.setStyle(Style.FILL);
		textpaint.setStrokeWidth(dip2px(getContext(), 1));
		textpaint.setColor(textcolor);
		textpaint.setTextAlign(Paint.Align.CENTER);
		textpaint.setTextSize(dip2px(getContext(), 20));
		
		
		//文字画笔设置
		fftextpaint = new Paint();
		//设置抗锯齿
		fftextpaint.setAntiAlias(true);
		fftextpaint.setStyle(Style.FILL);
		fftextpaint.setStrokeWidth(dip2px(getContext(), 1));
		fftextpaint.setColor(fftextcolor);
		fftextpaint.setTextAlign(Paint.Align.CENTER);
		fftextpaint.setTextSize(dip2px(getContext(), 11));
		
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//画灰色背景
		RectF mbackgroundrectf = new RectF();
		mbackgroundrectf.left   = dip2px(getContext(), 2.5f);                            
		mbackgroundrectf.top    = dip2px(getContext(), 2.5f);                                
		mbackgroundrectf.right  = dip2px(getContext(), 77.5f);                      
		mbackgroundrectf.bottom = dip2px(getContext(), 77.5f); 
		//画背景粗线圆弧
		canvas.drawArc(mbackgroundrectf, 0, 360, false, backgroundpaint); 
		
		/**
		 * 画睡眠图
		 */
		if(score != 0){
			xitaend = (totalnumber*(score/100f)) + xitastart;
			//计算小圆中心点坐标
			double startx = dip2px(getContext(), 40) - (dip2px(getContext(), 37.5f) * Math.sin(Math.toRadians(xitastart)));
			double starty = dip2px(getContext(), 40) - (dip2px(getContext(), 37.5f) * Math.cos(Math.toRadians(xitastart)));
			
			RectF startrectf = new RectF();
			startrectf.left   = (float)(startx - dip2px(getContext(), 2.5f));                            
			startrectf.top    = (float)(starty - dip2px(getContext(), 2.5f));                                
			startrectf.right  = (float)(startx + dip2px(getContext(), 2.5f));                      
			startrectf.bottom = (float)(starty + dip2px(getContext(), 2.5f)); 
			//画小圆弧
			canvas.drawArc(startrectf, 0, 360, false, paint); 
			
			
			RectF mrectf = new RectF();
			mrectf.left   = dip2px(getContext(), 2.5f);                            
			mrectf.top    = dip2px(getContext(), 2.5f);                                
			mrectf.right  = dip2px(getContext(), 77.5f);                      
			mrectf.bottom = dip2px(getContext(), 77.5f); 
			//画粗线圆弧
			canvas.drawArc(mrectf, 270 - xitaend, xitaend - xitastart, false, mpaint); 
			
			
			//计算小圆中心点坐标
			double endx = dip2px(getContext(), 40) - (dip2px(getContext(), 37.5f) * Math.sin(Math.toRadians(xitaend)));
			double endy = dip2px(getContext(), 40) - (dip2px(getContext(), 37.5f) * Math.cos(Math.toRadians(xitaend)));
			
			RectF endrectf = new RectF();
			endrectf.left   = (float)(endx - dip2px(getContext(), 2.5f));                            
			endrectf.top    = (float)(endy - dip2px(getContext(), 2.5f));                                
			endrectf.right  = (float)(endx + dip2px(getContext(), 2.5f));                       
			endrectf.bottom = (float)(endy + dip2px(getContext(), 2.5f)); 
			//画小圆弧
			canvas.drawArc(endrectf, 0, 360, false, paint);
		}
		
		canvas.drawText((int)score+"", dip2px(getContext(), 40), dip2px(getContext(), 45), textpaint);
		String state = "";
		if(score >= 80){
			state = "优良";
		}else if(score < 80 && score >=60){
			state = "一般";
		}else{
			state = "较差";
		}
		canvas.drawText(state, dip2px(getContext(), 40), dip2px(getContext(), 60), fftextpaint);
	}
	
	
	/** 
	* 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	*/  
	public static float dip2px(Context context, float dpValue) {  
	  final float scale = context.getResources().getDisplayMetrics().density;  
	  return (dpValue * scale + 0.5f);  
	}  
	
	
}
