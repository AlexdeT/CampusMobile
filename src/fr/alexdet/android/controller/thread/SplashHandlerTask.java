package fr.alexdet.android.controller.thread;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import fr.alexdet.android.R;
import fr.alexdet.android.controller.LoginActivity;
import fr.alexdet.android.controller.MainActivity;
import fr.alexdet.android.controller.SplashActivity;

/**
* This class handle the splash screen process. When the user lunch the application, the {@link SplashActivity} is started.
* It executes this task, which defines whether the {@link LoginActivity} or the {@link MainActivity} should
* be started; depending on the user's presents in the {@link SQLightDatabase}.
*
*@author alexisdetalhouet
*/
public class SplashHandlerTask extends AsyncTask<Void, Void, Message> {

	/**
	* String use to debug
	*/
	private static final String TAG = "SplashHandlerTask";

	/**
	* Use to save the notification bundle if exists
	*/
	private static Bundle notificationBundle = null;

	/**
	* The activity that lunches this task
	*/
	private static SplashActivity mActivity;

	/**
	* Constructor saving the parent activity ({@link SplashActivity})
	*@param activity activity that lunches the task
	*/
	public SplashHandlerTask(SplashActivity activity) {
		mActivity = activity;
	}

	/**
	 * Handler that finish this activity and to start the {@link LoginActivity} or
	 * the {@link MainActivity} depending on the msg received.
	 */
	private static Handler splashHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == SplashActivity.LOGINACTIVITY) {

				final Intent intent = new Intent(mActivity, LoginActivity.class);
				startActivity(intent, SplashActivity.LOGINACTIVITY);
				Log.v(TAG, "start LoginActivity");

			} else if (msg.what == SplashActivity.MAINACTIVITY) {

				final Intent intent = new Intent(mActivity, MainActivity.class);

				if (notificationBundle != null) {
					mActivity
							.deleteNotificationFromNotificationCenter(notificationBundle
									.getInt("id"));
					intent.putExtra("notifID", notificationBundle);
				}
				intent.putExtra("isFirstTime", false);

				startActivity(intent, SplashActivity.MAINACTIVITY);

				Log.i(TAG,
						"start MainActivity, with Notification ?  "
								+ (notificationBundle != null ? notificationBundle
										.toString() : null));
			}

			super.handleMessage(msg);
		}
	
		/**
		*The method lunch an activity 
		*
		*@param i intent to be started
		*@param key key containing the result
		*/
		private void startActivity(Intent i, int key) {
			mActivity.startActivityForResult(i, key);
			mActivity.finish();
			mActivity.overridePendingTransition(R.anim.mainfadein,
					R.anim.splashfadeout);
		}
	};

	@Override
	protected Message doInBackground(Void... arg0) {

		Boolean isFirstLogin = mActivity.checkUser();

		// if the application has been lunch before the user click on a
		// notification, we get the notification information saved in the
		// PendingIntent declared in the MyNotification class
		notificationBundle = mActivity.getIntent().getBundleExtra("notifID");

		final Message msg = new Message();

		// it means the user is already logged in the app, and he has an
		// active GCM registration ID
		if (notificationBundle != null)
			msg.what = SplashActivity.MAINACTIVITY;
		else if (isFirstLogin)
			msg.what = SplashActivity.MAINACTIVITY;
		else
			msg.what = SplashActivity.LOGINACTIVITY;

		// splashHandler.sendMessage(msg);
		return msg;
	}

	@Override
	protected void onPostExecute(Message msg) {
		super.onPostExecute(msg);
		splashHandler.sendMessage(msg);
	}

}
