package fr.alexdet.android.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gcm.GCMRegistrar;

import fr.alexdet.android.R;

/**
 * This {@link Fragment} display the logout form
 * 
 * @author alexisdetalhouet
 * 
 */
public class LogoutViewController extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflate the logout view
		View v = inflater.inflate(R.layout.logout, container, false);

		// set the yes button's listener
		v.findViewById(R.id.buttonYes).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// erase the database
						getSherlockActivity().deleteDatabase("myeceparis.db");
						// unregister the user from the google cloud messaging
						// service
						GCMRegistrar.unregister(getSherlockActivity());

						// intent with the targeted activity
						Intent intent = new Intent(getSherlockActivity(),
								SplashActivity.class);
						// start the new activity
						getSherlockActivity().startActivity(intent);
						// finish the current activity
						getSherlockActivity().finish();

						// Set the animation between activity; here it is a fade
						// in fade out animation
						getSherlockActivity().overridePendingTransition(
								R.anim.mainfadein, R.anim.splashfadeout);
					}
				});

		// set the no butto's listener
		v.findViewById(R.id.buttonNo).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// open the left menu
				((MainActivity) getSherlockActivity()).getLeftDrawer()
						.openMenu();
			}
		});

		// return the view
		return v;
	}
}
