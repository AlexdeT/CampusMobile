package fr.alexdet.android.controller.slidemenu;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.OnDrawerStateChangeListener;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import fr.alexdet.android.R;
import fr.alexdet.android.controller.LogoutViewController;
import fr.alexdet.android.controller.MainActivity;
import fr.alexdet.android.controller.MapActivity;
import fr.alexdet.android.controller.SharedApp;
import fr.alexdet.android.controller.key.LeftMenuKey;
import fr.alexdet.android.view.AttendanceFragment;
import fr.alexdet.android.view.EcePasswordChangerFragment;
import fr.alexdet.android.view.GradesFragment;
import fr.alexdet.android.view.InfoFragment;
import fr.alexdet.android.view.MyBaseAdapter.Category;
import fr.alexdet.android.view.MyBaseAdapter.Item;
import fr.alexdet.android.view.MyFragmentPagerAdapter;
import fr.alexdet.android.view.MyListView;
import fr.alexdet.android.view.MyMenuBaseAdapter;
import fr.alexdet.android.view.MyViewPager;
import fr.alexdet.android.view.QFQFragment;
import fr.alexdet.android.view.WelcomeFragment;
import fr.alexdet.android.view.WhiteFragment;

public class LeftSlideMenu extends SlideMenu {

	private MainActivity mActivity;
	private final int NUMBER_OF_MENU_ITEM = 9;

	private WhiteFragment whiteFragment;

	public static boolean isBroadcastRegistered = false;
	public static boolean isWhiteFragmentCreated = false;

	// private static final String TAG = "LeftSlideMenu";

	public LeftSlideMenu(MainActivity activity) {
		mActivity = activity;
//		mActivity.setTheme(R.style.Theme_MyEceParis_LeftDrawer);

		whiteFragment = (WhiteFragment) Fragment.instantiate(mActivity,
				WhiteFragment.class.getName());

		this.configureMenuDrawer();
		this.createMenuContent();
		this.createMenuFragment();
	}

	@Override
	public void configureMenuDrawer() {
		// set drag type, with MenuDrawer.MENU_DRAG_CONTENT
		// you can drag the menu from everywhere in the screen
		// and set the position of the menu
		mMenuDrawer = MenuDrawer
				.attach(mActivity, MenuDrawer.MENU_DRAG_CONTENT);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		mMenuDrawer.setContentView(R.layout.pager);

		// set a listener on the state of the menu
		mMenuDrawer
				.setOnDrawerStateChangeListener(new OnDrawerStateChangeListener() {
					@Override
					public void onDrawerStateChange(int oldState, int newState) {
						// if the user has clicked on the map item, lunch
						// the
						// map activity
						if (oldState == MenuDrawer.STATE_OPEN
								&& mActivePosition == 9)
							mActivity.startActivityForResult(new Intent(
									mActivity.getApplicationContext(),
									MapActivity.class), 0);

					}
				});

		// animated the menu drawer when the app is lunch
		mMenuDrawer.peekDrawer();

	}

	@Override
	public void createMenuContent() {
		// Get the user name from the saved login
		String login = SharedApp.getLogin();

		// Create the menu items
		List<Object> items = new ArrayList<Object>();
		items.add(new Category(login));
		items.add(new Item(mActivity.getString(R.string.menu_info),
				R.drawable.menu_info));
		items.add(new Item(mActivity.getString(R.string.menu_mdp),
				R.drawable.menu_password));
		items.add(new Category(mActivity.getString(R.string.title_webapps)));
		items.add(new Item(mActivity.getString(R.string.menu_notes),
				R.drawable.menu_notes));
		items.add(new Item(mActivity.getString(R.string.menu_absences),
				R.drawable.menu_absence));
		items.add(new Item(mActivity.getString(R.string.menu_cours),
				R.drawable.menu_cours));
		items.add(new Category(mActivity.getString(R.string.title_help)));
		items.add(new Item(mActivity.getString(R.string.menu_qfq),
				R.drawable.menu_qfq));
		items.add(new Item(mActivity.getString(R.string.menu_plan),
				android.R.drawable.ic_menu_compass));
		items.add(new Item(mActivity.getString(R.string.menu_logout),
				android.R.drawable.ic_lock_lock));

		// Create an adapter in order to save the item and create his view;
		mMenuAdapter = new MyMenuBaseAdapter(items,
				mActivity.getLayoutInflater());

		// Create a listview that will manage the scrolling menu
		mList = new MyListView(mActivity);
		mList.setAdapter(mMenuAdapter);
		setOnMenuItemClickListener();
		mList.setOnItemClickListener(mItemClickListener);

		mList.setOnScrollChangedListener(new MyListView.OnScrollChangedListener() {
			@Override
			public void onScrollChanged() {
				mMenuDrawer.invalidate();
			}
		});

		// Set the content we just created in the left slide menu
		mMenuDrawer.setMenuView(mList);
	}

	@Override
	public void createMenuFragment() {

		// Creating an ArrayList in order to save the fragments
		mFragments = new ArrayList<Fragment>();

		mFragments.add(Fragment.instantiate(mActivity,
				WelcomeFragment.class.getName()));
		mFragments.add(Fragment.instantiate(mActivity,
				InfoFragment.class.getName()));
		mFragments.add(Fragment.instantiate(mActivity,
				EcePasswordChangerFragment.class.getName()));
		mFragments.add(Fragment.instantiate(mActivity,
				GradesFragment.class.getName()));
		mFragments.add(Fragment.instantiate(mActivity,
				AttendanceFragment.class.getName()));
		// white fragment - course activity
		mFragments.add(whiteFragment);
		mFragments.add(Fragment.instantiate(mActivity,
				QFQFragment.class.getName()));
		// white fragment - map activity
		mFragments.add(whiteFragment);
		// white fragment - logout
		mFragments.add(Fragment.instantiate(mActivity,
				LogoutViewController.class.getName()));

		// Create a FragmentAdapter that save the fragments and manage their use
		mFragmentAdapter = new MyFragmentPagerAdapter(
				mActivity.getSupportFragmentManager(), mFragments);
		mViewPager = (MyViewPager) mActivity.findViewById(R.id.leftPager);
		mViewPager.setAdapter(mFragmentAdapter);
		mViewPager.setOffscreenPageLimit(NUMBER_OF_MENU_ITEM);

	}

	@Override
	public void setOnMenuItemClickListener() {
		// menu item handler
		mItemClickListener = new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (mActivity.isRightMenuOpen())
					mActivity.getRightMenu().closeMenu();

				LeftMenuKey itemClicked = LeftMenuKey.values()[position];
				switch (itemClicked) {
				case ABSENCES:
					mViewPager.setCurrentItem(4);
					// deleteMenuItemRowNotifications(mActivity
					// .getString(R.string.menu_absences));
					break;
				case COURS:

					if (!mActivity.isSyncActive())
						mActivity.getRightMenu().openMenu();
					else
						mViewPager.setCurrentItem(5);
					// deleteMenuItemRowNotifications(mActivity
					// .getString(R.string.menu_cours));

					break;
				case INFO:
					mViewPager.setCurrentItem(1);
					break;
				case LOGOUT:
					mViewPager.setCurrentItem(8);
					break;
				case MDP:
					mViewPager.setCurrentItem(2);
					break;

				case NOTES:
					mViewPager.setCurrentItem(3);
					// deleteMenuItemRowNotifications(mActivity
					// .getString(R.string.menu_notes));
					break;
				case PLAN:
					mViewPager.setCurrentItem(7);
					break;
				case QFQ:
					mViewPager.setCurrentItem(6);
					break;
				case NAME:
				case HELP:
				case WEBAPPS:
				default:
					break;
				}

				mActivePosition = position;
				mMenuDrawer.setActiveView(view, position);
				mMenuDrawer.closeMenu();

			}
		};
	}

	@Override
	public void setMenuItemRowNotification() {
		// // Grade
		// if (GradesQuery.getNewGrades() != 0) {
		// if (mViewPager.getCurrentItem() != 3) {
		// TextView tv = (TextView) mActivity
		// .getWindow()
		// .getDecorView()
		// .findViewWithTag(
		// mActivity.getString(R.string.menu_notes))
		// .findViewById(R.id.menu_row_notification);
		// tv.setText("" + GradesQuery.getNewGrades());
		// tv.setVisibility(View.VISIBLE);
		// }
		// }
		// // Attendance
		// if (AttendanceQuery.getNewAttendances() != 0) {
		// if (mViewPager.getCurrentItem() != 4) {
		// TextView tv1 = (TextView) mActivity
		// .getWindow()
		// .getDecorView()
		// .findViewWithTag(
		// mActivity.getString(R.string.menu_absences))
		// .findViewById(R.id.menu_row_notification);
		// tv1.setText("" + AttendanceQuery.getNewAttendances());
		// tv1.setVisibility(View.VISIBLE);
		// }
		// }
		// // Course
		// if (CoursesQuery.getNewCourses() != 0) {
		// if (mViewPager.getCurrentItem() != 5) {
		// TextView tv2 = (TextView) mActivity
		// .getWindow()
		// .getDecorView()
		// .findViewWithTag(
		// mActivity.getString(R.string.menu_cours))
		// .findViewById(R.id.menu_row_notification);
		// tv2.setText("" + CoursesQuery.getNewCourses());
		// tv2.setVisibility(View.VISIBLE);
		// }
		// }
	}

	@Override
	public void deleteMenuItemRowNotifications(String tag) {
		//
		// TextView tv = (TextView) mActivity.getWindow().getDecorView()
		// .findViewWithTag(tag).findViewById(R.id.menu_row_notification);
		// if (tv != null && tv.getVisibility() == View.VISIBLE) {
		//
		// tv.setText("");
		// tv.setVisibility(View.INVISIBLE);
		// }
	}

	public void syncEnded() {
		if (isWhiteFragmentCreated)
			whiteFragment.getView().findViewById(R.id.loading_status)
					.setVisibility(View.GONE);

		if (mViewPager.getCurrentItem() == 5)
			mActivity.getRightMenu().openMenu();

		// setMenuItemRowNotification();
	}

	public MenuDrawer getDrawer() {
		return mMenuDrawer;
	}

	public void toggleMenu() {
		mMenuDrawer.toggleMenu();
	}
}
