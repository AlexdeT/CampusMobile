package fr.alexdet.android.controller;

import android.app.Application;
import fr.ece.android.casclient.CasClient;

/**
 * Use of a singleton. It is implemented by overriding the Application class
 * which is never released while the application is running. We save the
 * casClient that successfully logged the USER with his LOGIN. It will be
 * available any time in the application, and anywhere by using
 * getApplicationContext(). It is declare in the AndroidManifest.xml file under
 * the "application" cot
 * 
 * @author alexisdetalhouet
 * 
 * 
 */
public class SharedApp extends Application {

	/**
	 * The client that successfully logged the user.
	 */
	private static CasClient mCasClient;

	/**
	 * The token return by the CAS authentication. TODO Should be used when
	 * making the queries in order to authenticate through the server
	 */
	private static String myToken;

	/**
	 * The user login
	 */
	private static String myLogin;

	/**
	 * Constructor
	 */
	public SharedApp() {
		super();
	}

	/**
	* Get the user login
	*@return myLogin the user login
	*/
	public static String getLogin() {
		return myLogin;
	}
	
	/**
	* Get the user token
	*@return myToken the user token
	*/
	public static String getToken() {
		return myToken;
	}

	/**
	* Set the user login
	*/
	public static void setLogin(String myLogin) {
		SharedApp.myLogin = myLogin;
	}

	/**
	*Set the user token
	*/
	public static void setToken(String myToken) {
		SharedApp.myToken = myToken;

	}
	/**
	* Get the {@link CasClient}
	*@return the {@link CasClient} that successfully logged the user
	*/
	public static CasClient getCasClient() {
		return mCasClient;
	}

	/**
	* Set the {@link CasClient}
	*/
	public static void setCasClient(CasClient mCasClient) {
		SharedApp.mCasClient = mCasClient;
	}

	/**
	* This method release all the value stored in the class
	*/
	public static void release() {
		myLogin = null;
		mCasClient = null;
		myToken = null;
	}
}
