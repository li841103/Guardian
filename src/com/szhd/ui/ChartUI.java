package com.szhd.ui;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;

import com.szhd.guardian.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

public class ChartUI extends View{

	
	
	private Path curvepath,shadowpath;
	
	
	//曲线画笔
	private Paint curvepaint;
	//曲线画笔颜色
    private final static int curvecolor = Color.parseColor("#0CC2F7");  
    
    
    //曲线阴影画笔
    private Paint shadowpaint;
    //曲线阴影画笔颜色
    private final static int shadowcolor = Color.parseColor("#F8F8F8");  
    
    
    //曲线阴影画笔
    private Paint arcpaint;
    //曲线阴影画笔颜色
    private final static int arccolor = Color.parseColor("#F8F8F8");  
    
    
    //设置绘制文字画笔
    private Paint textpaint;
    //文字画笔颜色
    private final static int textcolor = Color.parseColor("#4d4d4d");  
    
    
    //设置绘制文字画笔
    private Paint fftextpaint;
    //文字画笔颜色
    private final static int fftextcolor = Color.parseColor("#FFFFFF");  
	
    //设置数据，此数据为时间长度
    private float[] f = {12,11,13,10,15};
    private float[] l = {12,11,13,10,15};
    //将警戒线调高可以避免出现报警
    //此数据为红色警戒线数值
    private float redwar = 14f;
    //此数据为黄色警戒线数值
    private float yelwar = 12.5f;
    //此数据为日期
    private String[] datedata = {"05-06","05-07","05-08","05-09","今天"};
    //此数据为睡眠时间
    private String[] sleeptime = {"5小时20分","5小时20分","5小时20分","5小时20分","5小时20分"};
    
    
    
    private float[] value(float[] a){
    	float[] g = {0,0,0,0,0};
    	//由小到大排序
    	Arrays.sort(a);
    	for(int i=0; i<5; i++){
    		g[i] =  dip2px(getContext(), 135f) - (dip2px(getContext(), 135f) * (l[i] - a[0])/(a[4]-a[0]));
    	}
    	return g;
    }



	public ChartUI(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public ChartUI(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public ChartUI(Context context) {
		super(context);
		init();
	}

	private void init() {
		//曲线相关初始化  
		curvepath = new Path();  
		shadowpath = new Path();  
		
		
		//曲线画笔设置
		curvepaint = new Paint();
		//设置抗锯齿
		curvepaint.setAntiAlias(true);
		curvepaint.setStyle(Style.STROKE);
		curvepaint.setStrokeWidth(dip2px(getContext(), 4));
		curvepaint.setColor(curvecolor);
		
		
		//曲线阴影画笔设置
		shadowpaint = new Paint();
		//设置抗锯齿
		shadowpaint.setAntiAlias(true);
		shadowpaint.setStyle(Style.STROKE);
		shadowpaint.setStrokeWidth(dip2px(getContext(), 4));
		shadowpaint.setColor(shadowcolor);
		
		
		//文字画笔设置
		textpaint = new Paint();
		//设置抗锯齿
		textpaint.setAntiAlias(true);
		textpaint.setStyle(Style.FILL);
		textpaint.setStrokeWidth(dip2px(getContext(), 1));
		textpaint.setColor(textcolor);
		textpaint.setTextAlign(Paint.Align.CENTER);
		textpaint.setTextSize(dip2px(getContext(), 10));
		
		
		//文字画笔设置
		fftextpaint = new Paint();
		//设置抗锯齿
		fftextpaint.setAntiAlias(true);
		fftextpaint.setStyle(Style.FILL);
		fftextpaint.setStrokeWidth(dip2px(getContext(), 1));
		fftextpaint.setColor(fftextcolor);
		fftextpaint.setTextAlign(Paint.Align.CENTER);
		fftextpaint.setTextSize(dip2px(getContext(), 10));
		
		
		arcpaint = new Paint();
		//设置抗锯齿
		arcpaint.setAntiAlias(true);
		arcpaint.setStyle(Style.FILL);
		arcpaint.setColor(arccolor);
	}

	
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//绘制背景图
	    Bitmap chart_border = ((BitmapDrawable)getResources().getDrawable(R.drawable.chart_border)).getBitmap();
	    canvas.drawBitmap(small(chart_border), 0, dip2px(getContext(), 50), null);


	    List<Integer> points_x = new LinkedList<Integer>();
	    List<Integer> points_y = new LinkedList<Integer>();
	    List<Integer> points_sy = new LinkedList<Integer>();
	    points_x.add(dip2px(getContext(), 306));
	    points_x.add(dip2px(getContext(), 238));
	    points_x.add(dip2px(getContext(), 170));
	    points_x.add(dip2px(getContext(), 102));
	    points_x.add(dip2px(getContext(), 34));
	    
	    points_sy.add(dip2px(getContext(), 80) + (int)(value(f)[0]/1.296f));
	    points_sy.add(dip2px(getContext(), 80) + (int)(value(f)[1]/1.296f));
	    points_sy.add(dip2px(getContext(), 80) + (int)(value(f)[2]/1.296f));
	    points_sy.add(dip2px(getContext(), 80) + (int)(value(f)[3]/1.296f));
	    points_sy.add(dip2px(getContext(), 80) + (int)(value(f)[4]/1.296f));
	    //绘制背景
	    drawCurve(canvas, points_x, points_sy, shadowpaint);
	    
		RectF startrectf = new RectF();
		startrectf.left   = (float)(dip2px(getContext(), 306) - dip2px(getContext(), 2));                            
		startrectf.top    = (float)(dip2px(getContext(), 80) + (int)(value(f)[0]/1.296f) - dip2px(getContext(), 2));                                
		startrectf.right  = (float)(dip2px(getContext(), 306) + dip2px(getContext(), 2));                      
		startrectf.bottom = (float)(dip2px(getContext(), 80) + (int)(value(f)[0]/1.296f) + dip2px(getContext(), 2)); 
		//画小圆弧
		canvas.drawArc(startrectf, 0, 360, false, arcpaint); 
		
		RectF endrectf = new RectF();
		endrectf.left   = (float)(dip2px(getContext(), 34) - dip2px(getContext(), 2));                            
		endrectf.top    = (float)(dip2px(getContext(), 80) + (int)(value(f)[4]/1.296f) - dip2px(getContext(), 2));                                
		endrectf.right  = (float)(dip2px(getContext(), 34) + dip2px(getContext(), 2));                      
		endrectf.bottom = (float)(dip2px(getContext(), 80) + (int)(value(f)[4]/1.296f) + dip2px(getContext(), 2)); 
		//画小圆弧
		canvas.drawArc(endrectf, 0, 360, false, arcpaint);
	    
	    
	    
	    
	    //绘制平均刻度线
	    Bitmap chart_average = ((BitmapDrawable)getResources().getDrawable(R.drawable.chart_average)).getBitmap();
	    float ff = 0f;
	    for(int i=0; i<5; i++){
	    	ff += value(f)[i];
	    }
	    canvas.drawBitmap(small(chart_average), 0, dip2px(getContext(), 50) + ff/5, null);
	    
	    
	    points_y.add(dip2px(getContext(), 50) + (int)value(f)[0]);
	    points_y.add(dip2px(getContext(), 50) + (int)value(f)[1]);
	    points_y.add(dip2px(getContext(), 50) + (int)value(f)[2]);
	    points_y.add(dip2px(getContext(), 50) + (int)value(f)[3]);
	    points_y.add(dip2px(getContext(), 50) + (int)value(f)[4]);
	    //绘制曲线
	    new DrawCurve().drawCurve(canvas, points_x, points_y, curvepaint, curvepath);
	    
	    
	    
	    //绘制坐标点图标
	    Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.point)).getBitmap();
	    canvas.drawBitmap(small(bitmap), dip2px(getContext(), 300f), dip2px(getContext(), 45) + (int)value(f)[0], null);
	    canvas.drawBitmap(small(bitmap), dip2px(getContext(), 232f), dip2px(getContext(), 45) + (int)value(f)[1], null);
	    canvas.drawBitmap(small(bitmap), dip2px(getContext(), 164f), dip2px(getContext(), 45) + (int)value(f)[2], null);
	    canvas.drawBitmap(small(bitmap), dip2px(getContext(), 96f), dip2px(getContext(), 45) + (int)value(f)[3], null);
	    canvas.drawBitmap(small(bitmap), dip2px(getContext(), 28f), dip2px(getContext(), 45) + (int)value(f)[4], null);
	    
	    
	    //绘制标签图片
	    Bitmap bitmap_chart_white = ((BitmapDrawable)getResources().getDrawable(R.drawable.chart_white)).getBitmap();
	    Bitmap bitmap_chart_blue = ((BitmapDrawable)getResources().getDrawable(R.drawable.chart_blue)).getBitmap();
	    Bitmap bitmap_chart_red = ((BitmapDrawable)getResources().getDrawable(R.drawable.chart_red)).getBitmap();
	    Bitmap bitmap_chart_yellow = ((BitmapDrawable)getResources().getDrawable(R.drawable.chart_yellow)).getBitmap();
	    if(l[0] < yelwar){
	    	canvas.drawBitmap(small(bitmap_chart_blue), dip2px(getContext(), 271), dip2px(getContext(), 3) + (int)value(f)[0], null);
	    }else if(l[0] >= yelwar && l[0] < redwar){
	    	canvas.drawBitmap(small(bitmap_chart_yellow), dip2px(getContext(), 271), dip2px(getContext(), 3) + (int)value(f)[0], null);
	    }else if(l[0] >= redwar){
	    	canvas.drawBitmap(small(bitmap_chart_red), dip2px(getContext(), 271), dip2px(getContext(), 3) + (int)value(f)[0], null);
	    }
	    
	    
	    if(l[1] < yelwar){
	    	canvas.drawBitmap(small(bitmap_chart_white), dip2px(getContext(), 203), dip2px(getContext(), 3) + (int)value(f)[1], null);
	    }else if(l[1] >= yelwar && l[1] < redwar){
	    	canvas.drawBitmap(small(bitmap_chart_yellow), dip2px(getContext(), 203), dip2px(getContext(), 3) + (int)value(f)[1], null);
	    }else if(l[1] >= redwar){
	    	canvas.drawBitmap(small(bitmap_chart_red), dip2px(getContext(), 203), dip2px(getContext(), 3) + (int)value(f)[1], null);
	    }
	    
	    
	    if(l[2] < yelwar){
	    	canvas.drawBitmap(small(bitmap_chart_white), dip2px(getContext(), 135), dip2px(getContext(), 3) + (int)value(f)[2], null);
	    }else if(l[2] >= yelwar && l[2] < redwar){
	    	canvas.drawBitmap(small(bitmap_chart_yellow), dip2px(getContext(), 135), dip2px(getContext(), 3) + (int)value(f)[2], null);
	    }else if(l[2] >= redwar){
	    	canvas.drawBitmap(small(bitmap_chart_red), dip2px(getContext(), 135), dip2px(getContext(), 3) + (int)value(f)[2], null);
	    }
	    
	    
	    if(l[3] < yelwar){
	    	canvas.drawBitmap(small(bitmap_chart_white), dip2px(getContext(), 67), dip2px(getContext(), 3) + (int)value(f)[3], null);
	    }else if(l[3] >= yelwar && l[3] < redwar){
	    	canvas.drawBitmap(small(bitmap_chart_yellow), dip2px(getContext(), 67), dip2px(getContext(), 3) + (int)value(f)[3], null);
	    }else if(l[3] >= redwar){
	    	canvas.drawBitmap(small(bitmap_chart_red), dip2px(getContext(), 67), dip2px(getContext(), 3) + (int)value(f)[3], null);
	    }
	    
	    
	    if(l[4] < yelwar){
	    	canvas.drawBitmap(small(bitmap_chart_white), dip2px(getContext(), -1), dip2px(getContext(), 3) + (int)value(f)[4], null);
	    }else if(l[4] >= yelwar && l[4] < redwar){
	    	canvas.drawBitmap(small(bitmap_chart_yellow), dip2px(getContext(), -1), dip2px(getContext(), 3) + (int)value(f)[4], null);
	    }else if(l[4] >= redwar){
	    	canvas.drawBitmap(small(bitmap_chart_red), dip2px(getContext(), -1), dip2px(getContext(), 3) + (int)value(f)[4], null);
	    }
	    
	    if(l[4] < yelwar){
	    	canvas.drawText(datedata[0], dip2px(getContext(), 33), dip2px(getContext(), 14) + (int)value(f)[4], textpaint);
	    	canvas.drawText(sleeptime[0], dip2px(getContext(), 33), dip2px(getContext(), 27) + (int)value(f)[4], textpaint);
	    }else{
	    	canvas.drawText(datedata[0], dip2px(getContext(), 33), dip2px(getContext(), 14) + (int)value(f)[4], fftextpaint);
	    	canvas.drawText(sleeptime[0], dip2px(getContext(), 33), dip2px(getContext(), 27) + (int)value(f)[4], fftextpaint);
	    }
	    
	    if(l[3] < yelwar){
	    	canvas.drawText(datedata[1], dip2px(getContext(), 101), dip2px(getContext(), 14) + (int)value(f)[3], textpaint);
	    	canvas.drawText(sleeptime[1], dip2px(getContext(), 101), dip2px(getContext(), 27) + (int)value(f)[3], textpaint);
	    }else{
	    	canvas.drawText(datedata[1], dip2px(getContext(), 101), dip2px(getContext(), 14) + (int)value(f)[3], fftextpaint);
	    	canvas.drawText(sleeptime[1], dip2px(getContext(), 101), dip2px(getContext(), 27) + (int)value(f)[3], fftextpaint);
	    }
	    
	    if(l[2] < yelwar){
	    	canvas.drawText(datedata[2], dip2px(getContext(), 169), dip2px(getContext(), 14) + (int)value(f)[2], textpaint);
	    	canvas.drawText(sleeptime[2], dip2px(getContext(), 169), dip2px(getContext(), 27) + (int)value(f)[2], textpaint);
	    }else{
	    	canvas.drawText(datedata[2], dip2px(getContext(), 169), dip2px(getContext(), 14) + (int)value(f)[2], fftextpaint);
	    	canvas.drawText(sleeptime[2], dip2px(getContext(), 169), dip2px(getContext(), 27) + (int)value(f)[2], fftextpaint);
	    }
	    
	    if(l[1] < yelwar){
	    	canvas.drawText(datedata[3], dip2px(getContext(), 237), dip2px(getContext(), 14) + (int)value(f)[1], textpaint);
	    	canvas.drawText(sleeptime[3], dip2px(getContext(), 237), dip2px(getContext(), 27) + (int)value(f)[1], textpaint);
	    }else{
	    	canvas.drawText(datedata[3], dip2px(getContext(), 237), dip2px(getContext(), 14) + (int)value(f)[1], fftextpaint);
	    	canvas.drawText(sleeptime[3], dip2px(getContext(), 237), dip2px(getContext(), 27) + (int)value(f)[1], fftextpaint);
	    }
	    
	    canvas.drawText(datedata[4], dip2px(getContext(), 305), dip2px(getContext(), 14) + (int)value(f)[0], fftextpaint);
	    canvas.drawText(sleeptime[4], dip2px(getContext(), 305), dip2px(getContext(), 27) + (int)value(f)[0], fftextpaint);
	    
	}
	
	
	/**
	 * 重要参数，两点之间分为几段描画，数字愈大分段越多，描画的曲线就越精细.
	 */
	private static final int STEPS = 12;
	
	/**
	 * 画曲线.
	 * 
	 * @param canvas
	 */
	private void drawCurve(Canvas canvas, List<Integer> points_x, List<Integer> points_y, Paint p) {
		
		List<Cubic> calculate_x = calculate(points_x);
		List<Cubic> calculate_y = calculate(points_y);
		shadowpath.moveTo(calculate_x.get(0).eval(0), calculate_y.get(0).eval(0));

		for (int i = 0; i < calculate_x.size(); i++) {
			for (int j = 1; j <= STEPS; j++) {
				float u = j / (float) STEPS;
				shadowpath.lineTo(calculate_x.get(i).eval(u), calculate_y.get(i)
						.eval(u));
			}
		}
		canvas.drawPath(shadowpath, p);
	}

	/**
	 * 计算曲线.
	 * 
	 * @param x
	 * @return
	 */
	private List<Cubic> calculate(List<Integer> x) {
		int n = x.size() - 1;
		float[] gamma = new float[n + 1];
		float[] delta = new float[n + 1];
		float[] D = new float[n + 1];
		int i;
		/*
		 * We solve the equation [2 1 ] [D[0]] [3(x[1] - x[0]) ] |1 4 1 | |D[1]|
		 * |3(x[2] - x[0]) | | 1 4 1 | | . | = | . | | ..... | | . | | . | | 1 4
		 * 1| | . | |3(x[n] - x[n-2])| [ 1 2] [D[n]] [3(x[n] - x[n-1])]
		 * 
		 * by using row operations to convert the matrix to upper triangular and
		 * then back sustitution. The D[i] are the derivatives at the knots.
		 */

		gamma[0] = 1.0f / 2.0f;
		for (i = 1; i < n; i++) {
			gamma[i] = 1 / (4 - gamma[i - 1]);
		}
		gamma[n] = 1 / (2 - gamma[n - 1]);

		delta[0] = 3 * (x.get(1) - x.get(0)) * gamma[0];
		for (i = 1; i < n; i++) {
			delta[i] = (3 * (x.get(i + 1) - x.get(i - 1)) - delta[i - 1])
					* gamma[i];
		}
		delta[n] = (3 * (x.get(n) - x.get(n - 1)) - delta[n - 1]) * gamma[n];

		D[n] = delta[n];
		for (i = n - 1; i >= 0; i--) {
			D[i] = delta[i] - gamma[i] * D[i + 1];
		}

		/* now compute the coefficients of the cubics */
		List<Cubic> cubics = new LinkedList<Cubic>();
		for (i = 0; i < n; i++) {
			Cubic c = new Cubic(x.get(i), D[i], 3 * (x.get(i + 1) - x.get(i))
					- 2 * D[i] - D[i + 1], 2 * (x.get(i) - x.get(i + 1)) + D[i]
					+ D[i + 1]);
			cubics.add(c);
		}
		return cubics;
	}
	
	public class Cubic {

		  float a,b,c,d;         /* a + b*u + c*u^2 +d*u^3 */

		  public Cubic(float a, float b, float c, float d){
		    this.a = a;
		    this.b = b;
		    this.c = c;
		    this.d = d;
		  }

		  
		  /** evaluate cubic */
		  public float eval(float u) {
		    return (((d*u) + c)*u + b)*u + a;
		  }
	}
	
	private static Bitmap small(Bitmap bitmap) {
		Matrix matrix = new Matrix(); 
		matrix.postScale(0.5f,0.5f); //长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		return resizeBmp;
	}
	
	/** 
	* 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	*/  
	public static int dip2px(Context context, float dpValue) {  
	  final float scale = context.getResources().getDisplayMetrics().density;  
	  return (int) (dpValue * scale + 0.5f);  
	}  
	
	
	
}
