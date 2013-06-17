package fr.alexdet.android.view;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.alexdet.android.R;

/**
 * Custom MyBaseAdapter in order to display the menu item
 * 
 * @author alexisdetalhouet
 * 
 */
public class MyMenuBaseAdapter extends MyBaseAdapter {

	/*
	 * List of the menu items
	 */
	private List<Object> mItems;
	/*
	 * Activity LayoutInflater
	 */
	private LayoutInflater mInflater;

	/**
	 * Constructor
	 * 
	 * @param items
	 *            list of menu items
	 * @param inflater
	 *            the activity LayoutInflater
	 */
	public MyMenuBaseAdapter(List<Object> items, LayoutInflater inflater) {
		mItems = items;
		mInflater = inflater;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position) instanceof Item ? 0 : 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public boolean isEnabled(int position) {
		return getItem(position) instanceof Item;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	/**
	 * Custom the view of each item Adding an icon on the left of the item
	 * 
	 * Define if the item is a category item or not
	 * 
	 * @see fr.alexdet.android.view.MyBaseAdapter#getView(int,
	 *      android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Object item = getItem(position);

		if (item instanceof Category) {
			if (v == null) {
				v = mInflater
						.inflate(R.layout.menu_row_category, parent, false);
			}
			TextView title = (TextView) v.findViewById(R.id.titleNotification);
			title.setText(((Category) item).mTitle);

		} else /* Item */{
			if (v == null) {
				if (((Item) item).mIsLeft) {
					v = mInflater.inflate(R.layout.menu_row_item_left, parent,
							false);
					TextView title = (TextView) v
							.findViewById(R.id.menu_row_title);
					title.setText(((Item) item).mTitle);
					title.setCompoundDrawablesWithIntrinsicBounds(
							((Item) item).mIconRes, 0, 0, 0);
				} else {
					v = mInflater.inflate(R.layout.menu_row_item_right, parent,
							false);
					TextView title = (TextView) v
							.findViewById(R.id.menu_row_title);
					title.setText(((Item) item).mTitle);
					title.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							((Item) item).mIconRes, 0);

					if (((int) ((Item) item).mNbNotif) > 0) {
						TextView tv = (TextView) v
								.findViewById(R.id.menu_row_notification);
						tv.setText("" + ((Item) item).mNbNotif);
					}

				}
			}

			// TextView title = (TextView) v.findViewById(R.id.menu_row_title);
			// title.setText(((Item) item).mTitle);
			// title.setCompoundDrawablesWithIntrinsicBounds(
			// ((Item) item).mIconRes, 0, 0, 0);

			// tag to retrieve the item
			v.setTag(((Item) item).mTitle);
			// Log.i("MyMenuBaseAdapter_TAG", ((Item) item).mTitle);
			// tag to set the item arrow on the slide menu item when it's select
			v.setTag(R.id.mdActiveViewPosition, position);
		}
		return v;
	}
}
