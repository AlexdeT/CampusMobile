package fr.alexdet.android.model.webservices;

import java.io.InputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import fr.alexdet.android.model.bdd.AttendanceRepository;
import fr.alexdet.android.model.data.Attendance;
import fr.alexdet.android.model.data.Notification;

/**
 * Class managing the Attendance item - Request the data from the DB or the
 * server and parse the json if the data come from the server
 * 
 * @author alexisdetalhouet
 * 
 */
public class AttendanceQuery {

	/**
	 * url to make attendances request
	 */
	private static String url = "http://23.23.80.185/mep_web_service/absences.php";

	/**
	 * Tag to retrieve the type of notification
	 */
	public static final String NOTIF_TAG = "ABSENCES";

	/**
	 * JSON absences names - correspond to the tag send in the JSON by the
	 * server
	 */
	private static final String TAG_ABSENCE_ID = "absences";
	private static final String TAG_ABSENCE_SEMESTRE = "semestre";
	private static final String TAG_ABSENCE_HEURE_DEBUT = "heureDebut";
	private static final String TAG_ABSENCE_HEURE_FIN = "heureFin";
	private static final String TAG_ABSENCE_TYPE = "type";

	// JSONArray
	private static JSONArray absences = null;

	/*
	 * Attendance SQLite
	 */
	private static AttendanceRepository mAttendanceDataSet = null;
	private static int mNewAttendances;

	/**
	 * Instantiate the Attendance table
	 * 
	 * @param context
	 *            the application context
	 */
	public AttendanceQuery(Context context) {
		mAttendanceDataSet = new AttendanceRepository(context);
		mNewAttendances = 0;
	}

	/**
	 * Instantiate the Attendance table - Get the JSON from the server
	 * 
	 * @param context
	 *            the application context
	 * @param needData
	 *            if true, it is the first time the user is logging in
	 */
	public AttendanceQuery(Context context, Boolean needData) {
		mAttendanceDataSet = new AttendanceRepository(context);
		mNewAttendances = 0;

		if (needData) {
			InputStream jsonIS = QueryRepository
					.getJsonFromServer(context, url);
			AttendanceQuery.parseAttendanceJSON(jsonIS, false);
		}
	}

	/**
	 * Static method parsing the JSON got from the server
	 * 
	 * @param jsonIS
	 *            json retrieved by the server
	 * @param isNotification
	 *            TODO
	 * 
	 * @return
	 */
	public static Attendance parseAttendanceJSON(InputStream jsonIS,
			boolean isNotification) {
		// getting JSON string from URL
		JSONObject json = JSONParser.getJSONObject(jsonIS);

		Log.i("AttendanceQuery", json.toString());

		try {
			// Getting Array of attendance
			absences = json.getJSONArray(TAG_ABSENCE_ID);
			mAttendanceDataSet.open();
			// looping through All the attendance
			for (int i = 0; i < absences.length(); i++) {
				JSONObject c = absences.getJSONObject(i);

				// Storing each json item in variable
				String semestre = c.getString(TAG_ABSENCE_SEMESTRE);
				String timestampDebut = c.getString(TAG_ABSENCE_HEURE_DEBUT);
				String timestampFin = c.getString(TAG_ABSENCE_HEURE_FIN);
				String type = c.getString(TAG_ABSENCE_TYPE);

				String[] time_start = timestampDebut.split(" ");
				String date_start = time_start[0];
				String heureDebut = time_start[1].substring(0, 5);

				String[] time_end = timestampFin.split(" ");
				String date_end = time_end[0];
				String heureFin = time_end[1].substring(0, 5);

				String date = null;
				if (date_start.equals(date_end))
					date = date_end;

				// Storing in the intern database
				Attendance uneAbsence = new Attendance(semestre, date,
						heureDebut, heureFin, type);

				mAttendanceDataSet.Save(uneAbsence);
				mNewAttendances++;

				NotificationQuery.addNotification(new Notification(uneAbsence
						.toString(), NOTIF_TAG));
			}
			mAttendanceDataSet.close();

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getNewAttendances() {
		return mNewAttendances;
	}

	public static List<Attendance> getListAttendances() {
		mAttendanceDataSet.open();
		List<Attendance> list = mAttendanceDataSet.GetAll();
		mAttendanceDataSet.close();
		return list;
	}
}
