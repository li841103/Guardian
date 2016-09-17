package com.szhd.guardian;

import com.szhd.impl.OnSlideListener;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class WarningSlideView extends LinearLayout{

	
	private Context mContext;
    private Scroller mScroller;
    private OnSlideListener mOnSlideListener;
    private LinearLayout mViewContent;
    private boolean mIsMoveClick = false;
    private int mHolderWidth = 80;

    private int mLastX = 0;
    private int mLastY = 0;
    private static final int TAN = 2;
	
	
    public WarningSlideView(Context context) {
    	super(context);
    	initView();
    }
	
	
	public WarningSlideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}


	public void setContentView(View view) {
	       mViewContent.addView(view);
	}
	
	
	private void initView() {
		mContext = getContext();
		mScroller = new Scroller(mContext);
		//设置LinearLayout的朝向
        setOrientation(LinearLayout.HORIZONTAL);
        //获取warning_view_merge中的控件
        View.inflate(mContext, R.layout.warning_view_merge, this);
        mViewContent = (LinearLayout) findViewById(R.id.warning_view_content);
        //宽度转换为标准尺寸
        mHolderWidth = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHolderWidth, getResources()
                        .getDisplayMetrics()));
		
	}

	public void setOnSlideListener(OnSlideListener onSlideListener) {
	       mOnSlideListener = onSlideListener;
	}

	public void shrink() {
		   if (getScrollX() != 0) {
		       this.smoothScrollTo(0, 0);
		   }
		   mIsMoveClick = false;
	}

	public void onRequireTouchEvent(MotionEvent event) {
	        int x = (int) event.getX();
	        int y = (int) event.getY();
	        int scrollX = getScrollX();
	        //Log.d(TAG, "x=" + x + "  y=" + y);

	        switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN: {
	            if (!mScroller.isFinished()) {
	                mScroller.abortAnimation();
	            }
	            if (mOnSlideListener != null) {
	                mOnSlideListener.onSlide(this,
	                        OnSlideListener.SLIDE_STATUS_START_SCROLL);
	            }
	            break;
	        }
	        case MotionEvent.ACTION_MOVE: {
	            int deltaX = x - mLastX;
	            int deltaY = y - mLastY;
	            //屏蔽上下滑动
	            if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
	                break;
	            }

	            int newScrollX = scrollX - deltaX;
	            if (deltaX != 0) {
	            	if (newScrollX < 0 && newScrollX < -mHolderWidth) {
	                    newScrollX = -mHolderWidth;
	                } else if (newScrollX > mHolderWidth) {
	                    newScrollX = mHolderWidth;
	                }else{
	                }
	            	if(newScrollX > 0){
	            		this.scrollTo(newScrollX, 0);
	            	}
	            }
	            break;
	        }
	        case MotionEvent.ACTION_UP: {
	            int newScrollX = 0;
	            if (scrollX - mHolderWidth * 0.75 > 0) {
	                newScrollX = mHolderWidth;
	                mIsMoveClick = !mIsMoveClick;
	            }else if (-scrollX - mHolderWidth * 0.75 > 0) {
	                newScrollX = -mHolderWidth;
	                mIsMoveClick = !mIsMoveClick;
	            }else{
	            	mIsMoveClick = false;
	            }
	            this.smoothScrollTo(newScrollX, 0);
	            if (mOnSlideListener != null) {
	                mOnSlideListener.onSlide(this,
	                        newScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF
	                                : OnSlideListener.SLIDE_STATUS_ON);
	            }
	            break;
	        }
	        default:
	            break;
	        }

	        mLastX = x;
	        mLastY = y;
	    }

	    private void smoothScrollTo(int destX, int destY) {
	        // 缓慢滚动到指定位置
	        int scrollX = getScrollX();
	        int delta = destX - scrollX;
	        mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
	        invalidate();
	    }

	    @Override
	    public void computeScroll() {
	        if (mScroller.computeScrollOffset()) {
	            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
	            postInvalidate();
	        }
	    }
	    public void open() {
			if(getScrollX() < mHolderWidth){
				int newScrollX = mHolderWidth;
				this.smoothScrollTo(newScrollX, 0);
				if (mOnSlideListener != null) {
					mOnSlideListener.onSlide(this,
							newScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF
									: OnSlideListener.SLIDE_STATUS_ON);
				}
			}else{
				mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, Math.abs(mHolderWidth) * 3);
				invalidate();
			}
		}
		
		public boolean close() {
			if(getScrollX() != 0){
				mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, Math.abs(mHolderWidth) * 3);
				invalidate();
				return false;
			}
			return true;
		}
		
		public void reset() {
			mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 0);
			invalidate();
		}

		public boolean ismIsMoveClick() {
			return mIsMoveClick;
		}
	
	
}
