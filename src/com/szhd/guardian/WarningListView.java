package com.szhd.guardian;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ListView;

import com.szhd.impl.OnSwipeListener;
import com.szhd.ui.WarningFragment.MessageItem;

public class WarningListView extends ListView {

	private static final int TOUCH_STATE_NONE = 0;
	private static final int TOUCH_STATE_X = 1;
	private static final int TOUCH_STATE_Y = 2;

	private int MAX_Y = 5;
	private int MAX_X = 3;
	private int position;
	private int tempposition;
	private float mDownX;
	private float mDownY;
	private int mTouchState;
	private int mTouchPosition;
	private OnSwipeListener mOnSwipeListener;

	private Interpolator mCloseInterpolator;
	private Interpolator mOpenInterpolator;

	private WarningSlideView mFocusedItemView;

	public WarningListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public WarningListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public WarningListView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		MAX_X = dp2px(MAX_X);
		MAX_Y = dp2px(MAX_Y);
		mTouchState = TOUCH_STATE_NONE;
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics());
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		// 根据点击的坐标，获得listview的位置下标
		position = pointToPosition(x, y);
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			tempposition = position;
			Log.d("tempposition", tempposition + "");
			if (position != INVALID_POSITION) {
				MessageItem data = (MessageItem) getItemAtPosition(position);
				mFocusedItemView = data.wsv;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			// 取绝对值
			float dy = Math.abs((ev.getY() - mDownY));
			float dx = Math.abs((ev.getX() - mDownX));
			if (mTouchState == TOUCH_STATE_X) {
				getSelector().setState(new int[] { 0 });
				ev.setAction(MotionEvent.ACTION_CANCEL);
				super.onTouchEvent(ev);
				return true;
			} else if (mTouchState == TOUCH_STATE_NONE) {
				if (Math.abs(dy) > MAX_Y) {
					mTouchState = TOUCH_STATE_Y;
				} else if (dx > MAX_X) {
					mTouchState = TOUCH_STATE_X;
					if (mOnSwipeListener != null) {
						mOnSwipeListener.onSwipeStart(mTouchPosition);
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mTouchState == TOUCH_STATE_X) {
				if (mOnSwipeListener != null) {
					mOnSwipeListener.onSwipeEnd(mTouchPosition);
				}
				ev.setAction(MotionEvent.ACTION_CANCEL);
				super.onTouchEvent(ev);
				return true;
			}
			break;
		}

		if (mFocusedItemView != null) {
			if (position != tempposition || position == INVALID_POSITION) {
				mFocusedItemView.shrink();
				return super.onTouchEvent(ev);
			}
			mFocusedItemView.onRequireTouchEvent(ev);
		}

		return super.onTouchEvent(ev);
	}

	public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
		this.mOnSwipeListener = onSwipeListener;
	}

	public void shrinkListItem(int position) {
		View item = getChildAt(position);
		if (item != null) {
			try {
				((WarningSlideView) item).shrink();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
	}

	public int getPosition() {
		return position;
	}

	public void setCloseInterpolator(Interpolator interpolator) {
		mCloseInterpolator = interpolator;
	}

	public void setOpenInterpolator(Interpolator interpolator) {
		mOpenInterpolator = interpolator;
	}

	public Interpolator getOpenInterpolator() {
		return mOpenInterpolator;
	}

	public Interpolator getCloseInterpolator() {
		return mCloseInterpolator;
	}

}
