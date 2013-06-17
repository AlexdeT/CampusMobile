package fr.alexdet.android.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import fr.alexdet.android.R;
import fr.alexdet.android.model.data.Notification;
import fr.alexdet.android.view.QuickAction;

/**
 * This class extends {@link SherlockFragmentActivity}. It sets the
 * {@link ActionBar}. All the application's {@link Activity} will extends this
 * class to have the custom Action bar. The custom view contains a right an left
 * button, and a title that can be clickable. When clicked, a
 * {@link QuickAction} window is display containing all the past notifications
 * 
 * @author alexisdetalhouet
 * 
 */

public abstract class MyActionBarActivity extends SherlockFragmentActivity {

	/**
	 * Custom {@link ActionBar} navigation view
	 */
	protected View mActionBarCustomNav = null;

	/**
	 * {@link QuickAction} class allows to create and display a pop up window.
	 * Here it is use to display a pop up containing all the
	 * {@link Notification}
	 */
	protected QuickAction mQuickAction;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		// inflate the action bar view
		mActionBarCustomNav = getLayoutInflater().inflate(R.layout.action_bar,
				null);
		// Hide default left icon
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		// Set the custom navigation to the activity's action bar
		getSupportActionBar().setCustomView(mActionBarCustomNav);
		// Display the custom ActionBar
		getSupportActionBar().setDisplayShowCustomEnabled(true);
	}

	/**
	 * Custom the action bar. All the action bar's item are available in the
	 * {@link MyActionBarActivity}. (leftButton, rightButton, title)
	 */
	protected abstract void setActionBar();
}
