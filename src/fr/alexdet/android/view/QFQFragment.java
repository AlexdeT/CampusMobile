package fr.alexdet.android.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

import fr.alexdet.android.R;


/**
 * TODO
 * 
 * Set the "qui-fait-quoi" - Data needed from the server or define plain text
 * 
 * @author alexisdetalhouet
 * 
 */
public class QFQFragment extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.qfq, container, false);
		v.setTag("qfq");
		return v;
	}
}
