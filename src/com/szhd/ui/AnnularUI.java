package com.szhd.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class AnnularUI extends View{
	
	//睡眠小圆画笔
	private Paint sleeppaint; 
	//睡眠粗线圆弧画笔
	private Paint msleeppaint; 
	//睡眠画笔颜色
    private final int sleepcolor = Color.parseColor("#0CC2F7");  
    
	//运动画笔
	private Paint sportpaint;
	//运动粗线圆弧画笔
	private Paint msportpaint;
	//运动画笔颜色
	private final int sportcolor = Color.parseColor("#80D710");  
	
	//心率画笔
	private Paint heartratepaint;
	//心率粗线圆弧画笔
	private Paint mheartratepaint;
	//心率画笔颜色
	private final int heartratecolor = Color.parseColor("#FF215A");  
	
	//体温画笔
	private Paint temperaturepaint;
	//体温粗线圆弧画笔
	private Paint mtemperaturepaint;
	//体温画笔颜色
	private final int temperaturecolor = Color.parseColor("#F7910C");  
	
	
	//背景环画笔
	private Paint backgroundpaint;
	//背景环温画笔颜色
	private final int backgroundcolor = Color.parseColor("#F0F0F0");  
	
	
    //设置绘制文字画笔
    private Paint textpaint;
    //设置绘制文字画笔
    private Paint ftextpaint;
    //设置绘制文字画笔
    private Paint fftextpaint;
    //文字画笔颜色
    private final static int textcolor = Color.parseColor("#4d4d4d");  
	
	
	//睡眠分数
	private float sleepscore = 0f;
	//运动分数
	private float sportscore = 0f;
	//心率分数
	private float heartratescore = 0f;
	//体温分数
	private float temperaturescore = 0f;
	
	private float sleepxitastart = 5;//角度
	private float sleepxitaend = 0;//角度
	
	private float sportxitastart = 5;//角度
	private float sportxitaend = 0;//角度
	
	private float heartratexitastart = 5;//角度
	private float heartratexitaend = 0;//角度
	
	private float temperaturexitastart = 5;//角度
	private float temperaturexitaend = 0;//角度
	
	private float totalnumber = 320f;
	

	public void setSleepscore(int sleepscore) {
		if(sleepscore >= 0 && sleepscore <= 100){
			this.sleepscore = sleepscore;
		}
	}

	public void setSportscore(int sportscore) {
		if(sportscore >= 0 && sportscore <= 100){
			this.sportscore = sportscore;
		}
	}

	public void setHeartratescore(int heartratescore) {
		if(heartratescore >= 0 && heartratescore <= 100){
			this.heartratescore = heartratescore;
		}
	}

	public void setTemperaturescore(int temperaturescore) {
		if(temperaturescore >= 0 && temperaturescore <= 100){
			this.temperaturescore = temperaturescore;
		}
	}

	
	public AnnularUI(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public AnnularUI(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public AnnularUI(Context context) {
		super(context);
		init();
	}

	
	private void init() {
		//睡眠画笔设置
		sleeppaint = new Paint();
		//设置抗锯齿
		sleeppaint.setAntiAlias(true);
		sleeppaint.setStyle(Style.FILL);
		sleeppaint.setColor(sleepcolor);
		//睡眠弧线画笔设置
		msleeppaint = new Paint();
		//设置抗锯齿
		msleeppaint.setAntiAlias(true);
		msleeppaint.setStrokeWidth(dip2px(getContext(), 10));
		msleeppaint.setStyle(Style.STROKE);
		msleeppaint.setColor(sleepcolor);
		
		
		
		//运动画笔设置
		sportpaint = new Paint();
		//设置抗锯齿
		sportpaint.setAntiAlias(true);
		sportpaint.setStyle(Style.FILL);
		sportpaint.setColor(sportcolor);
		//运动弧线画笔设置
		msportpaint = new Paint();
		//设置抗锯齿
		msportpaint.setAntiAlias(true);
		msportpaint.setStrokeWidth(dip2px(getContext(), 10));
		msportpaint.setStyle(Style.STROKE);
		msportpaint.setColor(sportcolor);
		
		
		
		//心率画笔设置
		heartratepaint = new Paint();
		//设置抗锯齿
		heartratepaint.setAntiAlias(true);
		heartratepaint.setStyle(Style.FILL);
		heartratepaint.setColor(heartratecolor);
		//心率弧线画笔设置
		mheartratepaint = new Paint();
		//设置抗锯齿
		mheartratepaint.setAntiAlias(true);
		mheartratepaint.setStrokeWidth(dip2px(getContext(), 10));
		mheartratepaint.setStyle(Style.STROKE);
		mheartratepaint.setColor(heartratecolor);
		
		
		
		//体温画笔设置
		temperaturepaint = new Paint();
		//设置抗锯齿
		temperaturepaint.setAntiAlias(true);
		temperaturepaint.setStyle(Style.FILL);
		temperaturepaint.setColor(temperaturecolor);
		//体温弧线画笔设置
		mtemperaturepaint = new Paint();
		//设置抗锯齿
		mtemperaturepaint.setAntiAlias(true);
		mtemperaturepaint.setStrokeWidth(dip2px(getContext(), 10));
		mtemperaturepaint.setStyle(Style.STROKE);
		mtemperaturepaint.setColor(temperaturecolor);
		
		
		//文字画笔设置
		textpaint = new Paint();
		//设置抗锯齿
		textpaint.setAntiAlias(true);
		textpaint.setStyle(Style.FILL);
		textpaint.setStrokeWidth(dip2px(getContext(), 1));
		textpaint.setColor(textcolor);
		textpaint.setTextAlign(Paint.Align.CENTER);
		textpaint.setTextSize(dip2px(getContext(), 16));
		
		
		//文字画笔设置
		ftextpaint = new Paint();
		//设置抗锯齿
		ftextpaint.setAntiAlias(true);
		ftextpaint.setStyle(Style.FILL);
		ftextpaint.setStrokeWidth(dip2px(getContext(), 1));
		ftextpaint.setColor(textcolor);
		ftextpaint.setTextAlign(Paint.Align.CENTER);
		ftextpaint.setTextSize(dip2px(getContext(), 60));
		
		
		
		//文字画笔设置
		fftextpaint = new Paint();
		//设置抗锯齿
		fftextpaint.setAntiAlias(true);
		fftextpaint.setStyle(Style.FILL);
		fftextpaint.setStrokeWidth(dip2px(getContext(), 1));
		fftextpaint.setColor(textcolor);
		fftextpaint.setTextAlign(Paint.Align.CENTER);
		fftextpaint.setTextSize(dip2px(getContext(), 11));
		
		backgroundpaint = new Paint();
		//设置抗锯齿
		backgroundpaint.setAntiAlias(true);
		backgroundpaint.setStrokeWidth(dip2px(getContext(), 20));
		backgroundpaint.setStyle(Style.STROKE);
		backgroundpaint.setColor(backgroundcolor);
	}
	
	
	@SuppressLint("DrawAllocation") @Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//计算分数为零的图形长度
		int key = 0;
		if(sleepscore == 0){
			key ++;
		}
		if(sportscore == 0){
			key ++;
		}
		if(heartratescore == 0){
			key ++;
		}
		if(temperaturescore == 0){
			key ++;
		}
		switch (key) {
		case 1:
			totalnumber = 330f;
			break;
		case 2:
			totalnumber = 340f;
			break;
		case 3:
			totalnumber = 350f;
			break;
		}
		
		//画灰色背景
		RectF mbackgroundrectf = new RectF();
		mbackgroundrectf.left   = dip2px(getContext(), 10);                            
		mbackgroundrectf.top    = dip2px(getContext(), 10);                                
		mbackgroundrectf.right  = dip2px(getContext(), 190);                      
		mbackgroundrectf.bottom = dip2px(getContext(), 190); 
		//画背景粗线圆弧
		canvas.drawArc(mbackgroundrectf, 0, 360, false, backgroundpaint); 
		
		if((sleepscore + sportscore + heartratescore + temperaturescore) != 0){
			/**
			 * 画睡眠图
			 */
			if(sleepscore != 0){
				sleepxitaend = (totalnumber * sleepscore/(sleepscore + sportscore + heartratescore + temperaturescore)) + sleepxitastart;
				//计算小圆中心点坐标
				double sleepstartx = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.sin(Math.toRadians(sleepxitastart)));
				double sleepstarty = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.cos(Math.toRadians(sleepxitastart)));
				
				RectF sleepstartrectf = new RectF();
				sleepstartrectf.left   = (float)(sleepstartx - dip2px(getContext(), 5));                            
				sleepstartrectf.top    = (float)(sleepstarty - dip2px(getContext(), 5));                                
				sleepstartrectf.right  = (float)(sleepstartx + dip2px(getContext(), 5));                      
				sleepstartrectf.bottom = (float)(sleepstarty + dip2px(getContext(), 5)); 
				//画小圆弧
				canvas.drawArc(sleepstartrectf, 0, 360, false, sleeppaint); 
				
				
				RectF msleeprectf = new RectF();
				msleeprectf.left   = dip2px(getContext(), 5);                            
				msleeprectf.top    = dip2px(getContext(), 5);                                
				msleeprectf.right  = dip2px(getContext(), 195);                      
				msleeprectf.bottom = dip2px(getContext(), 195); 
				//画粗线圆弧
				canvas.drawArc(msleeprectf, 270 - sleepxitaend, sleepxitaend - sleepxitastart, false, msleeppaint); 
				
				
				//计算小圆中心点坐标
				double sleependx = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.sin(Math.toRadians(sleepxitaend)));
				double sleependy = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.cos(Math.toRadians(sleepxitaend)));
				
				RectF sleependrectf = new RectF();
				sleependrectf.left   = (float)(sleependx - dip2px(getContext(), 5));                            
				sleependrectf.top    = (float)(sleependy - dip2px(getContext(), 5));                                
				sleependrectf.right  = (float)(sleependx + dip2px(getContext(), 5));                      
				sleependrectf.bottom = (float)(sleependy + dip2px(getContext(), 5)); 
				//画小圆弧
				canvas.drawArc(sleependrectf, 0, 360, false, sleeppaint); 
			}
			
			
			
			/**
			 * 画运动图
			 */
			if(sportscore != 0){
				if(sleepscore == 0){
					sportxitaend = (totalnumber * sportscore/(sleepscore + sportscore + heartratescore + temperaturescore)) + sportxitastart;
				}else{
					sportxitastart = sleepxitaend + 10f;
					sportxitaend = (totalnumber * sportscore/(sleepscore + sportscore + heartratescore + temperaturescore)) + sportxitastart;
				}
				//计算小圆中心点坐标
				double sportstartx = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.sin(Math.toRadians(sportxitastart)));
				double sportstarty = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.cos(Math.toRadians(sportxitastart)));
				
				RectF sportstartrectf = new RectF();
				sportstartrectf.left   = (float)(sportstartx - dip2px(getContext(), 5));                            
				sportstartrectf.top    = (float)(sportstarty - dip2px(getContext(), 5));                                
				sportstartrectf.right  = (float)(sportstartx + dip2px(getContext(), 5));                      
				sportstartrectf.bottom = (float)(sportstarty + dip2px(getContext(), 5)); 
				//画小圆弧
				canvas.drawArc(sportstartrectf, 0, 360, false, sportpaint); 
				
				
				RectF msportrectf = new RectF();
				msportrectf.left   = dip2px(getContext(), 5);                            
				msportrectf.top    = dip2px(getContext(), 5);                                
				msportrectf.right  = dip2px(getContext(), 195);                      
				msportrectf.bottom = dip2px(getContext(), 195); 
				//画粗线圆弧
				canvas.drawArc(msportrectf, 270 - sportxitaend, sportxitaend - sportxitastart, false, msportpaint); 
				
				
				//计算小圆中心点坐标
				double sportendx = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.sin(Math.toRadians(sportxitaend)));
				double sportendy = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.cos(Math.toRadians(sportxitaend)));
				
				RectF sportendrectf = new RectF();
				sportendrectf.left   = (float)(sportendx - dip2px(getContext(), 5));                            
				sportendrectf.top    = (float)(sportendy - dip2px(getContext(), 5));                                
				sportendrectf.right  = (float)(sportendx + dip2px(getContext(), 5));                      
				sportendrectf.bottom = (float)(sportendy + dip2px(getContext(), 5)); 
				//画小圆弧
				canvas.drawArc(sportendrectf, 0, 360, false, sportpaint); 
			}
			
			
			
			/**
			 * 心率动图
			 */
			if(heartratescore != 0){
				if(sportscore == 0){
					if(sleepscore == 0){
						heartratexitaend = (totalnumber * heartratescore/(sleepscore + sportscore + heartratescore + temperaturescore)) + heartratexitastart;
					}else{
						heartratexitastart = sleepxitaend + 10f;
						heartratexitaend = (totalnumber * heartratescore/(sleepscore + sportscore + heartratescore + temperaturescore)) + heartratexitastart;
					}
				}else{
					heartratexitastart = sportxitaend + 10f;
					heartratexitaend = (totalnumber * heartratescore/(sleepscore + sportscore + heartratescore + temperaturescore)) + heartratexitastart;
				}
				//计算小圆中心点坐标
				double heartratestartx = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.sin(Math.toRadians(heartratexitastart)));
				double heartratestarty = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.cos(Math.toRadians(heartratexitastart)));
				
				RectF heartratestartrectf = new RectF();
				heartratestartrectf.left   = (float)(heartratestartx - dip2px(getContext(), 5));                            
				heartratestartrectf.top    = (float)(heartratestarty - dip2px(getContext(), 5));                                
				heartratestartrectf.right  = (float)(heartratestartx + dip2px(getContext(), 5));                      
				heartratestartrectf.bottom = (float)(heartratestarty + dip2px(getContext(), 5)); 
				//画小圆弧
				canvas.drawArc(heartratestartrectf, 0, 360, false, heartratepaint); 
				
				
				RectF mheartraterectf = new RectF();
				mheartraterectf.left   = dip2px(getContext(), 5);                            
				mheartraterectf.top    = dip2px(getContext(), 5);                                
				mheartraterectf.right  = dip2px(getContext(), 195);                      
				mheartraterectf.bottom = dip2px(getContext(), 195); 
				//画粗线圆弧
				canvas.drawArc(mheartraterectf, 270 - heartratexitaend, heartratexitaend - heartratexitastart, false, mheartratepaint); 
				
				
				//计算小圆中心点坐标
				double heartrateendx = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.sin(Math.toRadians(heartratexitaend)));
				double heartrateendy = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.cos(Math.toRadians(heartratexitaend)));
				
				RectF heartrateendrectf = new RectF();
				heartrateendrectf.left   = (float)(heartrateendx - dip2px(getContext(), 5));                            
				heartrateendrectf.top    = (float)(heartrateendy - dip2px(getContext(), 5));                                
				heartrateendrectf.right  = (float)(heartrateendx + dip2px(getContext(), 5));                      
				heartrateendrectf.bottom = (float)(heartrateendy + dip2px(getContext(), 5)); 
				//画小圆弧
				canvas.drawArc(heartrateendrectf, 0, 360, false, heartratepaint); 
			}
			
			
			
			/**
			 * 体温动图
			 */
			if(temperaturescore != 0){
				if(heartratescore == 0){
					if(sportscore == 0){
						if(sleepscore == 0){
							temperaturexitaend = (totalnumber * temperaturescore/(sleepscore + sportscore + heartratescore + temperaturescore)) + temperaturexitastart;
						}else{
							temperaturexitastart = sleepxitaend + 10f;
							temperaturexitaend = (totalnumber * temperaturescore/(sleepscore + sportscore + heartratescore + temperaturescore)) + temperaturexitastart;
						}
					}else{
						temperaturexitastart = sportxitaend + 10f;
						temperaturexitaend = (totalnumber * temperaturescore/(sleepscore + sportscore + heartratescore + temperaturescore)) + temperaturexitastart;
					}
				}else{
					temperaturexitastart = heartratexitaend + 10f;
					temperaturexitaend = (totalnumber * temperaturescore/(sleepscore + sportscore + heartratescore + temperaturescore)) + temperaturexitastart;
				}
				//计算小圆中心点坐标
				double temperaturestartx = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.sin(Math.toRadians(temperaturexitastart)));
				double temperaturestarty = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.cos(Math.toRadians(temperaturexitastart)));
				
				RectF temperaturestartrectf = new RectF();
				temperaturestartrectf.left   = (float)(temperaturestartx - dip2px(getContext(), 5));                            
				temperaturestartrectf.top    = (float)(temperaturestarty - dip2px(getContext(), 5));                                
				temperaturestartrectf.right  = (float)(temperaturestartx + dip2px(getContext(), 5));                      
				temperaturestartrectf.bottom = (float)(temperaturestarty + dip2px(getContext(), 5)); 
				//画小圆弧
				canvas.drawArc(temperaturestartrectf, 0, 360, false, temperaturepaint); 
				
				
				RectF mtemperaturerectf = new RectF();
				mtemperaturerectf.left   = dip2px(getContext(), 5);                            
				mtemperaturerectf.top    = dip2px(getContext(), 5);                                
				mtemperaturerectf.right  = dip2px(getContext(), 195);                      
				mtemperaturerectf.bottom = dip2px(getContext(), 195); 
				//画粗线圆弧
				canvas.drawArc(mtemperaturerectf, 270 - temperaturexitaend, temperaturexitaend - temperaturexitastart, false, mtemperaturepaint); 
				
				
				//计算小圆中心点坐标
				double temperatureendx = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.sin(Math.toRadians(temperaturexitaend)));
				double temperatureendy = dip2px(getContext(), 100) - (dip2px(getContext(), 95) * Math.cos(Math.toRadians(temperaturexitaend)));
				
				RectF temperatureendrectf = new RectF();
				temperatureendrectf.left   = (float)(temperatureendx - dip2px(getContext(), 5));                            
				temperatureendrectf.top    = (float)(temperatureendy - dip2px(getContext(), 5));                                
				temperatureendrectf.right  = (float)(temperatureendx + dip2px(getContext(), 5));                      
				temperatureendrectf.bottom = (float)(temperatureendy + dip2px(getContext(), 5)); 
				//画小圆弧
				canvas.drawArc(temperatureendrectf, 0, 360, false, temperaturepaint); 
			}
		}
		
		canvas.drawText("倍儿棒", dip2px(getContext(), 100), dip2px(getContext(), 60), textpaint);
		canvas.drawText("92", dip2px(getContext(), 100), dip2px(getContext(), 120), ftextpaint);
		canvas.drawText("得分保持上升趋势", dip2px(getContext(), 100), dip2px(getContext(), 145), fftextpaint);
		canvas.drawText("超过80%用户", dip2px(getContext(), 100), dip2px(getContext(), 160), fftextpaint);
		
	}
	
	
	/** 
	* 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	*/  
	public static int dip2px(Context context, float dpValue) {  
	  final float scale = context.getResources().getDisplayMetrics().density;  
	  return (int) (dpValue * scale + 0.5f);  
	}  
	  
	/** 
	* 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
	*/  
	public static int px2dip(Context context, float pxValue) {  
	  final float scale = context.getResources().getDisplayMetrics().density;  
	  return (int) (pxValue / scale + 0.5f);  
	}   
	
	
	
}
