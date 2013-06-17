package fr.alexdet.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.alexdet.android.R;


/**
 * Custom ArrayAdapter displaying the notification in the pop up window
 * 
 * @author alexisdetalhouet
 * 
 */
public class MyArrayAdapter extends ArrayAdapter<String> {

	/*
	 * Inflater of the activity
	 */
	private LayoutInflater mInflater;
	/*
	 * Notifications names list
	 */
	private String[] mNotificationsNames;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            the application context
	 * @param textViewResourceId
	 *            the textView resId
	 * @param objects
	 *            the notification names
	 * @param inflater
	 *            the activity inflater
	 */
	public MyArrayAdapter(Context context, int textViewResourceId,
			String[] objects, LayoutInflater inflater) {
		super(context, textViewResourceId, objects);
		this.mInflater = inflater;
		this.mNotificationsNames = objects;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	/**
	 * Set the view
	 * 
	 * @param position
	 *            position in the Array of the clicked item
	 * @param convertView
	 *            the item view
	 * @param parent
	 *            the ViewGroup of the ArrayAdapter
	 * @return the view
	 */
	public View getCustomView(int position, View convertView, ViewGroup parent) {
		View row = mInflater.inflate(R.layout.list_row_notification, parent,
				false);

		TextView label = (TextView) row.findViewById(R.id.title);
		label.setText(mNotificationsNames[position]);

		return row;
	}
}
