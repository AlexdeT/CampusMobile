package fr.alexdet.android.controller.thread;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import fr.alexdet.android.controller.LoginActivity;
import fr.alexdet.android.controller.SharedApp;
import fr.alexdet.android.controller.key.LoginResult;
import fr.ece.android.casclient.CasAuthenticationException;
import fr.ece.android.casclient.CasClient;
import fr.ece.android.casclient.CasProtocolException;
import fr.ece.android.casclient.EceHttpClient;

/**
 * This class is an asynchronous task. It tries to log the user through the ECE
 * CAS protocol
 * 
 * Return the result of the connection and if the connection failed, pop a
 * dialog box telling the problem
 * 
 * @author alexisdetalhouet
 * 
 */
public class LoginUserTask extends AsyncTask<Void, Void, LoginResult> {

	/**
	 * URL to make the CAS authentification
	 */
	private static final String casUrl = "https://webauth.ece.fr/cas/";
	private static final String serviceUrl = "http://23.23.80.185/mep_web_service/";

	private LoginActivity mActivity;

	private CasClient mCasClient;
	private String mToken;

	private String mLogin;
	private String mPassword;

	public LoginUserTask(LoginActivity activity, String login, String password) {
		mActivity = activity;

		mLogin = login;
		mPassword = password;
	}

	/**
	 * Try to log the user
	 * 
	 * @return LoginResult - if null, the user is well authenticated, else
	 *         contain the error that occured
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected LoginResult doInBackground(Void... params) {
		// Check for internet connectivity
		ConnectivityManager connMgr = (ConnectivityManager) mActivity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnected()) {
			return LoginResult.NETWORK_UNAVAIL;
		} else {

			// Build an HttpClient to which the auth info will be attached.
			EceHttpClient httpClient = new EceHttpClient(
					mActivity.getApplication());

			// The local client that will handle the CAS authentication
			mCasClient = new CasClient(httpClient, casUrl);

			// login CAS user with no service
			try {

				mToken = mCasClient.login(serviceUrl, mLogin, mPassword);
				// casClient.validate(serviceUrl, mToken);

				SharedApp.setCasClient(mCasClient);
				SharedApp.setToken(mToken);
			} catch (CasAuthenticationException e) {
				return LoginResult.CAS_AUTH_FAILED;
			} catch (CasProtocolException e) {
				return LoginResult.CAS_PROTOCOL_ERROR;
			}
			return null;
		}
	}

	/**
	 * @param LoginResult
	 *            if null, save the user content in the SharedApp and start the
	 *            application else pop up a dialog with the error
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(LoginResult result) {

		Log.v("LoginUserTask", "onPostExecute; LoginResult = " + result);
		mActivity.loginEnded(result);
	}
}
