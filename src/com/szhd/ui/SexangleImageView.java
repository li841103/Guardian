package com.szhd.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SexangleImageView extends View {

	private int mWidth;
	private int mHeight;
	//private int centreX;
	//private int centreY;
	private int mLenght;
	private Paint paint;
	private Path path;

	public SexangleImageView(Context context) {
		super(context);
	}

	public SexangleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		mWidth = getWidth();
		mHeight = getHeight();
		//centreX = mWidth / 2;			
		//centreY = mHeight / 2;
		mLenght = mWidth / 2;

		double radian30 = 30 * Math.PI / 180;
		//计算点坐标
		float a = (float) (mLenght * Math.sin(radian30));
		float b = (float) (mLenght * Math.cos(radian30));
		float c = (mHeight - 2 * b) / 2;

		if (null == paint) {
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStyle(Style.STROKE);//FILL
			paint.setColor(Color.parseColor("#ffffff"));
			paint.setAlpha(0);
		}
		path = new Path();

		//将画笔移动至该点
		path.moveTo(mHeight / 2, mWidth);
		path.lineTo(mHeight - c, mWidth - a);
		path.lineTo(mHeight - c, mWidth - a - mLenght);
		path.lineTo(mHeight / 2, 0);
		path.lineTo(c, a);
		path.lineTo(c, mWidth - a);
		
		path.close();

		//在这个区域画图
		canvas.drawPath(path, paint);
		
		/*
		InputStream is = getResources().openRawResource(R.drawable.sec_10);   
        Bitmap mBitmap = BitmapFactory.decodeStream(is);   
        Paint mPaint = new Paint();   
        canvas.drawBitmap(mBitmap, centreX, centreY, mPaint);  
		*/

	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			float edgeLength = ((float) getWidth()) / 2;
//			float radiusSquare = edgeLength * edgeLength * 3 / 4;
//			float dist = (event.getX() - getWidth() / 2)
//					* (event.getX() - getWidth() / 2)
//					+ (event.getY() - getHeight() / 2)
//					* (event.getY() - getHeight() / 2);
//			
//			if (dist <= radiusSquare) {
//				paint.setColor(Color.parseColor("#A8A8A8"));
//				paint.setStyle(Style.FILL);
//				paint.setAlpha(100);
//				invalidate();
//			}
//			break;
//		case MotionEvent.ACTION_UP:
//			paint.setColor(Color.parseColor("#A2A2A2"));		
//			paint.setStyle(Style.STROKE);
//			paint.setAlpha(200);
//			invalidate();
//			CharSequence flagIcons = this.getContentDescription();//Flag_image
//			if(flagIcons==null){
//				
//			}else{
//				   Message msg1=new Message(); 
//		           msg1.what = Integer.parseInt(flagIcons.toString());
//		           //MainActivity.myHandler.sendMessage(msg1);
//			}
//			break;
//		}
//		return true;
//	}

}
