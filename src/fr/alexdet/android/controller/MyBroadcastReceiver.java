package fr.alexdet.android.controller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import fr.alexdet.android.view.AttendanceFragment;
import fr.alexdet.android.view.GradesFragment;

/**
 *
 * This class extends {@link BroadcastReceiver}. {@link Activity} can register to this receiver, 
 * and depending on the {@link IntentFilter} actions defines, the class dispatch the update to the registered action
 *
 * @author alexisdetalhouet
 * 
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// Attendance action
		if (arg1.getAction().equals(AttendanceFragment.FILTER_ACTION))
			AttendanceFragment.setView();
		//Grade action
		if (arg1.getAction().equals(GradesFragment.FILTER_ACTION))
			GradesFragment.setView();

		Log.v("MyBroadcastReceiver", "" + arg1.getAction());

	}
}
