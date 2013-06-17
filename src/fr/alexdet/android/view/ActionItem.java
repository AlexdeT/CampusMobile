package fr.alexdet.android.view;

/**
 * Action item, displayed as menu with icon and text.
 * 
 * @author Lorensius. W. L. T <lorenz@londatiga.net>
 * 
 * 
 *         Adapted for MyECEParis : - Alexis de Talhouet
 * 
 */
public class ActionItem {
	private String title;
	private int iconId = -1;
	private boolean selected;
	private boolean sticky = true;

	/**
	 * Constructor
	 * 
	 * @param iconId
	 *            The drawable attach to the textView
	 * @param title
	 *            Text to show for the item
	 */
	public ActionItem(int iconId, String title) {
		this.title = title;
		this.iconId = iconId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	public int getIconId() {
		return iconId;
	}

	public void setSticky(boolean sticky) {
		this.sticky = sticky;
	}

	public boolean isSticky() {
		return sticky;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return this.selected;
	}
}