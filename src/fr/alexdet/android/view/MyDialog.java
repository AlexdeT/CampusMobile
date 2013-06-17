package fr.alexdet.android.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import fr.alexdet.android.R;

/**
 * 
 * A custom dialog box specified for MyECEParis
 * 
 * @author alexisdetalhouet
 * 
 */
public class MyDialog extends Dialog {

	/*
	 * Dialog title
	 */
	private TextView mTitle;
	/*
	 * Dialog content
	 */
	private TextView mContent;

	private Button bDismiss;
	private Button bExit;

	private TextView separator;

	/**
	 * Create a new Dialog view
	 * 
	 * @param activity
	 *            the activity in which the Dialog is created
	 * 
	 * @param title
	 *            by default is the app name is set
	 * 
	 * @param content
	 *            the content of the dialog
	 */
	public MyDialog(Activity activity, String title, String content) {

		super(activity);

		// Request a transparent background for the window in order to hide the
		// square view of the window
		// the xml view is customize in order to show a rounded corners square
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		// No title set by the Dialog default window
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the Dialog content view
		setContentView(R.layout.dialog);

		Context context = activity.getApplicationContext();

		// Dialog title
		mTitle = (TextView) findViewById(R.id.dialogTitle);
		mTitle.setText(title);

		// Content
		mContent = (TextView) findViewById(R.id.dialogContent);
		mContent.setText(content);

		// separator
		separator = (TextView) findViewById(R.id.separator);
		
		bDismiss = (Button) findViewById(R.id.buttonNo);
		bDismiss.setText(context.getString(android.R.string.ok));
		bDismiss.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		bExit = (Button) findViewById(R.id.buttonYes);
	}

	public TextView getTitle() {
		return mTitle;
	}

	public TextView getContent() {
		return mContent;
	}

	public Button getExit() {
		return bExit;
	}

	public Button getDismiss() {
		return bDismiss;
	}

	public void setExit(Button bExit) {
		this.bExit = bExit;
	}

	public TextView getSeparator() {
		return separator;
	}

}
