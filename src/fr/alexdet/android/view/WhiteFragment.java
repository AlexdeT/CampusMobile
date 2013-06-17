package fr.alexdet.android.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

import fr.alexdet.android.R;
import fr.alexdet.android.controller.MainActivity;
import fr.alexdet.android.controller.slidemenu.LeftSlideMenu;

/**
 * White Fragment used for the MainSlideActivity
 * 
 * Some item in the menu are activities, then when the user choose one, instead
 * of display whatever fragment, we display a white fragment
 * 
 * @author alexisdetalhouet
 * 
 */
public class WhiteFragment extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.v("WhiteFragment", "onCreateView while sync : "
				+ ((MainActivity) getSherlockActivity()).isSyncActive());

		View v = inflater.inflate(R.layout.white, container, false);

		if (!((MainActivity) getSherlockActivity()).isSyncActive()) {
			v.findViewById(R.id.loading_status).setVisibility(View.GONE);

			Log.v("WhiteFragment", "hide loading status");
		}

		v.setTag("white");
		return v;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LeftSlideMenu.isWhiteFragmentCreated = false;
		Log.v("WhiteFragment", "onDestroyView ");
	}
}
