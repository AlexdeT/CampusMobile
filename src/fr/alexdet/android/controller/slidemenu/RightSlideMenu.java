package fr.alexdet.android.controller.slidemenu;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import fr.alexdet.android.R;
import fr.alexdet.android.controller.MainActivity;
import fr.alexdet.android.model.data.Course;
import fr.alexdet.android.model.webservices.CoursesQuery;
import fr.alexdet.android.view.CourseFragment;
import fr.alexdet.android.view.MyBaseAdapter.Category;
import fr.alexdet.android.view.MyBaseAdapter.Item;
import fr.alexdet.android.view.MyFragmentPagerAdapter;
import fr.alexdet.android.view.MyListView;
import fr.alexdet.android.view.MyMenuBaseAdapter;
import fr.alexdet.android.view.MyViewPager;

public class RightSlideMenu extends SlideMenu {

	private List<Course> listCourses;

	// private static final String TAG = "RightSlideMenu";

	private MainActivity mActivity;

	private static boolean firstOpening = true;

	public RightSlideMenu(MainActivity activity, List<Course> courses) {

		mActivity = activity;

		listCourses = courses;
		this.configureMenuDrawer();
		this.createMenuContent();
		this.createMenuFragment();
	}

	@Override
	public void configureMenuDrawer() {

		// set the menu position (right or left), here right
		// set the way to trigger the menu, here, from wherever on the screen,
		// by swiping from right to left, the menu is shown
		mMenuDrawer = MenuDrawer.attach(mActivity,
				MenuDrawer.MENU_DRAG_CONTENT, Position.RIGHT);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
		mMenuDrawer.setContentView(((MainActivity) mActivity).getLeftDrawer());

	}

	@Override
	public void createMenuContent() {
		List<Object> items = new ArrayList<Object>();
		items.add(new Category("Liste des cours"));
		for (Course aCourse : listCourses) {
			items.add(new Item(aCourse.getShortname(),
					android.R.drawable.ic_menu_edit, false, aCourse
							.getNbNotif()));
		}

		mMenuAdapter = new MyMenuBaseAdapter(items,
				mActivity.getLayoutInflater());

		// Init the menu list
		mList = new MyListView(mActivity);
		mList.setAdapter(mMenuAdapter);
		this.setOnMenuItemClickListener();
		mList.setOnItemClickListener(mItemClickListener);
		mList.setOnScrollChangedListener(new MyListView.OnScrollChangedListener() {
			@Override
			public void onScrollChanged() {
				mMenuDrawer.invalidate();
			}
		});

		mMenuDrawer.setMenuView(mList);
	}

	@Override
	public void createMenuFragment() {
		// list of menu fragments
		mFragments = new ArrayList<Fragment>();

		for (Course aCourse : listCourses) {
			// save the course name in a bundle to put it in the fragment
			Bundle bundle = new Bundle();
			bundle.putString("course_name", aCourse.getShortname());
			Fragment fragment = Fragment.instantiate(
					mActivity.getApplicationContext(),
					CourseFragment.class.getName(), bundle);
			mFragments.add(fragment);
		}

		// Initialize the adapter and the pager with all the courses views
		// when click on an item, it gets the item view from the viewpager
		mFragmentAdapter = new MyFragmentPagerAdapter(
				mActivity.getSupportFragmentManager(), mFragments);

		mViewPager = (MyViewPager) mActivity.findViewById(R.id.rightPager);
		mViewPager.setOffscreenPageLimit(listCourses.size());
		mViewPager.setAdapter(mFragmentAdapter);
	}

	@Override
	public void setOnMenuItemClickListener() {

		mItemClickListener = new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mActivePosition = position;
				mMenuDrawer.setActiveView(view, position);

				Item t = (Item) mList.getItemAtPosition(position);

				for (int i = 0; i < listCourses.size(); i++)
					if (t.mTitle.equals(listCourses.get(i).getShortname())) {
						mViewPager.setCurrentItem(i);
						// deleteMenuItemRowNotifications(listCourses.get(i)
						// .getShortname());
					}

				mMenuDrawer.closeMenu();
			}
		};
	}

	// private void orderCoursesPerCourseSections() {
	//
	// for (Course aCourse : listCourses) {
	// // Getting all the course section per course
	// for (CourseSection aSection : CourseSectionsQuery
	// .getListCourseSections()) {
	// if (aSection.getCourseId() == aCourse.getCourseid()) {
	// aCourse.setNbNotif(aCourse.getNbNotif() + 1);
	// }
	// }
	//
	// if (aCourse.getNbNotif() != 0) {
	// CoursesQuery.updateDataSet(aCourse);
	// Log.v(TAG, "orderCoursesPerCourseSections : update item "
	// + aCourse.toString());
	// }
	// }
	// }

	@Override
	public void setMenuItemRowNotification() {
		// Log.v(TAG, "setMenuItemRowNotification");
		//
		// for (Course aCourse : listCourses) {
		// TextView tv = (TextView) mActivity.getWindow().getDecorView()
		// .findViewWithTag(aCourse.getShortname())
		// .findViewById(R.id.menu_row_notification);
		//
		// tv.setText("" + aCourse.getNbNotif());
		// tv.setVisibility(View.VISIBLE);
		// }
	}

	@Override
	public void deleteMenuItemRowNotifications(String tag) {
		// Log.v(TAG, "deleteMenuItemRowNotifications");
		// TextView tv = (TextView) mActivity.getWindow().getDecorView()
		// .findViewWithTag(tag).findViewById(R.id.menu_row_notification);
		// if (tv != null && tv.getVisibility() == View.VISIBLE) {
		// tv.setText("");
		// tv.setVisibility(View.INVISIBLE);
		// }
	}

	public void openMenu() {

		if (firstOpening) {
			firstOpening = false;
			// orderCoursesPerCourseSections();
			// setMenuItemRowNotification();
		}

		mActivity.setIsRightMenuOpen(true);
		mActivity.getSupportActionBar().getCustomView()
				.findViewById(R.id.rightButton).setVisibility(View.VISIBLE);

		mMenuDrawer.toggleMenu();
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		mMenuDrawer.findViewById(R.id.rightPager).setVisibility(View.VISIBLE);
		mMenuDrawer.findViewById(R.id.leftPager).setVisibility(View.GONE);
	}

	public void hideMenu() {
		mMenuDrawer.closeMenu();
	}

	public void closeMenu() {
		mActivity.setIsRightMenuOpen(false);
		mActivity.getSupportActionBar().getCustomView()
				.findViewById(R.id.rightButton).setVisibility(View.GONE);
		mActivity.getLeftDrawer().findViewById(R.id.leftPager)
				.setVisibility(View.VISIBLE);

		mMenuDrawer.closeMenu();
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
		mMenuDrawer.findViewById(R.id.rightPager).setVisibility(View.GONE);

	}

	public MenuDrawer getDrawer() {
		return mMenuDrawer;
	}
}
