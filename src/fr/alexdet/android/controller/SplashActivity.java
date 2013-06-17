package fr.alexdet.android.controller;

import java.util.List;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import fr.alexdet.android.R;
import fr.alexdet.android.controller.thread.SplashHandlerTask;
import fr.alexdet.android.model.bdd.UserRepository;
import fr.alexdet.android.model.data.User;

/**
 * This class is the LUNCHER of the application. It checks if the internal
 * mobile {@link SQLiteDatabase} 'myeceparis' exists. If yes, it checks if a
 * user is present. If yes, set the {@link SharedApp} login and lunch the
 * {@link MainActivity}, if no the class starts the {@link LoginActivity}
 * 
 * @author alexisdetalhouet
 * 
 */

public class SplashActivity extends MyActionBarActivity {

	/**
	 * String use to debug
	 */
	private static final String TAG = "SplashScreenActivity";

	/**
	 * The class activity
	 */
	private static Activity mActivity;

	/**
	 * The handler task. Define if the {@link LoginActivity} or the
	 * {@link MainActivity} should be started
	 */
	private SplashHandlerTask mCheckTask = null;

	/**
	 * Flag indicating the LoginActivity
	 */
	public final static int LOGINACTIVITY = 0;

	/**
	 * Falg indicating the MainActivity
	 */
	public final static int MAINACTIVITY = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
		// set the activity
		mActivity = SplashActivity.this;
		// set the view
		setContentView(R.layout.splash_screen);

		if (mCheckTask != null) {
			return;
		}
		// lunch the asyncTask
		mCheckTask = new SplashHandlerTask(this);
		mCheckTask.execute((Void) null);

		setActionBar();
	}

	@Override
	public void setActionBar() {
		// hide left button
		mActionBarCustomNav.findViewById(R.id.leftButton).setVisibility(
				View.INVISIBLE);
		// hide right button
		mActionBarCustomNav.findViewById(R.id.rightButton).setVisibility(
				View.INVISIBLE);
		// set the title clickable state to false
		mActionBarCustomNav.findViewById(R.id.title).setClickable(false);
	}

	/**
	 * Delete the notification that wakes the application
	 * 
	 * @param id
	 *            the notification id that has been clicked
	 */
	public void deleteNotificationFromNotificationCenter(int id) {
		Log.v(TAG, "delete Notification " + id + " from Notification Center");
		// create the notification
		final NotificationManager notificationManager = (NotificationManager) mActivity
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// the notification can be deleted and retrieved thanks by its ID
		notificationManager.cancel(id);
	}

	/**
	 * Check if the user is actually present in the {@link SQLiteDatabase}
	 */
	public boolean checkUser() {
		// get the device's database list
		String[] dblist = mActivity.databaseList();
		for (String db : dblist) {
			// if ours is present, get the user
			if (db.equals("myeceparis.db")) {
				UserRepository userRepo = new UserRepository(
						mActivity.getApplicationContext());
				userRepo.open();
				List<User> userList = userRepo.GetAll();
				if (userList != null) {
					for (User u : userList) {
						SharedApp.setLogin(u.getLogin());
					}
					Log.v(TAG, "db present, user is: " + SharedApp.getLogin());
					userRepo.close();
					return true; // consume the activity and start the
									// MainActivity
				}
				userRepo.close();
			}
		}
		return false;
	}
}
