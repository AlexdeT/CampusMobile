package fr.alexdet.android.model.gcm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import fr.alexdet.android.controller.MyNotifications;
import fr.alexdet.android.controller.SharedApp;
import fr.alexdet.android.model.webservices.QueryRepository;

/**
 * Class managing the google cloud messaging registration and notofication
 * 
 * @author alexisdetalhouet
 * 
 */
public class GCMIntentService extends GCMBaseIntentService {

	private static final String LOG_TAG = "GetAClue::GCMIntentService";
	/*
	 * The senderId define by google when we registered the application in the
	 * google cloud messaging service
	 */
	private static final String SENDER_ID = "527280961620";

	/**
	 * Constructor calling the GCMIntentService constructor with the senderId in
	 * argument
	 */
	public GCMIntentService() {
		super(SENDER_ID);
	}

	/**
	 * Trigger when an error occurred during the notification process
	 * 
	 * @see com.google.android.gcm.GCMBaseIntentService#onError(android.content.Context,
	 *      java.lang.String)
	 */
	@Override
	protected void onError(Context arg0, String errorId) {
		Log.i(LOG_TAG, "GCMIntentService onError called: " + errorId);
	}

	/**
	 * Trigger when the application receive a notification - Store the
	 * notification in the SQLite DB
	 * 
	 * @see com.google.android.gcm.GCMBaseIntentService#onMessage(android.content.Context,
	 *      android.content.Intent)
	 */
	@Override
	protected void onMessage(Context arg0, Intent intent) {
		Log.i(LOG_TAG, "GCMIntentService onMessage called");
		Log.i(LOG_TAG, "Message is: " + intent.getExtras().getString("message"));

		new MyNotifications(getApplicationContext(), intent.getExtras()
				.getString("message"));

	}

	/**
	 * Trigger once the user is registered on the google cloud messaging service
	 * - Send the registration id to our server in order to dispatch the
	 * notification
	 * 
	 * @see com.google.android.gcm.GCMBaseIntentService#onRegistered(android.content.Context,
	 *      java.lang.String)
	 */
	@Override
	protected void onRegistered(Context arg0, String registrationId) {
		Log.i(LOG_TAG, "GCMIntentService onRegistered called");
		Log.i(LOG_TAG, "Registration id is: " + registrationId);

		QueryRepository.sendRegIdToServer(registrationId,
				getApplicationContext());
	}

	/**
	 * Trigger once the user has unregistered from google cloud messaging
	 * service - Warn the server this registration id needs to be delete
	 * 
	 * @see com.google.android.gcm.GCMBaseIntentService#onUnregistered(android.content.Context,
	 *      java.lang.String)
	 */
	@Override
	protected void onUnregistered(Context arg0, String registrationId) {
		Log.i(LOG_TAG, "GCMIntentService onUnregistered called");
		Log.i(LOG_TAG, "Registration id is: " + registrationId);

		QueryRepository.deleteRegIdFromServer(registrationId,
				getApplicationContext());

		SharedApp.release();

	}
}
