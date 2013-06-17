package fr.alexdet.android.model.webservices;

import java.util.List;

import android.content.Context;
import fr.alexdet.android.model.bdd.NotificationRepository;
import fr.alexdet.android.model.data.Notification;

/**
 * Contain and manage the NotificationRepository
 * 
 * @author alexisdetalhouet
 * 
 */
public class NotificationQuery {

	/**
	 * SQLite database
	 */
	private static NotificationRepository mNotificationRepository;

	/**
	 * Create the notification data set
	 * 
	 * @param context
	 *            the application context
	 */
	public NotificationQuery(Context context) {
		mNotificationRepository = new NotificationRepository(context);
	}

	/**
	 * Add a new notification
	 * 
	 * @param n
	 *            notification to be added
	 */
	public static void addNotification(Notification n) {
		mNotificationRepository.open();
		mNotificationRepository.Save(n);
		mNotificationRepository.close();
	}

	/**
	 * Get all the notifications
	 * 
	 * @return a list with all the notifications
	 */
	public static List<Notification> getListNotif() {
		mNotificationRepository.open();
		List<Notification> list = mNotificationRepository.GetAll();
		mNotificationRepository.close();
		return list;
	}
}
