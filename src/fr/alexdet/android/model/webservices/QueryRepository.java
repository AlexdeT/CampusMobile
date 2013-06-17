package fr.alexdet.android.model.webservices;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;
import fr.alexdet.android.controller.SharedApp;

/**
 * Abstract class defining all the method needed to make the server request
 * 
 * @author alexisdetalhouet
 * 
 */
public abstract class QueryRepository {

	private static final String TAG = "QueryRepository";

	/*
	 * url to send the google registration ID to the server
	 */
	private static String urlLogin = "http://23.23.80.185/mep_web_service/register.php";
	private static String urlLogout = "http://23.23.80.185/mep_web_service/logout.php";

	/*
	 * url to get tha data corresponding to a notification
	 */
	private static final String notificationUrl = "http://23.23.80.185/mep_web_service/get_maj_from_notif.php";

	public static void getData(Context context, boolean fromServer) {
		new NotificationQuery(context);

		new AttendanceQuery(context, fromServer);
		new CoursesQuery(context, fromServer);
		new CourseSectionsQuery(context, fromServer);
		new GradesQuery(context, fromServer);
	}

	/**
	 * Return the stored data (SQLite db) Create a new Query for each type of
	 * data
	 * 
	 * @param context
	 *            the application context
	 */
	public static void getDataFromDataBase(Context context) {
		new NotificationQuery(context);

		new AttendanceQuery(context);
		new CoursesQuery(context);
		new CourseSectionsQuery(context);
		new GradesQuery(context);
	}

	/**
	 * Return the data contain in the server db - Create a new Query for each
	 * type of data with the request for data
	 * 
	 * @param context
	 *            the application context
	 */
	public static void getDataFromServer(Context context) {
		new NotificationQuery(context);

		new AttendanceQuery(context, true);
		new CoursesQuery(context, true);
		new CourseSectionsQuery(context, true);
		new GradesQuery(context, true);
	}

	/**
	 * Send the google registration id to the server
	 * 
	 * @param regId
	 *            the user regId given by the google cloud messaging service
	 *            once registered
	 * @param context
	 *            the application context
	 */
	public static void sendRegIdToServer(String regId, Context context) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("studentID", SharedApp.getLogin()));
		params.add(new BasicNameValuePair("regId", regId));

		try {
			// Making HTTP request
			HttpPost httpPost = new HttpPost(urlLogin);
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.execute(httpPost);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ask the server to delete the regId corresponding to the logged user
	 * 
	 * @param regId
	 *            the user regId that has been unregistered from the google
	 *            cloud messaging service
	 * @param context
	 *            the application context
	 */
	public static void deleteRegIdFromServer(String regId, Context context) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("studentID", SharedApp.getLogin()));
		params.add(new BasicNameValuePair("regID", regId));

		try {
			// Making HTTP request
			HttpPost httpPost = new HttpPost(urlLogout);
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.execute(httpPost);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Make the query to the server
	 * 
	 * @param context
	 *            the application context
	 * @param url
	 *            the url where to get the data
	 * @return an InputStream containing the JSON table
	 */
	public static InputStream getJsonFromServer(Context context, String url) {
		Log.i(TAG, "Get data from url: " + url);

		InputStream is = null;

		// Making HTTP request
		try {
			// TODO
			/*
			 * for CASfy the server, we need to send the token returned by the
			 * cas authentication process, for now it dosen't work, can't find
			 * the issue so we send the user name in the post query
			 */
			// HttpGet httpGet = new HttpGet(url + "?ticket="
			// + SharedApp.getToken());

			/*
			 * send the login through the httpPost
			 */
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("studentID", SharedApp.getLogin()));

			// Making HTTP request
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);

			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (is == null)
			Log.w(TAG, "Query to: " + url + " is empty");
		return is;
	}

	/**
	 * Make the query to the server. Ask for the specific data dealing with the
	 * notification received
	 * 
	 * @param context
	 *            the application context
	 * @param tableName
	 *            the table where to get the data
	 * @param rowId
	 *            the row of the table in which the info are contained
	 * @return an InputStream containing the JSON row
	 */
	public static InputStream getNotifiedDataJsonFromServer(Context context,
			String tableName, String rowId) {
		Log.i(TAG, "Get notified data from");
		InputStream is = null;

		// Making HTTP request
		try {

			// send the login through the httpPost
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// TODO as above, we need the send the token and not the user login
			params.add(new BasicNameValuePair("studentID", SharedApp.getLogin()));

			params.add(new BasicNameValuePair("tableName", tableName));
			params.add(new BasicNameValuePair("rowID", rowId));

			// Making HTTP request
			HttpPost httpPost = new HttpPost(notificationUrl);
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);

			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			Log.i(TAG, "" + is);
			return is;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.i(TAG, "Query <i>Notification</i> returns no data");
		return null;
	}
}
