package fr.alexdet.android.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import fr.alexdet.android.R;
import fr.alexdet.android.view.CourseFragment.Groupe;
import fr.alexdet.android.view.CourseFragment.Objet;

/**
 * Custom BaseExpandableListAdapter in order to display the courses elements has
 * wanted
 * 
 * @author alexisdetalhouet
 * 
 */
public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

	private ArrayList<Groupe> mGroupes;
	private LayoutInflater mInflater;

	public MyExpandableListViewAdapter(Context context,
			ArrayList<Groupe> groupes) {
		this.mGroupes = groupes;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	public Objet getChild(int gPosition, int cPosition) {
		return mGroupes.get(gPosition).getObjets().get(cPosition);
	}

	public long getChildId(int gPosition, int cPosition) {
		return cPosition;
	}

	/**
	 * Custom the view by adding to a group element all his item
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean,
	 *      android.view.View, android.view.ViewGroup)
	 */
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final Objet object = (Objet) getChild(groupPosition, childPosition);

		ChildViewHolder childViewHolder;

		if (convertView == null) {
			childViewHolder = new ChildViewHolder();

			convertView = mInflater.inflate(R.layout.expandable_child,
					null);

			childViewHolder.textViewChild = (TextView) convertView
					.findViewById(R.id.expandableChildText);

			convertView.setTag(childViewHolder);
		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}

		childViewHolder.textViewChild.setText(object.getNom());

		return convertView;
	}

	public int getChildrenCount(int gPosition) {
		return mGroupes.get(gPosition).getObjets().size();
	}

	public Object getGroup(int gPosition) {
		return mGroupes.get(gPosition);
	}

	public int getGroupCount() {
		return mGroupes.size();
	}

	public long getGroupId(int gPosition) {
		return gPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder gholder;

		Groupe group = (Groupe) getGroup(groupPosition);

		if (convertView == null) {
			gholder = new GroupViewHolder();

			convertView = mInflater.inflate(R.layout.expandable_group,
					null);

			gholder.textViewGroup = (TextView) convertView
					.findViewById(R.id.expandalbleGroupText);

			convertView.setTag(gholder);
		} else {
			gholder = (GroupViewHolder) convertView.getTag();
		}

		gholder.textViewGroup.setText(group.getNom());

		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	class GroupViewHolder {
		public TextView textViewGroup;
	}

	class ChildViewHolder {
		public TextView textViewChild;
	}

}
