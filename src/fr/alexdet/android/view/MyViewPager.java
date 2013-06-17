package fr.alexdet.android.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Custom ViewPager in order to set the swipe event
 * 
 * @author alexisdetalhouet
 * 
 */
public class MyViewPager extends ViewPager {

	/*
	 * Boolean storing if the swipe event is enable or not
	 */
	private boolean enabled;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            the application context
	 * @param attrs
	 * 
	 */
	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = false;
	}

	/**
	 * Active if the swipe is enable
	 * 
	 * @see android.support.v4.view.ViewPager#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onTouchEvent(event);
		}
		return false;
	}

	/**
	 * Active if the swipe is enable
	 * 
	 * @see android.support.v4.view.ViewPager#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onInterceptTouchEvent(event);
		}
		return false;
	}

	/**
	 * Set the swipe gesture - if true, the ViewPager will allow the swipe
	 * Movement
	 * 
	 * @param enabled
	 */
	public void setSwipeEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}