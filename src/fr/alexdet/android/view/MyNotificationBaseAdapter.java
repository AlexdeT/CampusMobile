package fr.alexdet.android.view;

import java.util.List;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.alexdet.android.R;


/**
 * BaseAdapter for the notification window
 * 
 * @author alexisdetalhouet
 * 
 */
public class MyNotificationBaseAdapter extends MyBaseAdapter {

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
	public MyNotificationBaseAdapter(List<Object> items, LayoutInflater inflater) {
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
				v = mInflater.inflate(R.layout.menu_row_category,
						parent, false);
			}

			TextView title = (TextView) v.findViewById(R.id.titleNotification);
			title.setText(((Category) item).mTitle);
			title.setTypeface(Typeface.createFromAsset(parent.getContext()
					.getAssets(), "fonts/Champagne&LimousinesBold.ttf"));

		} else /* Item */{
			if (v == null) {
				v = mInflater.inflate(R.layout.list_row_notification, parent,
						false);
			}
			TextView title = (TextView) v.findViewById(R.id.title);
			title.setText(((Item) item).mTitle);
			title.setTypeface(Typeface.createFromAsset(parent.getContext()
					.getAssets(), "fonts/Champagne&LimousinesBold.ttf"));
			title.setCompoundDrawablesWithIntrinsicBounds(
					((Item) item).mIconRes, 0, 0, 0);
			title.setTextSize(21);

			// tag to notify the item that there are notifications
			v.setTag("notification_" + ((Item) item).mTitle);
			// tag to set the item arrow on the slide menu item when it's select
			v.setTag(R.id.mdActiveViewPosition, position);
		}
		return v;
	}
}
