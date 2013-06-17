package fr.alexdet.android.model.webservices;

import java.io.InputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import fr.alexdet.android.model.bdd.CourseSectionRepository;
import fr.alexdet.android.model.data.CourseSection;
import fr.alexdet.android.model.data.Notification;

/**
 * Class managing the CourseSection item - Request the data from the DB or the
 * server and parse the json if the data come from the server
 * 
 * @author alexisdetalhouet
 * 
 */
public class CourseSectionsQuery {

	/*
	 * url to make courses sections request
	 */
	private static String url = "http://23.23.80.185/mep_web_service/sections.php";

	/*
	 * JSON course section names - correspond to the tag send in the JSON by the
	 * server
	 */
	private static final String TAG_COURSE_SECTION_COL_ID = "sections";
	private static final String TAG_COURSE_SECTION_ID = "id";
	private static final String TAG_COURSE_SECTION_COURSE = "course";
	private static final String TAG_COURSE_SECTION_NAME = "name";
	private static final String TAG_COURSE_SECTION_SUMMARY = "summary";

	// JSONArray
	private static JSONArray courseSection = null;

	/*
	 * CourseSection SQLite
	 */
	private static CourseSectionRepository mCourseSectionDataSet;
	private static int mNewCourseSections;

	public final static String NOTIF_TAG = "N_COURS_SECTION";

	/**
	 * Instantiate the CourseSection table
	 * 
	 * @param context
	 *            the application context
	 */
	public CourseSectionsQuery(Context context) {

		mCourseSectionDataSet = new CourseSectionRepository(context);
		mNewCourseSections = 0;
	}

	/**
	 * Instantiate the Attendance table - Get the JSON from the server
	 * 
	 * @param context
	 *            the application context
	 * @param needData
	 *            if true, it is the first time the user is logging in
	 */
	public CourseSectionsQuery(Context context, Boolean needData) {

		mCourseSectionDataSet = new CourseSectionRepository(context);
		mNewCourseSections = 0;

		if (needData) {
			InputStream jsonIS = QueryRepository.getJsonFromServer(
					context, url);
			CourseSectionsQuery.parseCourseSectionJSON(jsonIS, false);
		}
	}

	public static CourseSection parseCourseSectionJSON(InputStream jsonIS,
			boolean isNotification) {

		JSONObject json = JSONParser.getJSONObject(jsonIS);

		try {

			courseSection = json.getJSONArray(TAG_COURSE_SECTION_COL_ID);
			mCourseSectionDataSet.open();
			for (int i = 0; i < courseSection.length(); i++) {
				JSONObject c = courseSection.getJSONObject(i);

				int id = c.getInt(TAG_COURSE_SECTION_ID);
				int course = c.getInt(TAG_COURSE_SECTION_COURSE);
				String name = c.getString(TAG_COURSE_SECTION_NAME);
				String summary = c.getString(TAG_COURSE_SECTION_SUMMARY);

				CourseSection uneSectionDunCours = new CourseSection(id,
						course, name, summary);

				mCourseSectionDataSet.Save(uneSectionDunCours);
				mNewCourseSections++;

				NotificationQuery
						.addNotification(new Notification(uneSectionDunCours
								.toString(), NOTIF_TAG));

				if (isNotification)
					return uneSectionDunCours;
			}
			mCourseSectionDataSet.close();

		} catch (JSONException e) {
			e.printStackTrace();

		}
		return null;

	}

	public static int getNewCourseSection() {
		return mNewCourseSections;
	}

	public static List<CourseSection> getListCourseSections() {
		mCourseSectionDataSet.open();
		List<CourseSection> list = mCourseSectionDataSet.GetAll();
		mCourseSectionDataSet.close();
		return list;
	}
}
