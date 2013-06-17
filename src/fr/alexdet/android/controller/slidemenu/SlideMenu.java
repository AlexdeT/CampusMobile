package fr.alexdet.android.controller.slidemenu;

import java.util.ArrayList;

import net.simonvt.menudrawer.MenuDrawer;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import fr.alexdet.android.view.MyFragmentPagerAdapter;
import fr.alexdet.android.view.MyListView;
import fr.alexdet.android.view.MyMenuBaseAdapter;
import fr.alexdet.android.view.MyViewPager;

public abstract class SlideMenu implements ISlideMenu {

	/**
	 * Key use to save the right menu drawer state
	 */
	public static final String STATE_MENUDRAWER_RIGHT = "menuDrawerRight";
	
	/**
	* Key use to save the right menu drawer active position
	*/
	public static final String STATE_ACTIVE_POSITION_RIGHT = "activePositionRight";

	/**
	* Key use to save the left menu drawer state
	*/
	public static final String STATE_MENUDRAWER_LEFT = "menuDrawerLeft";
	
	/**
	* Key use to save the left menu drawer active position
	*/
	public static final String STATE_ACTIVE_POSITION_LEFT = "activePositionLeft";

	/**
	 * {@link MenuDrawer}
	 */
	public MenuDrawer mMenuDrawer;

	/**
	 * Contain all the menu fragments (views)
	 */
	public MyViewPager mViewPager;

	/**
	 * List containing the menu items
	 */
	public MyListView mList;

	/**
	 * Adapter for the menu items
	 */
	public MyMenuBaseAdapter mMenuAdapter;

	/**
	 * Adapter for the fragments contained in the view pager
	 */
	public MyFragmentPagerAdapter mFragmentAdapter;

	/**
	 * Store the active menu item position in order to draw the arrow
	 */
	public int mActivePosition = -1;

	/**
	 * List containing all the fragments
	 */
	public ArrayList<Fragment> mFragments;

	/**
	 * Menu item listener
	 */
	public AdapterView.OnItemClickListener mItemClickListener;

}
