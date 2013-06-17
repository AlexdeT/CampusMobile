package fr.alexdet.android.model.webservices;

import java.io.InputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import fr.alexdet.android.model.bdd.GradeReposiroty;
import fr.alexdet.android.model.data.Grade;
import fr.alexdet.android.model.data.Notification;

/**
 * Class managing the Grade item - Request the data from the DB or the server
 * and parse the json if the data come from the server
 * 
 * @author alexisdetalhouet
 * 
 */
public class GradesQuery {

	/*
	 * Grade repository
	 */
	private static GradeReposiroty mGradeDataSet;
	private static int mNewGrades;

	/*
	 * url to make grades request
	 */
	private static String url = "http://23.23.80.185/mep_web_service/notes.php";

	public static final String NOTIF_TAG = "NOTES";

	/*
	 * JSON notes names - correspond to the tag send in the JSON by the server
	 */
	private static final String TAG_NOTE_ID = "notes";
	private static final String TAG_NOTE_MODU = "modu";
	private static final String TAG_NOTE_INTITULE = "intitule";
	private static final String TAG_NOTE_SEMESTRE = "semestre";
	private static final String TAG_NOTE_NOTE = "note";
	private static final String TAG_NOTE_MOYENNE_CLASSE = "moyenneClasse";

	// JSONArray
	private static JSONArray notes = null;

	/**
	 * Instantiate the Grade table
	 * 
	 * @param context
	 *            the application context
	 */
	public GradesQuery(Context context) {
		mGradeDataSet = new GradeReposiroty(context);
		mNewGrades = 0;
	}

	/**
	 * Instantiate the Grade table - Get the JSON from the server
	 * 
	 * @param context
	 *            the application context
	 * @param needData
	 *            if true, it is the first time the user is logging in
	 */
	public GradesQuery(Context context, Boolean needData) {
		mGradeDataSet = new GradeReposiroty(context);
		mNewGrades = 0;

		if (needData) {
			InputStream jsonIS = QueryRepository.getJsonFromServer(
					context, url);
			GradesQuery.parseGradeJSON(jsonIS, false);
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
	public static Grade parseGradeJSON(InputStream jsonIS,
			boolean isNotification) {
		// getting JSON string from URL
		JSONObject json = JSONParser.getJSONObject(jsonIS);

		try {
			// Getting Array of attendance
			notes = json.getJSONArray(TAG_NOTE_ID);
			mGradeDataSet.open();
			for (int i = 0; i < notes.length(); i++) {
				JSONObject c = notes.getJSONObject(i);

				String modu = c.getString(TAG_NOTE_MODU);
				String intitule = c.getString(TAG_NOTE_INTITULE);
				String semestre = c.getString(TAG_NOTE_SEMESTRE);
				String note = c.getString(TAG_NOTE_NOTE);
				String moyenneClasse = c.getString(TAG_NOTE_MOYENNE_CLASSE);

				Grade uneNote = new Grade(modu, intitule, semestre, note,
						moyenneClasse);

				mGradeDataSet.Save(uneNote);
				mNewGrades++;

				NotificationQuery.addNotification(new Notification(uneNote
						.toString(), NOTIF_TAG));

				if (isNotification)
					return uneNote;
			}
			mGradeDataSet.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getNewGrades() {
		return mNewGrades;
	}

	public static List<Grade> getListGrades() {
		mGradeDataSet.open();
		List<Grade> list = mGradeDataSet.GetAll();
		mGradeDataSet.close();
		return list;
	}
}
