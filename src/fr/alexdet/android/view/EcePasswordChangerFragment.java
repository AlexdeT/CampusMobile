package fr.alexdet.android.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;

import fr.alexdet.android.R;
import fr.alexdet.android.controller.SharedApp;
import fr.alexdet.android.controller.key.ChangePasswordResult;
import fr.ece.android.casclient.CasAuthenticationException;
import fr.ece.android.casclient.CasClient;
import fr.ece.android.casclient.CasProtocolException;
import fr.ece.android.casclient.EceHttpClient;

/**
 * ECE password changer view
 * 
 * Contain an AsynckTask in order to make the internet queries
 * 
 * @author alexisdetalhouet
 * 
 */
public class EcePasswordChangerFragment extends SherlockFragment {

	/*
	 * URL to request
	 */
	private final static String serviceUrl = "https://sysuser.ece.fr/systems/password/change/";
	private static final String casUrl = "https://webauth.ece.fr/cas/";

	private Button mGoButton;
	private EditText loginText;
	private EditText oldPassText;
	private EditText newPass1Text;
	private EditText newPass2Text;

	private View mChangePasswordFormView;
	private View mChangePasswordStatusView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LinearLayout wrapper = new LinearLayout(getSherlockActivity());
		inflater.inflate(R.layout.password_changer, wrapper, true);

		mChangePasswordFormView = wrapper
				.findViewById(R.id.password_changer_form);
		mChangePasswordStatusView = wrapper.findViewById(R.id.password_status);

		setHasOptionsMenu(true);

		// set the login from saved user
		loginText = (MyClearableEditText) mChangePasswordFormView
				.findViewById(R.id.loginText);
		loginText.setText(SharedApp.getLogin());

		oldPassText = (MyClearableEditText) mChangePasswordFormView
				.findViewById(R.id.oldPassText);
		newPass1Text = (MyClearableEditText) mChangePasswordFormView
				.findViewById(R.id.newPass1Text);
		newPass2Text = (MyClearableEditText) mChangePasswordFormView
				.findViewById(R.id.newPass2Text);
		mGoButton = (Button) mChangePasswordFormView
				.findViewById(R.id.goButton);
		mGoButton.setTypeface(Typeface.createFromAsset(getSherlockActivity()
				.getAssets(), "fonts/Champagne&LimousinesBold.ttf"));
		mGoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changePassword();
			}
		});

		mChangePasswordFormView.setTag("password_changer");
		return wrapper;
	}

	/**
	 * onOptionsItemSelected When the menu is toggle, the data written by the
	 * user in the field newPass1Text, newPass2Text, and oldPasstext is erased.
	 * 
	 * @see com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(android
	 *      .view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			oldPassText.setText(null);
			newPass1Text.setText(null);
			newPass2Text.setText(null);
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * The change password control logic, executed when the user presses the
	 * button.
	 */
	public void changePassword() {
		showProgress(true);
		new ChangePasswordTask().execute();
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mChangePasswordStatusView.setVisibility(View.VISIBLE);
			mChangePasswordStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mChangePasswordStatusView
									.setVisibility(show ? View.VISIBLE
											: View.GONE);
						}
					});

			mChangePasswordFormView.setVisibility(View.VISIBLE);
			mChangePasswordFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mChangePasswordFormView
									.setVisibility(show ? View.GONE
											: View.VISIBLE);
						}
					});
		} else {
			mChangePasswordStatusView.setVisibility(show ? View.VISIBLE
					: View.GONE);
			mChangePasswordFormView.setVisibility(show ? View.GONE
					: View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous change password functionality task used to
	 * authenticate the user.
	 * 
	 * @author alexisdetalhouet
	 * 
	 */
	protected class ChangePasswordTask extends
			AsyncTask<Void, Void, ChangePasswordResult> {
		@Override
		protected ChangePasswordResult doInBackground(Void... arg0) {
			return changePasswordWorker();
		}

		@Override
		protected void onPostExecute(ChangePasswordResult result) {
			super.onPostExecute(result);

			String title = null;
			String text = null;

			switch (result) {
			case ACCEPTED:
				text = getString(R.string.accepted);
				title = getString(R.string.title_success);
				break;
			case RECENTLY_ACCEPTED:
				text = getString(R.string.recently_accepted);
				title = getString(R.string.title_failure);
				break;
			case REFUSED:

				text = getString(R.string.refused);
				title = getString(R.string.title_failure);

				break;
			case UNDETERMINED:

				text = getString(R.string.undetermined);
				title = getString(R.string.title_undetermined);

				break;
			case CAS_AUTH_FAILED:

				text = getString(R.string.cas_auth_failed);
				title = getString(R.string.title_failure);

				break;
			case CAS_PROTOCOL_ERROR:

				text = getString(R.string.cas_protocol_error);
				title = getString(R.string.title_failure);

				break;
			case NETWORK_UNAVAIL:
				text = getString(R.string.error_network_unavail);
				title = getString(R.string.title_failure);

				break;
			case NETWORK_ERROR:

				text = getString(R.string.error_network_error);
				title = getString(R.string.title_failure);

				break;
			case NEWPASS_DIFFERENT:

				text = getString(R.string.error_newpass_different);
				title = getString(R.string.title_failure);

				newPass1Text.requestFocus();
				newPass1Text.selectAll();
				break;
			}
			showProgress(false);

			MyDialog d = new MyDialog(getSherlockActivity(), title, text);
			d.getExit().setVisibility(View.GONE);
			d.getSeparator().setVisibility(View.GONE);
			d.show();
		}
	}

	protected ChangePasswordResult changePasswordWorker() {
		String newPass1 = newPass1Text.getText().toString();
		String newPass2 = newPass2Text.getText().toString();
		if (!newPass1.equals(newPass2)) {
			return ChangePasswordResult.NEWPASS_DIFFERENT;
		} else {
			ConnectivityManager connMgr = (ConnectivityManager) getSherlockActivity()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo == null || !networkInfo.isConnected()) {
				return ChangePasswordResult.NETWORK_UNAVAIL;
			} else {
				EceHttpClient httpClient = new EceHttpClient(
						getSherlockActivity().getApplicationContext());
				CasClient casClient = new CasClient(httpClient,
						EcePasswordChangerFragment.casUrl);
				try {
					// authenticate to the CAS
					String serviceTicket = casClient.login(
							EcePasswordChangerFragment.serviceUrl, loginText
									.getText().toString(), oldPassText
									.getText().toString());

					// the service requires the client to GET the page before a
					// POST will be accepted : Load and throw away
					HttpGet httpGet = new HttpGet(
							EcePasswordChangerFragment.serviceUrl + "?ticket="
									+ serviceTicket);
					HttpResponse response = httpClient.execute(httpGet);
					int statusCode = response.getStatusLine().getStatusCode();

					// if the GET succeeds, proceed to the POST that changes the
					// password
					if (statusCode == HttpStatus.SC_OK) {
						// The form takes two fields : passwd and passwd1. These
						// are encoded and posted.
						HttpPost httpPost = new HttpPost(
								EcePasswordChangerFragment.serviceUrl);
						List<NameValuePair> nvps = new ArrayList<NameValuePair>();
						nvps.add(new BasicNameValuePair("passwd", new String(
								newPass1)));
						nvps.add(new BasicNameValuePair("passwd2", new String(
								newPass2)));
						httpPost.setEntity(new UrlEncodedFormEntity(nvps));
						response = httpClient.execute(httpPost);
						statusCode = response.getStatusLine().getStatusCode();
						return extractResult(response.getEntity());
					} else {
						return ChangePasswordResult.CAS_AUTH_FAILED;
					}
				} catch (CasAuthenticationException e1) {
					return ChangePasswordResult.CAS_AUTH_FAILED;
				} catch (CasProtocolException e1) {
					return ChangePasswordResult.CAS_PROTOCOL_ERROR;
				} catch (IOException e1) {
					return ChangePasswordResult.NETWORK_ERROR;
				} finally {
					httpClient.getConnectionManager().shutdown();
				}
			}
		}
	}

	/**
	 * Reads the HTTP response to an attempt to change the password looking for
	 * particular HTML strings that are parsed to calculate the server's
	 * response to the request.
	 * 
	 * TODO replace with a line by line and regex approach.
	 * 
	 * @param entity
	 *            The raw HTML response from the server
	 * @return A String value that represents the interpreted server response,
	 *         null if undetermined.
	 * @throws IOException
	 */
	private ChangePasswordResult extractResult(HttpEntity entity)
			throws IOException {
		ChangePasswordResult result = ChangePasswordResult.UNDETERMINED;
		// download the entire HTML response into a local StringBuffer
		StringBuffer htmlText = new StringBuffer("");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				entity.getContent()));
		String line = reader.readLine();
		while (line != null) {
			htmlText.append(line);
			line = reader.readLine();
		}
		// Interpret the response by looking for known Strings in the buffer
		if (htmlText.indexOf("VOUS DEVEZ ATTENDRE 5 MINUTES ENVIRON") > 0) {
			result = ChangePasswordResult.ACCEPTED;
		} else if (htmlText.indexOf("<form action=") > 0
				&& htmlText.indexOf("passwd") > 0
				&& htmlText.indexOf("passwd2") > 0) {
			result = ChangePasswordResult.REFUSED;
		} else if (htmlText
				.indexOf("Vous avez chang&eacute; votre mot de passe il y a peu de temps.") > 0) {
			result = ChangePasswordResult.RECENTLY_ACCEPTED;
		}
		return result;
	}
}
