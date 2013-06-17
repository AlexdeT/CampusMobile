package fr.alexdet.android.model.gcm;

import android.content.Context;


/**
 * Class extends GCMBroadcastReceiver in order to set the
 * GCMIntentServiceClassName
 * 
 * @author alexisdetalhouet
 * 
 */
public class GCMBroadcastReceiver extends
		com.google.android.gcm.GCMBroadcastReceiver {

	/**
	 * Return the class name
	 * 
	 * @see com.google.android.gcm.GCMBroadcastReceiver#getGCMIntentServiceClassName(android.content.Context)
	 */
	@Override
	protected String getGCMIntentServiceClassName(Context context) {

		return "fr.alexdet.android.model.gcm.GCMIntentService";
	}
}
