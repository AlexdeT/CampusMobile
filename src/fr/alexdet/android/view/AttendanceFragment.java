package fr.alexdet.android.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import fr.alexdet.android.R;
import fr.alexdet.android.controller.MainActivity;
import fr.alexdet.android.controller.slidemenu.LeftSlideMenu;
import fr.alexdet.android.model.data.Attendance;
import fr.alexdet.android.model.webservices.AttendanceQuery;

/**
 * Attendance view
 * 
 * @author alexisdetalhouet
 * 
 */
public class AttendanceFragment extends SherlockFragment {

	private static SherlockFragmentActivity mActivity;

	private static View v = null;

	private static TableLayout mTableLayoutS1;
	private static TableLayout mTableLayoutS2;

	private static List<Attendance> liste_absences_S1;
	private static List<Attendance> liste_absences_S2;

	private static Button mSemestre1Button;
	private static Button mSemestre2Button;

	private static ImageView mButtonSeparator;

	private final static String SEMESTRE1 = "S1";
	private final static String SEMESTRE2 = "S2";

	public static final String FILTER_ACTION = "attendanceFragment";

	@Override
	public void onResume() {
		super.onResume();
		if (MainActivity.isSyncActive) {
			MainActivity.mIntentFilter.addAction(FILTER_ACTION);
			mActivity.registerReceiver(MainActivity.mBroadcastReceiver,
					MainActivity.mIntentFilter);
			LeftSlideMenu.isBroadcastRegistered = true;
			Log.v("Attendancefragment", "registerReceiver");
		}
	}

	/**
	 * Create the view
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("Attendancefragment", "onCreateView");

		mActivity = getSherlockActivity();

		v = inflater.inflate(R.layout.attendance, container, false);

		if (!MainActivity.isSyncActive)
			setView();
		else {
			v.findViewById(R.id.loading_status).setVisibility(View.VISIBLE);
			v.findViewById(R.id.attendanceTableLayout).setVisibility(View.GONE);
		}
		v.setTag("attendancefragment");
		return v;
	}

	/**
	 * Create the view with the data
	 * 
	 * The view is adapted depending on the data we have
	 */
	public static void setView() {

		Log.v("AttendanceFragment", "setView");
		v.findViewById(R.id.loading_status).setVisibility(View.GONE);
		v.findViewById(R.id.attendanceTableLayout).setVisibility(View.VISIBLE);

		mButtonSeparator = (ImageView) v.findViewById(R.id.buttonSeparator);

		mTableLayoutS1 = (TableLayout) v.findViewById(R.id.tableLayoutS1);
		mTableLayoutS2 = (TableLayout) v.findViewById(R.id.tableLayoutS2);

		mSemestre1Button = (Button) v.findViewById(R.id.button1);
		mSemestre2Button = (Button) v.findViewById(R.id.button2);

		orderAttendancePerSemestre();

		if (!liste_absences_S1.isEmpty()) {

			mTableLayoutS2.setVisibility(View.GONE);
			if (liste_absences_S2.isEmpty()) {
				mSemestre2Button.setVisibility(View.GONE);
				mButtonSeparator.setVisibility(View.GONE);
			}

			mSemestre1Button.setTextColor(mActivity.getResources().getColor(
					android.R.color.white));

			setTableLayout(v, liste_absences_S1, mTableLayoutS1);
		}
		if (!liste_absences_S2.isEmpty()) {

			if (liste_absences_S1.isEmpty()) {
				mTableLayoutS1.setVisibility(View.GONE);
				mSemestre1Button.setVisibility(View.GONE);
				mButtonSeparator.setVisibility(View.GONE);

			}

			mSemestre2Button.setTextColor(mActivity.getResources().getColor(
					android.R.color.black));

			setTableLayout(v, liste_absences_S2, mTableLayoutS2);

		}
		if (!liste_absences_S1.isEmpty() && !liste_absences_S2.isEmpty()) {
			mSemestre1Button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					mTableLayoutS1.setVisibility(View.VISIBLE);
					mTableLayoutS2.setVisibility(View.GONE);

					mSemestre1Button.setTextColor(mActivity.getResources()
							.getColor(android.R.color.white));
					mSemestre2Button.setTextColor(mActivity.getResources()
							.getColor(android.R.color.black));

				}
			});

			mSemestre2Button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					mTableLayoutS1.setVisibility(View.GONE);
					mTableLayoutS2.setVisibility(View.VISIBLE);

					mSemestre1Button.setTextColor(mActivity.getResources()
							.getColor(android.R.color.black));
					mSemestre2Button.setTextColor(mActivity.getResources()
							.getColor(android.R.color.white));
				}
			});

		}
		if (liste_absences_S1.isEmpty() && liste_absences_S2.isEmpty()) {
			mSemestre2Button.setVisibility(View.GONE);
			mButtonSeparator.setVisibility(View.GONE);
			mTableLayoutS2.setVisibility(View.GONE);
		}
	}

	/**
	 * Order the attendance into two array, one for the first semester and once
	 * for the second semester
	 */
	private static void orderAttendancePerSemestre() {

		List<Attendance> liste_absences = AttendanceQuery.getListAttendances();

		liste_absences_S1 = new ArrayList<Attendance>();
		liste_absences_S2 = new ArrayList<Attendance>();

		if (liste_absences != null) {
			for (Attendance uneAbsence : liste_absences) {
				if (uneAbsence.getSemestre().equals(SEMESTRE1))
					liste_absences_S1.add(uneAbsence);
				else if (uneAbsence.getSemestre().equals(SEMESTRE2))
					liste_absences_S2.add(uneAbsence);
			}
		}
	}

	/**
	 * Set the tableLayout for the semester
	 * 
	 * @param v
	 *            the root view
	 * @param liste
	 *            the semester list
	 * @param table
	 *            the table that needs to be set
	 */
	private static void setTableLayout(View v, List<Attendance> liste,
			TableLayout table) {

		for (int i = 0; i < liste.size(); i++) {

			Attendance uneAbsence = liste.get(i);

			TableRow tr = new TableRow(mActivity.getApplicationContext());
			tr.setLayoutParams(new TableRow.LayoutParams(
					TableRow.LayoutParams.MATCH_PARENT,
					TableRow.LayoutParams.WRAP_CONTENT));
			tr.setBackgroundResource(R.drawable.table_row_info);
			tr.setGravity(Gravity.CENTER);

			TextView d = new TextView(mActivity.getApplicationContext());
			d.setGravity(Gravity.CENTER);
			d.setLines(2);
			d.setTextColor(mActivity.getResources().getColor(
					android.R.color.black));
			d.setBackgroundResource(R.drawable.cell_data);
			d.setText(uneAbsence.getDate());

			TextView s = new TextView(mActivity.getApplicationContext());
			s.setGravity(Gravity.CENTER);
			s.setLines(2);
			s.setTextColor(mActivity.getResources().getColor(
					android.R.color.black));
			s.setBackgroundResource(R.drawable.cell_data);
			s.setText(uneAbsence.getHeureDebut());

			TextView e = new TextView(mActivity.getApplicationContext());
			e.setGravity(Gravity.CENTER);
			e.setLines(2);
			e.setTextColor(mActivity.getResources().getColor(
					android.R.color.black));
			e.setBackgroundResource(R.drawable.cell_data);
			e.setText(uneAbsence.getHeureFin());

			TextView t = new TextView(mActivity.getApplicationContext());
			t.setGravity(Gravity.CENTER);
			t.setLines(2);
			t.setTextColor(mActivity.getResources().getColor(
					android.R.color.black));
			t.setBackgroundResource(R.drawable.cell_data);
			t.setText(uneAbsence.getType());

			tr.addView(d);
			tr.addView(s);
			tr.addView(e);
			tr.addView(t);

			table.addView(tr, new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					TableLayout.LayoutParams.WRAP_CONTENT));

			// footer's line at the end of the table
			if (i == liste.size() - 1) {
				TextView dtText = new TextView(
						mActivity.getApplicationContext());
				dtText.setBackgroundResource(R.drawable.table_row_line_end_ab_table);

				table.addView(dtText, new TableLayout.LayoutParams(
						TableLayout.LayoutParams.MATCH_PARENT,
						TableLayout.LayoutParams.WRAP_CONTENT));
			}

		}
	}
}
