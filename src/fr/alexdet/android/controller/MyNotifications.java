package fr.alexdet.android.controller;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import fr.alexdet.android.R;

/**
 * An instance of this class is created when a notification is received, and it
 * will be displayed on the Android notification bar. The created notification contains
 * a {@link PendingIntent} in order to start the {@link MainActivity}.
 * 
 * @author alexisdetalhouet
 * 
 */
public class MyNotifications {

	private static final String TAG = "MyNotifications";
	private int NOTIFICATION_ID = 198300000;

	private String tableName;
	private String rowID;
	private String pushID;
	private String action;

	private String msg;

	/**
	 * Constructor
	 * 
	 * Get the message receive. It contains the information to make the query
	 * which retrieve the data needed. We don't (re)load all the table, only the
	 * notified item
	 * 
	 * @param context
	 *            the application context
	 * @param serverPushMsg
	 *            the message retrieved through the notification delivered from
	 *            the Google Play Service
	 * 
	 */
	public MyNotifications(Context context, String serverPushMsg) {

		String[] data = serverPushMsg.split(";");

		pushID = data[0];
		tableName = data[1];
		rowID = data[2];
		action = data[3];
		msg = data[4];

		Log.i(TAG, "Notification created: table = " + tableName);

		createNotification(context, msg);

	}

	public String getTableName() {
		return tableName;
	}

	public String getRowID() {
		return rowID;
	}

	public String getPushID() {
		return pushID;
	}

	public String getAction() {
		return action;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setRowID(String rowID) {
		this.rowID = rowID;
	}

	public void setPushID(String pushID) {
		this.pushID = pushID;
	}

	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Create a {@link Notification} and displays it on the notification center
	 * 
	 * @param context
	 * @param message
	 */
	@SuppressWarnings("deprecation")
	private void createNotification(Context context, String message) {
		// Recuperation du notification Manager
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Recuperation du titre et description de la notfication
		String notificationTitle = "MyECEParis";
		String notificationDesc = message;

		// Creation de la notification avec specification de l'icone de la
		// notification et le texte qui apparait a la creation de la
		// notfication
		Notification notification = new Notification(R.drawable.ic_launcher,
				notificationTitle, System.currentTimeMillis());

		// Definition de la redirection au moment du clique sur la
		// notification.
		// Dans notre cas la notification redirige vers notre application en
		// passant en parametre le nom de la table a updater ainsi que la ligne
		// correspondant aux donnees a recuperer
		Bundle b = new Bundle();
		b.putString("tableName", tableName);
		b.putString("rowID", rowID);
		b.putInt("id", NOTIFICATION_ID);

		Log.v(TAG, "bundle : " + b.toString());

		Intent intent = new Intent(context, SplashActivity.class);
		intent.putExtra("notifID", b);
		intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);

		// // Notification & Vibration
		notification.setLatestEventInfo(context, notificationTitle,
				notificationDesc, pendingIntent);
		notification.vibrate = new long[] { 0, 200, 100, 200, 100, 200 };

		notificationManager.notify(NOTIFICATION_ID, notification);
	}

	// private void create(Context context) {
	// Intent resultIntent = new Intent(context, SplashActivity.class);
	// TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
	// // Adds the back stack
	// stackBuilder.addParentStack(MainSlideActivity.class);
	// // Adds the Intent to the top of the stack
	// stackBuilder.addNextIntent(resultIntent);
	// // Gets a PendingIntent containing the entire back stack
	// PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
	// PendingIntent.FLAG_UPDATE_CURRENT);
	// NotificationCompat.Builder builder = new NotificationCompat.Builder(
	// context);
	// builder.setContentIntent(resultPendingIntent);
	// NotificationManager mNotificationManager = (NotificationManager) context
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// mNotificationManager.notify(NOTIFICATION_ID, builder.build());
	// }
}
