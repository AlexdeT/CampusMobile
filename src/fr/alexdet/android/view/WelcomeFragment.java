package fr.alexdet.android.view;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import fr.alexdet.android.R;


/**
 * Welcome view displayed after the connection
 * 
 * @author alexisdetalhouet
 * 
 */
public class WelcomeFragment extends SherlockFragment {

	/**
	 * Custom view defining the project
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.welcome, container, false);

		TextView credit = (TextView) v.findViewById(R.id.credit);
		credit.setText(Html.fromHtml(getString(R.string.credit)));

		TextView bienvenue = (TextView) v.findViewById(R.id.text_bienvenue);
		bienvenue.setText(Html.fromHtml(getString(R.string.text_bienvenue)));

		v.setTag("welcome");
		return v;
	}
}