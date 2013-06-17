package fr.alexdet.android.controller.thread;

import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import fr.alexdet.android.controller.MainActivity;
import fr.alexdet.android.model.webservices.AttendanceQuery;
import fr.alexdet.android.model.webservices.CourseSectionsQuery;
import fr.alexdet.android.model.webservices.CoursesQuery;
import fr.alexdet.android.model.webservices.GradesQuery;
import fr.alexdet.android.model.webservices.QueryRepository;
import fr.alexdet.android.view.AttendanceFragment;
import fr.alexdet.android.view.GradesFragment;

/**
 * 
 * The class is an {@link AsyncTask} lunched from the {@link MainActivity}. Its
 * aim is the get the data either from the server, or from the internal database
 * {@link SQLiteDatabase}
 * 
 * @author alexisdetalhouet
 * 
 */
public class LoadDataTask extends AsyncTask<Void, Integer, String> {

	/**
	 * The activity context used to make our query
	 */
	private Context mContext;

	/**
	 * The activity executing the task
	 */
	private SherlockFragmentActivity mActivity;

	/**
	 * The bundle is used to get the notification type
	 */
	private Bundle b;

	/**
	 * if needData = true, it execute the dataBakcup from server
	 */
	private Boolean needData = true;

	/**
	 * Constructor
	 * 
	 * @param activity
	 *            the activity calling the task
	 * @param needDataFromServer
	 *            if true, the method gets the data through a query
	 *            {@link QueryRepository}
	 * @param b
	 *            if a bundle is present, the application has been lunched by a
	 *            Notification so we make a specific query to retrieve the data
	 */
	public LoadDataTask(SherlockFragmentActivity activity,
			Boolean needDataFromServer, Bundle b) {
		this.mActivity = activity;
		this.b = b;
		this.needData = needDataFromServer;
		this.mContext = mActivity.getApplicationContext();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	/**
	 * The actual task the class has to perform. This method is called while the
	 * parent Activity is active and functional.
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(Void... params) {
		// if the db hasn't yet be created, it means it is the first use; thus
		// we gather all the data from the server
		if (needData) {
			QueryRepository.getDataFromServer(mContext);
		} else
		// the data are already registered in the application database
		{
			QueryRepository.getDataFromDataBase(mContext);
			/*
			 * if the activity is opened when the user click on a notification,
			 * the bundle gives us the type of notification and get the data
			 * from the query and then save the new data in his repository
			 */

			if (b != null) {

				Log.v("LoadDataTask", "notification: " + b.toString());

				// data needed to make to query
				String mTableName = b.getString("tableName");
				String mRowID = b.getString("rowId");
				// lunch the query
				InputStream is = QueryRepository.getNotifiedDataJsonFromServer(
						mContext, mTableName, mRowID);

				Log.i("LoadDataTask",
						"inpout stream retrieve: " + is.toString());

				// depending on the type of data retrieved, call the method to
				// parse them
				if (mTableName.equals(AttendanceQuery.NOTIF_TAG)) {
					AttendanceQuery.parseAttendanceJSON(is, true);
					return AttendanceQuery.NOTIF_TAG;
				} else if (mTableName.equals(GradesQuery.NOTIF_TAG)) {
					GradesQuery.parseGradeJSON(is, true);
					return GradesQuery.NOTIF_TAG;
				} else if (mTableName.equals(CoursesQuery.NOTIF_TAG)) {
					CourseSectionsQuery.parseCourseSectionJSON(is, true);
					return CoursesQuery.NOTIF_TAG;
				} else
					return "OTHER TEST / PROF";
			}
		}
		return null;
	}

	/**
	 * Manage the progress status bar
	 * 
	 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	/**
	 * Once the task is done this function is called. Here we update the UIs
	 * (notification on the menu item and in the notification window)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result) {

		// TODO manage notification
		Log.v("LoadDataTask", "onPostExecute; notitifcation: " + result);

		((MainActivity) mActivity).syncEnded();

		// While the data are gathered, the views display a loading view.
		// Once
		// the data is download, refresh those views and hide the spinner.
		Intent data = new Intent(AttendanceFragment.FILTER_ACTION);
		mActivity.sendBroadcast(data);
		Intent data1 = new Intent(GradesFragment.FILTER_ACTION);
		mActivity.sendBroadcast(data1);
	}
}