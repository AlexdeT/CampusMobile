package fr.alexdet.android.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Custom BaseAdapter
 * 
 * 
 * @author alexisdetalhouet
 * 
 */
public abstract class MyBaseAdapter extends BaseAdapter {

	/**
	 * Contain the item data
	 * 
	 * @author alexisdetalhouet
	 * 
	 */
	public static class Item {

		// Item title
		public String mTitle;
		// Item icon resIs
		public int mIconRes;
		// Item position
		public boolean mIsLeft = true;

		public int mNbNotif = -1;

		public Item(String title, int iconRes) {
			mTitle = title;
			mIconRes = iconRes;
		}

		public Item(String title, int iconRes, int nbNotif) {
			mTitle = title;
			mIconRes = iconRes;
			mNbNotif = nbNotif;
		}

		public Item(String title, int iconRes, boolean isLeft) {
			mTitle = title;
			mIconRes = iconRes;
			mIsLeft = isLeft;
		}

		public Item(String title, int iconRes, boolean isLeft, int nbNotif) {
			mTitle = title;
			mIconRes = iconRes;
			mIsLeft = isLeft;
			mNbNotif = nbNotif;
		}

	}

	/**
	 * Contain the category data
	 * 
	 * @author alexisdetalhouet
	 * 
	 */
	public static final class Category {

		// Category title
		public String mTitle;

		public Category(String title) {
			mTitle = title;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
