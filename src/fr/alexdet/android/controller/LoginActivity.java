package fr.alexdet.android.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import fr.alexdet.android.R;
import fr.alexdet.android.controller.key.LoginResult;
import fr.alexdet.android.controller.thread.LoginUserTask;
import fr.alexdet.android.model.bdd.UserRepository;
import fr.alexdet.android.model.data.User;
import fr.alexdet.android.view.MyClearableEditText;
import fr.alexdet.android.view.MyDialog;
import fr.ece.android.casclient.CasAuthenticationException;
import fr.ece.android.casclient.CasProtocolException;

/**
 * 
 * This class extends {@link MyActionBarActivity}. It displays the login screen.
 * This class also contain the lost password view. When trigger, the lost
 * password view is shown and the login form is hidden. During the login
 * process, the class create and execute the {@link LoginUserTask}
 * 
 * @author alexisdetalhouet
 * 
 * 
 */
public class LoginActivity extends MyActionBarActivity {

	/**
	 * String set for debug
	 */
	public static final String TAG = "LoginCAS";

	/**
	 * The activity context
	 */
	private static Context mContext;

	/**
	 * AsyncTask performing to login throu the CAS protocol
	 */
	private LoginUserTask mAuthTask = null;

	/**
	 * User login
	 */
	private String mLogin;

	/**
	 * User password
	 */
	private String mPassword;

	/**
	 * UI - Login Button
	 */
	private Button mButton;

	/**
	 * UI - Login EditText
	 */
	private MyClearableEditText mLoginEditText;

	/**
	 * UI - Password EditText
	 */
	private MyClearableEditText mPasswordEditText;

	/**
	 * UI - Loading EditText
	 */
	private TextView mLoginStatusMessageView;

	/**
	 * UI - Lost password button
	 */
	private Button mLostPassword;

	/**
	 * UI - Login View
	 */
	private static View mLoginFormView;

	/**
	 * UI - Authenticating View
	 */
	private static View mLoginStatusView;

	/**
	 * UI - Lost password View view
	 */
	private static ScrollView mLostPasswordView;

	@Override
	protected void onRestart() {
		super.onRestart();
		// set the LoadDataTask to null
		mAuthTask = null;

		// hide the loading progress bar
		showProgress(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the action bar
		setActionBar();

		// set the activity view
		setContentView(R.layout.login);

		// set the activity context
		mContext = getApplicationContext();

		// Lost password view
		mLostPasswordView = (ScrollView) findViewById(R.id.lost_password_scrollview);
		mLostPassword = (Button) findViewById(R.id.mdp_perdu);
		mLostPassword.setOnClickListener(handler);

		// Login form view
		mLoginFormView = findViewById(R.id.login_form);
		mLoginEditText = (MyClearableEditText) findViewById(R.id.login);
		mLoginEditText.setText(mLogin);
		mPasswordEditText = (MyClearableEditText) findViewById(R.id.password);
		mPasswordEditText.setText(mPassword);
		mButton = (Button) findViewById(R.id.sign_in_button);
		mButton.setTypeface(Typeface.createFromAsset(getAssets(),
				"fonts/Champagne&LimousinesBold.ttf"));
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mLoginEditText.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(mPasswordEditText.getWindowToken(),
						0);
				try {
					attemptLogin();
				} catch (CasAuthenticationException e) {
					e.printStackTrace();
				} catch (CasProtocolException e) {
					e.printStackTrace();
				}
			}
		});

		// Loading view (progress bar)
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
	}

	@Override
	protected void setActionBar() {
		// hide the left button
		mActionBarCustomNav.findViewById(R.id.leftButton).setVisibility(
				View.INVISIBLE);
		// hide right button
		mActionBarCustomNav.findViewById(R.id.rightButton).setVisibility(
				View.INVISIBLE);
	}

	/**
	 * Attempts to sign in the account specified by the login form.
	 * 
	 * 
	 * @throws CasProtocolException
	 * @throws CasAuthenticationException
	 */
	private void attemptLogin() throws CasAuthenticationException,
			CasProtocolException {
		// set the authenticating message
		mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
		// show the loading view
		showProgress(true);

		if (mAuthTask != null) {
			return;
		}

		// Store values at the time of the login attempt.
		mLogin = mLoginEditText.getText().toString();
		mPassword = mPasswordEditText.getText().toString();

		// create the authentication task
		mAuthTask = new LoginUserTask(this, mLogin, mPassword);
		// excecute the task
		mAuthTask.execute((Void) null);
	}

	/**
	 * Shows the progress UI and hides the login form.
	 * 
	 * @param show
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = mContext.getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Listenner on the lost password link. When pressed, show the lost password
	 * view
	 */
	private View.OnClickListener handler = new View.OnClickListener() {
		public void onClick(View v) {
			if (v == mLostPassword) {
				showLostPasswordView();
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// if the user pressed the back key and the lost password view is
		// visible, hide the lost password view. Else, finish the activity
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (mLostPasswordView.getVisibility() == View.VISIBLE) {
					hideLostPasswordView();
					return true;
				} else
					finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Show the lost password view when the link is clicked
	 */
	private void showLostPasswordView() {
		mLostPasswordView.setVisibility(View.VISIBLE);

		TextView title = (TextView) findViewById(R.id.titre);
		title.setText(R.string.mdp_oubliee);

		TextView mdpInstruction = (TextView) mLostPasswordView
				.findViewById(R.id.instruction);
		mdpInstruction.setText(Html
				.fromHtml(getString(R.string.mdp_instruction)));

		ImageButton backButton = (ImageButton) mActionBarCustomNav
				.findViewById(R.id.leftButton);
		backButton.setVisibility(View.VISIBLE);
		backButton.setImageBitmap(BitmapFactory.decodeResource(getResources(),
				android.R.drawable.ic_menu_revert));
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideLostPasswordView();
			}
		});
	}

	/**
	 * Hide the lost password view
	 */
	private void hideLostPasswordView() {
		// hiding the lost password view
		mLostPasswordView.setVisibility(View.GONE);
		mActionBarCustomNav.findViewById(R.id.leftButton).setVisibility(
				View.INVISIBLE);
		// setting the page title
		TextView tv = (TextView) findViewById(R.id.titre);
		tv.setText(R.string.login);
	}

	/**
	 * Save the user credentials
	 */
	private void saveUserCredentials() {
		// Create the db
		UserRepository userRepo = new UserRepository(getApplicationContext());
		// Open the created db
		userRepo.open();
		// Save the user
		userRepo.Save(new User(mLogin));
		// Close the db
		userRepo.close();
	}

	/**
	 * Start the MainActivitys
	 */
	private void startMainActivity() {
		// Create an intent with the targeted activity
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		// Create a bundle to give data to the MainActivity
		Bundle bundle = new Bundle();
		// If the user has logged, it means it is the first time he is using the
		// app, so we warm the MainActivity it's the first time in order to make
		// the adapted queries to gather the data
		bundle.putBoolean("isFirstTime", true);
		// save the bundle in the intent
		intent.putExtras(bundle);
		// start the activity
		startActivity(intent);

		Log.i("LoginActivity", "startMainActivity");

		// Create an intent to set the result before finishing the activity
		Intent i = new Intent();
		setResult(SplashActivity.LOGINACTIVITY, i);
		finish();

		// Set the animation between activity; here it is a fade in fade out
		// animation
		overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
	}

	/**
	 * If an error occurred during the login process, display the returned error
	 * in a dialog pop up window
	 * 
	 * @param result
	 *            result of the {@link LoginUserTask}
	 */
	private void printDialogResult(LoginResult result) {

		// Set the error string
		String text = null;
		switch (result) {
		case CAS_AUTH_FAILED:
			text = getString(R.string.cas_auth_failed);
			break;
		case CAS_PROTOCOL_ERROR:
			text = getString(R.string.cas_protocol_error);
			break;
		case NETWORK_UNAVAIL:
			text = getString(R.string.error_network_unavail);
			break;
		case NETWORK_ERROR:
			text = getString(R.string.error_network_error);
			break;
		}

		// Create and show the dialog
		MyDialog d = new MyDialog(LoginActivity.this,
				getString(R.string.app_name), text);
		d.getExit().setVisibility(View.GONE);
		d.getSeparator().setVisibility(View.GONE);
		d.show();
	}

	/**
	 * This method is called in the postExecute method of the
	 * {@link LoginUserTask}. Once the authentication succeeded, we save the
	 * user credential and set the {@link SharedApp} login
	 * 
	 * @param result
	 *            result of the {@link LoginUserTask}
	 */
	public void loginEnded(LoginResult result) {
		if (result == null) {
			SharedApp.setLogin(mLogin);
			saveUserCredentials();
			startMainActivity();
		} else {
			printDialogResult(result);
		}
	}
}
