package fr.alexdet.android.controller;

import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gcm.GCMRegistrar;

import fr.alexdet.android.R;
import fr.alexdet.android.controller.slidemenu.LeftSlideMenu;
import fr.alexdet.android.controller.slidemenu.RightSlideMenu;
import fr.alexdet.android.controller.slidemenu.SlideMenu;
import fr.alexdet.android.controller.thread.LoadDataTask;
import fr.alexdet.android.model.data.Course;
import fr.alexdet.android.model.data.Notification;
import fr.alexdet.android.model.webservices.AttendanceQuery;
import fr.alexdet.android.model.webservices.CoursesQuery;
import fr.alexdet.android.model.webservices.GradesQuery;
import fr.alexdet.android.model.webservices.NotificationQuery;
import fr.alexdet.android.view.ActionItem;
import fr.alexdet.android.view.AttendanceFragment;
import fr.alexdet.android.view.GradesFragment;
import fr.alexdet.android.view.MyDialog;
import fr.alexdet.android.view.MyViewPager;
import fr.alexdet.android.view.QuickAction;

/**
 * 
 * This class creates and manages the {@link LeftSlideMenu} and the
 * {@link RightSlideMenu} which respectively correspond to the main menu, and
 * the courses menu. It instantiates all the views thanks to
 * {@link FragmentActivity} and display them with {@link MyViewPager}. </br>
 * This class extends the {@link MyActionBarActivity} in order keep the custom
 * action bar style through all the application. The action bar also contain a
 * {@link QuickAction} object which allow the user to see his notifications in
 * an overlay window. </br> This class register the application over the google
 * cloud messaging service in order to allow push notification. Its manages an
 * the {@link LoadDataTask} that retrieves the data, either from the
 * {@link SQLiteDatabase} or from the MyECEParis server client if it is the
 * first time the user uses the application
 * 
 * @author alexisdetalhouet
 * 
 */
public class MainActivity extends MyActionBarActivity {

	/**
	 * String set for debug
	 */
	private static final String TAG = "MainSlideActivity";

	/**
	 * BroadcastReceiver used to notify fragments when data are loaded
	 */
	public static MyBroadcastReceiver mBroadcastReceiver = new MyBroadcastReceiver();

	/**
	 * IntentFilter storing the fragment's action (only use for the
	 * {@link AttendanceFragment} and the {@link GradesFragment} in order to set
	 * {@link MyBroadcastReceiver}
	 */
	public static IntentFilter mIntentFilter = new IntentFilter();

	/**
	 * State of the synchronization
	 */
	public static boolean isSyncActive = true;

	/**
	 * Course menu
	 */
	private RightSlideMenu mRightMenu;

	/**
	 * State of the course menu
	 */
	private boolean isRightMenuOpen = false;

	/**
	 * Main menu
	 */
	private LeftSlideMenu mLeftMenu;

	@Override
	protected void onCreate(Bundle inState) {
		super.onCreate(inState);
		Log.v(TAG, "onCreate");

		// Set the slide menu item clicked position in order to show the row
		// indicator on the right/left of the menu
		if (inState != null) {
			mRightMenu.mActivePosition = inState
					.getInt(SlideMenu.STATE_ACTIVE_POSITION_RIGHT);
			mLeftMenu.mActivePosition = inState
					.getInt(SlideMenu.STATE_ACTIVE_POSITION_LEFT);
		}

		// create the main menu
		mLeftMenu = new LeftSlideMenu(this);

		setActionBar();
		registerPush();
		launchDataTask();

	}

	@Override
	protected void onPause() {
		super.onPause();
		// If a fragment register to this receiver, it unregisters the activity
		if (LeftSlideMenu.isBroadcastRegistered) {
			LeftSlideMenu.isBroadcastRegistered = false;
			unregisterReceiver(mBroadcastReceiver);
			Log.v(TAG, "onPause, unregister receiver");
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle inState) {
		super.onRestoreInstanceState(inState);
		// Restore the course menu state as it was at the last use
		mRightMenu.mMenuDrawer.restoreState(inState
				.getParcelable(SlideMenu.STATE_MENUDRAWER_RIGHT));
		// Restore the main menu state as it was at the last use
		mLeftMenu.mMenuDrawer.restoreState(inState
				.getParcelable(SlideMenu.STATE_MENUDRAWER_LEFT));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save the actual state of the main MenuDrawer and its active position
		outState.putParcelable(SlideMenu.STATE_MENUDRAWER_LEFT,
				mLeftMenu.mMenuDrawer.saveState());
		outState.putInt(SlideMenu.STATE_ACTIVE_POSITION_LEFT,
				mLeftMenu.mActivePosition);
		// Save the actual state of the course MenuDrawer and its active
		// position
		outState.putParcelable(SlideMenu.STATE_MENUDRAWER_RIGHT,
				mRightMenu.mMenuDrawer.saveState());
		outState.putInt(SlideMenu.STATE_ACTIVE_POSITION_RIGHT,
				mRightMenu.mActivePosition);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				// If the left slide menu is open and the user press the back
				// key, a dialog pop up window ask for confirmation to quit the
				// app
				if (MenuDrawer.STATE_OPEN == mLeftMenu.mMenuDrawer
						.getDrawerState()) {
					// create the dialog
					final MyDialog d = new MyDialog(this,
							getString(R.string.app_name),
							getString(R.string.confirmationExit));
					// set the exit button listener
					d.getExit().setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// finish the activity
							finish();
							// set the animation
							overridePendingTransition(R.anim.mainfadein,
									R.anim.splashfadeout);
							// dismiss the dialog
							d.dismiss();
						}
					});
					// set the dismiss button's text
					d.getDismiss().setText(getString(R.string.no));

					d.show();
				}
				// if the right menu exist, and it's open, we close the right
				// menu
				if (mRightMenu != null) {
					if (MenuDrawer.STATE_OPEN == mRightMenu.mMenuDrawer
							.getDrawerState())
						mRightMenu.mMenuDrawer.closeMenu();

					// if the left slide menu is close it opens it
					else
						mLeftMenu.mMenuDrawer.openMenu();
				}
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if result is OK, the activity finished without issue so we open the
		// left slide menu
		if (resultCode == RESULT_OK) {
			mLeftMenu.mMenuDrawer.openMenu();
		}
	}

	@Override
	public void setActionBar() {
		// Left button
		mActionBarCustomNav.findViewById(R.id.leftButton).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isRightMenuOpen)
							mRightMenu.hideMenu();
						// open or close the left menu
						mLeftMenu.toggleMenu();
					}
				});
		// Right button
		mActionBarCustomNav.findViewById(R.id.rightButton).setVisibility(
				View.INVISIBLE);
		mActionBarCustomNav.findViewById(R.id.rightButton).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// open the right menu
						mRightMenu.openMenu();
					}
				});
	}

	/**
	 * Check if the user is already register in the Google Cloud Messaging
	 * Service, if no, the method registers the user and a unique
	 * "Registration ID" is given to his phone
	 */
	private void registerPush() {

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			GCMRegistrar.register(this, this.getString(R.string.SENDER_ID));
		} else {
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// nothing
			}
		}

	}

	/**
	 * Lunch the {@link LoadDataTask} executing the data backup. </br> If it is
	 * the first time the user is connecting to the application, all his data
	 * will be download. </br> If the activity started with a notification
	 * bundle, it means the parent activity has been notified a notification was
	 * pressed. A bundle contained in the activity gathered all the notification
	 * information : tableName, rowID in order to retrieve the data. </br>
	 * Delete the current notification/
	 * 
	 */
	private void launchDataTask() {
		Boolean isFirstConnection = getIntent().getBooleanExtra("isFirstTime",
				true);

		Bundle b = null;
		b = getIntent().getBundleExtra("notifID");
		// if a notification bundle has been set, get the notification type and
		// display the corresponding view
		if (b != null) {
			if (b.getString("tableName").equals("ABSENCES")) {
				mLeftMenu.mViewPager.setCurrentItem(4);
			}

			if (b.getString("tableName").equals("NOTES")) {
				mLeftMenu.mViewPager.setCurrentItem(3);
			}

			isFirstConnection = b.getBoolean("isFirstTime");

			Log.v(TAG, "lunchDataTask, notification bundle: " + b);
		}

		Log.v(TAG, "lunchDataTask, isFirstConnection: " + isFirstConnection);

		// create the task and execute it
		new LoadDataTask(this, isFirstConnection, b).execute((Void) null);
		// show the progress bar stating the sync evolution
		mActionBarCustomNav.findViewById(R.id.progressBarLoad).setVisibility(
				View.VISIBLE);
		// set the sync state as active
		isSyncActive = true;
	}

	public void setActionBarPopUpNotification(final List<Course> listeCours) {
		// Pop up window - display notification historic
		mQuickAction = new QuickAction(this);

		// Action bar title, allows the clickable state and create the handler
		mActionBarCustomNav.findViewById(R.id.title).setClickable(true);
		mActionBarCustomNav.findViewById(R.id.title).setBackgroundResource(
				R.drawable.spinner_ab_default_myeceparis);
		mActionBarCustomNav.findViewById(R.id.title).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mQuickAction.show(v);
					}
				});

		// for all notifications, create an item to be contained in the
		// QuickAction view
		for (Notification n : NotificationQuery.getListNotif()) {
			int drawableIconId = -1;

			if (n.getType().equals(GradesQuery.NOTIF_TAG))
				drawableIconId = R.drawable.menu_notes;
			else if (n.getType().equals(AttendanceQuery.NOTIF_TAG))
				drawableIconId = R.drawable.menu_absence;
			else if (n.getType().equals(CoursesQuery.NOTIF_TAG))
				drawableIconId = R.drawable.menu_cours;
			else
				drawableIconId = -1;

			if (drawableIconId != -1) {
				ActionItem item = new ActionItem(drawableIconId, n.toString());
				mQuickAction.addActionItem(item);
			}
		}

		// set the action item click listener
		// open the view corresponding to the notification
		mQuickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {

						if (!isRightMenuOpen
								|| mLeftMenu.mMenuDrawer.getDrawerState() == MenuDrawer.STATE_OPEN)
							mLeftMenu.mMenuDrawer.closeMenu();

						// note
						if (source.getActionItem(pos).getIconId() == R.drawable.menu_notes) {
							if (isRightMenuOpen)
								mRightMenu.closeMenu();
							mLeftMenu.mViewPager.setCurrentItem(3);
							mLeftMenu.mActivePosition = 4;
							mLeftMenu.mMenuDrawer.setActiveView(
									mLeftMenu.mList.getChildAt(4),
									mLeftMenu.mActivePosition);
						}

						// absence
						if (source.getActionItem(pos).getIconId() == R.drawable.menu_absence) {
							if (isRightMenuOpen)
								mRightMenu.closeMenu();
							mLeftMenu.mViewPager.setCurrentItem(4);
							mLeftMenu.mActivePosition = 5;
							mLeftMenu.mMenuDrawer.setActiveView(
									mLeftMenu.mList.getChildAt(5),
									mLeftMenu.mActivePosition);
						}

						// cours
						if (source.getActionItem(pos).getIconId() == R.drawable.menu_cours) {
							mLeftMenu.mActivePosition = 6;
							mLeftMenu.mMenuDrawer.setActiveView(
									mLeftMenu.mList.getChildAt(6),
									mLeftMenu.mActivePosition);

							for (int i = 0; i < listeCours.size(); i++) {
								if (source.getActionItem(pos).getTitle() == listeCours
										.get(i).getShortname()) {

									Log.v(TAG, "salut");
									mRightMenu.mViewPager.setCurrentItem(i);
									mRightMenu.mActivePosition = i + 1;
									mRightMenu.mMenuDrawer.setActiveView(
											mRightMenu.mList.getChildAt(i + 1),
											mRightMenu.mActivePosition);
									break;
								}
							}

							mRightMenu.openMenu();

						}

						mQuickAction.dismiss();
					}
				});

		mQuickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
			@Override
			public void onDismiss() {
			}
		});

	}

	/**
	 * This method is called on the postExecute method of the
	 * {@link LoadDataTask}. Once all the data are retrieved, we set the
	 * {@link RightSlideMenu}, the notification {@link QuickAction} window and
	 * we hide the action bar's progress bar
	 */
	public void syncEnded() {
		Log.v(TAG, "syncEnded");
		isSyncActive = false;

		List<Course> listeCours = CoursesQuery.getListCourses();

		setActionBarPopUpNotification(listeCours);
		mActionBarCustomNav.findViewById(R.id.progressBarLoad).setVisibility(
				View.INVISIBLE);

		mRightMenu = new RightSlideMenu(this, listeCours);

		mLeftMenu.syncEnded();
	}

	/**
	 * Get the course menu state
	 * 
	 * @return true if the menu is open, else false
	 */
	public boolean isRightMenuOpen() {
		return isRightMenuOpen;
	}

	/**
	 * Set the course menu state
	 * 
	 * @param state
	 *            state of the right menu
	 */
	public void setIsRightMenuOpen(boolean state) {
		isRightMenuOpen = state;
	}

	/**
	 * Get the sync state
	 * 
	 * @return the sync state
	 */
	public boolean isSyncActive() {
		return isSyncActive;
	}

	/**
	 * Get the main {@link MenuDrawer}
	 * 
	 * @return the left {@link MenuDrawer}
	 */
	public MenuDrawer getLeftDrawer() {
		return mLeftMenu.getDrawer();
	}

	/**
	 * Get the course {@link MenuDrawer}
	 * 
	 * @return the right {@link MenuDrawer}
	 */
	public MenuDrawer getRightDrawer() {
		return mRightMenu.getDrawer();
	}

	/**
	 * Get the {@link RightSlideMenu}
	 * 
	 * @return the {@link RightSlideMenu}
	 */
	public RightSlideMenu getRightMenu() {
		return mRightMenu;
	}
}
