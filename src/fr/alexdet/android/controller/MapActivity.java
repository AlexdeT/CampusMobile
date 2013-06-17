package fr.alexdet.android.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.alexdet.android.R;

/**
 * This class display a map with the differents ECE places
 * 
 * @author alexisdetalhouet
 * 
 */
public class MapActivity extends MyActionBarActivity {

	/**
	 * Object to display the view
	 */
	private MapView mMapView;

	/**
	 * Object creating the view
	 */
	private GoogleMap mMap;

	/**
	 * Coordinate to center the map
	 */
	private static final LatLng CENTER_MAP = new LatLng(48.855906, 2.308846);
	/**
	 * QG coordinate
	 */
	private static final LatLng QG = new LatLng(48.852187, 2.286196);

	/**
	 * RG coordinate
	 */
	private static final LatLng RG = new LatLng(48.854654, 2.325339);

	/**
	 * Effeil coordiante
	 */
	private static final LatLng EFFIEL = new LatLng(48.851055, 2.288694);

	@Override
	protected void onCreate(Bundle inState) {
		super.onCreate(inState);

		setContentView(R.layout.map);

		try {
			MapsInitializer.initialize(this);
		} catch (GooglePlayServicesNotAvailableException e) {
			e.printStackTrace();
		}

		mMapView = (MapView) findViewById(R.id.map);
		mMapView.onCreate(inState);

		setActionBar();
		setUpMapIfNeeded();

	}

	@Override
	public void setActionBar() {
		TextView tv = (TextView) mActionBarCustomNav.findViewById(R.id.title);
		tv.setText("Plan");

		// hide right button
		mActionBarCustomNav.findViewById(R.id.rightButton).setVisibility(
				View.INVISIBLE);
		// set listener on right button; dismiss the activity when click
		mActionBarCustomNav.findViewById(R.id.leftButton).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						finish();
					}
				});
	}

	/**
	 * Set the map
	 */
	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((MapView) findViewById(R.id.map)).getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	/**
	 * Set the map content
	 */
	private void setUpMap() {

		mMap.setMyLocationEnabled(true);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CENTER_MAP, 12));

		mMap.addMarker(new MarkerOptions().position(QG).title("QG")
				.snippet("35 quai de Grenelle"));
		mMap.addMarker(new MarkerOptions().position(RG).title("RG")
				.snippet("34 rue de Grenelle"));
		mMap.addMarker(new MarkerOptions().position(EFFIEL).title("Effiel")
				.snippet("Adresse"));
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
