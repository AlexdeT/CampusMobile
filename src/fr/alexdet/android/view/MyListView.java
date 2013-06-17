package fr.alexdet.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


/**
 * Custom ListView displaying the menu content
 * 
 * @author alexisdetalhouet
 * 
 */
public class MyListView extends ListView {

	private OnScrollChangedListener mOnScrollChangedListener;
	private static final int ID = 0;

	public interface OnScrollChangedListener {

		void onScrollChanged();
	}

	public MyListView(Context context) {
		super(context);
		setId(ID);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		if (mOnScrollChangedListener != null)
			mOnScrollChangedListener.onScrollChanged();
	}

	public void setOnScrollChangedListener(OnScrollChangedListener listener) {
		mOnScrollChangedListener = listener;
	}
}
