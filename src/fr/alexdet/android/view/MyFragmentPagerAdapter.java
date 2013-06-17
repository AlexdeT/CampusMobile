package fr.alexdet.android.view;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Custom BaseExpandableListAdapter - Contain all the Fragment - Allows to set a
 * Fragment title
 * 
 * @author alexisdetalhouet
 * 
 */
public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

	/*
	 * List of Fragments
	 */
	private final ArrayList<Fragment> fragments;

	/**
	 * Constructor calling the super constructor with the activity
	 * FragmentManager
	 * 
	 * @param fm
	 *            the activity FragmentManager
	 */
	public MyFragmentPagerAdapter(android.support.v4.app.FragmentManager fm) {
		super(fm);
		this.fragments = null;
	}

	/**
	 * Constructor calling the super constructor with the activity
	 * FragmentManager - Save the Fragment array
	 * 
	 * @param fm
	 *            the activity FragmentManager
	 */
	public MyFragmentPagerAdapter(android.support.v4.app.FragmentManager fm,
			ArrayList<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return getItem(position).getArguments().getString("course_name");
	}

	@Override
	public Fragment getItem(int position) {

		return this.fragments.get(position);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}
}
