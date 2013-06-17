package fr.alexdet.android.controller.slidemenu;

import net.simonvt.menudrawer.MenuDrawer;
import android.support.v4.app.FragmentActivity;

public interface ISlideMenu {

	/**
	 * Configures the {@link MenuDrawer}
	 */
	public void configureMenuDrawer();

	/**
	 * Create the {@link MenuDrawer} content
	 */
	public void createMenuContent();

	/**
	 * Use of the {@link FragmentActivity}. It allows to change the actual view
	 * with a other view by created as many fragments as needed
	 * 
	 * The function create all the views corresponding to the menu content. Each
	 * fragment created corresponds to a specific view
	 */
	public void createMenuFragment();

	/**
	 * This method set a listener on the menu items. It will change to current
	 * view with the one linked to the menu item that triggers the method. This
	 * method also save the active position to retrieve easily the item or the
	 * view
	 */
	public void setOnMenuItemClickListener();

	/**
	 * Call once the data are loaded, this method update the menu item by
	 * displaying the number of new data on the left of the item. This method is
	 * called by the LoadDataTesk
	 * 
	 */
	public void setMenuItemRowNotification();

	/**
	 * Update left menu item; Delete notification displayed on the right of the
	 * item
	 * 
	 * @param item
	 *            item that needs update
	 */
	public void deleteMenuItemRowNotifications(String tag);

}
