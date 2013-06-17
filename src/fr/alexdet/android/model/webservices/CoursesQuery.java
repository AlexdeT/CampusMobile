package fr.alexdet.android.model.webservices;

import java.io.InputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import fr.alexdet.android.model.bdd.CourseRepository;
import fr.alexdet.android.model.data.Course;
import fr.alexdet.android.model.data.Notification;

/**
 * Class managing the Course item - Request the data from the DB or the server
 * and parse the json if the data come from the server
 * 
 * @author alexisdetalhouet
 * 
 */
public class CoursesQuery {

	/*
	 * Course repository
	 */
	private static CourseRepository mCourseDataSet;

	/*
	 * url to make courses request
	 */
	private static String url = "http://23.23.80.185/mep_web_service/cours.php";

	/**
	 * Tag to retrieve the type of notification
	 */
	public static final String NOTIF_TAG = "MDL_COURSE_SECTIONS";

	/*
	 * JSON courses names
	 */
	private static final String TAG_COURSE_COL_ID = "cours";
	private static final String TAG_COURS_USERNAME = "username";
	private static final String TAG_COURS_SHORTNAME = "shortname";
	private static final String TAG_COURS_FULLNAME = "fullname";
	private static final String TAG_COURS_COURSEID = "courseid";
	// private static final String TAG_COURS_COURSE_NB_NOTIF = "nbNotif";

	// JSONArray
	private static JSONArray courses = null;

	private static int mNewCourses;

	/**
	 * Instantiate the Course table
	 * 
	 * @param context
	 *            the application context
	 */
	public CoursesQuery(Context context) {
		mCourseDataSet = new CourseRepository(context);
		mNewCourses = 0;
	}

	/**
	 * Instantiate the Course table - Get the JSON from the server
	 * 
	 * @param context
	 *            the application context
	 * @param needData
	 *            if true, it is the first time the user is logging in
	 */
	public CoursesQuery(Context context, Boolean needData) {
		mCourseDataSet = new CourseRepository(context);
		mNewCourses = 0;

		if (needData) {
			InputStream jsonIS = QueryRepository
					.getJsonFromServer(context, url);
			CoursesQuery.parseCourseJSON(jsonIS, false);
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
	public static Course parseCourseJSON(InputStream jsonIS,
			boolean isNotification) {

		JSONObject json = JSONParser.getJSONObject(jsonIS);

		try {

			courses = json.getJSONArray(TAG_COURSE_COL_ID);
			mCourseDataSet.open();
			for (int i = 0; i < courses.length(); i++) {
				JSONObject c = courses.getJSONObject(i);

				String username = c.getString(TAG_COURS_USERNAME);
				String shortname = c.getString(TAG_COURS_SHORTNAME);
				String fullname = c.getString(TAG_COURS_FULLNAME);
				int courseid = c.getInt(TAG_COURS_COURSEID);
				int nbNotif = 0;

				Course unCours = new Course(username, shortname, fullname,
						courseid, nbNotif);

				mCourseDataSet.Save(unCours);
				mNewCourses++;

				NotificationQuery.addNotification(new Notification(shortname,
						NOTIF_TAG));

				if (isNotification)
					return unCours;
			}
			mCourseDataSet.close();

		} catch (JSONException e) {
			e.printStackTrace();

		}
		return null;
	}

	public static int getNewCourses() {
		return mNewCourses;
	}

	public static List<Course> getListCourses() {
		mCourseDataSet.open();
		List<Course> list = mCourseDataSet.GetAll();
		mCourseDataSet.close();
		return list;
	}

	public static void updateDataSet(Course entite) {
		mCourseDataSet.open();
		mCourseDataSet.Update(entite);
		mCourseDataSet.close();
	}
}
