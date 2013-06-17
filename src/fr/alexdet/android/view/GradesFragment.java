package fr.alexdet.android.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Typeface;
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
import fr.alexdet.android.model.data.Grade;
import fr.alexdet.android.model.webservices.GradesQuery;

/**
 * Grade view
 * 
 * @author alexisdetalhouet
 * 
 */
public class GradesFragment extends SherlockFragment {

	private static SherlockFragmentActivity mActivity;

	private static View v = null;

	private static TableLayout mTableLayoutS1;
	private static TableLayout mTableLayoutS2;

	private static List<Grade> liste_notes_S1;
	private static List<Grade> liste_notes_S2;

	private static Button mSemestre1Button;
	private static Button mSemestre2Button;

	private static HashMap<String, Boolean> titleS1;
	private static HashMap<String, Boolean> titleS2;

	private static ImageView mButtonSeparator;

	private final static String SEMESTRE1 = "S1";
	private final static String SEMESTRE2 = "S2";

	public static final String FILTER_ACTION = "gradeFragment";

	@Override
	public void onResume() {
		super.onResume();
		if (MainActivity.isSyncActive) {
			MainActivity.mIntentFilter.addAction(FILTER_ACTION);
			mActivity.registerReceiver(MainActivity.mBroadcastReceiver,
					MainActivity.mIntentFilter);
			LeftSlideMenu.isBroadcastRegistered = true;
			Log.v("GradeFragment", "registerReceiver");
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
		Log.v("GradeFragment", "onCreateView");

		mActivity = getSherlockActivity();

		v = inflater.inflate(R.layout.grades, container, false);
		if (!MainActivity.isSyncActive)
			setView();
		else {
			v.findViewById(R.id.loading_status).setVisibility(View.VISIBLE);
			v.findViewById(R.id.gradeTableLayout).setVisibility(View.GONE);
		}
		v.setTag("gradefragment");
		return v;
	}

	/**
	 * Create the view with the data
	 * 
	 * The view is adapted depending on the data we have
	 */
	public static void setView() {

		Log.v("GradeFragment", "setView");

		v.findViewById(R.id.loading_status).setVisibility(View.GONE);
		v.findViewById(R.id.gradeTableLayout).setVisibility(View.VISIBLE);

		mButtonSeparator = (ImageView) v.findViewById(R.id.buttonSeparator);

		mTableLayoutS1 = (TableLayout) v.findViewById(R.id.tableLayoutS1);
		mTableLayoutS2 = (TableLayout) v.findViewById(R.id.tableLayoutS2);

		mSemestre1Button = (Button) v.findViewById(R.id.button1);
		mSemestre2Button = (Button) v.findViewById(R.id.button2);

		orderAttendancePerSemestre();

		if (!liste_notes_S1.isEmpty()) {
			mTableLayoutS2.setVisibility(View.GONE);

			if (liste_notes_S2.isEmpty()) {
				mSemestre2Button.setVisibility(View.GONE);
				mButtonSeparator.setVisibility(View.GONE);
			}

			mSemestre1Button.setTextColor(mActivity.getResources().getColor(
					android.R.color.white));

			titleS1 = new HashMap<String, Boolean>();

			setTableLayout(v, liste_notes_S1, mTableLayoutS1, titleS1);
		}
		if (!liste_notes_S2.isEmpty()) {

			if (liste_notes_S1.isEmpty()) {
				mTableLayoutS1.setVisibility(View.GONE);
				mSemestre1Button.setVisibility(View.GONE);
				mButtonSeparator.setVisibility(View.GONE);

			}

			mSemestre2Button.setTextColor(mActivity.getResources().getColor(
					android.R.color.black));

			titleS2 = new HashMap<String, Boolean>();

			setTableLayout(v, liste_notes_S2, mTableLayoutS2, titleS2);

		}
		if (!liste_notes_S1.isEmpty() && !liste_notes_S2.isEmpty()) {
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
		if (liste_notes_S1.isEmpty() && liste_notes_S2.isEmpty()) {
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

		List<Grade> liste_notes = GradesQuery.getListGrades();

		liste_notes_S1 = new ArrayList<Grade>();
		liste_notes_S2 = new ArrayList<Grade>();

		for (Grade uneNote : liste_notes) {
			if (uneNote.getSemestre().equals(SEMESTRE1))
				liste_notes_S1.add(uneNote);
			else if (uneNote.getSemestre().equals(SEMESTRE2))
				liste_notes_S2.add(uneNote);
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
	private static void setTableLayout(View v, List<Grade> liste,
			TableLayout table, HashMap<String, Boolean> title) {

		for (int i = 0; i < liste.size(); i++) {

			Grade uneNote = liste.get(i);

			// cours description
			if (title.get(uneNote.getModu()) == null) {
				title.put(uneNote.getModu(), true);
				TableRow tTitle = new TableRow(
						mActivity.getApplicationContext());
				tTitle.setLayoutParams(new TableRow.LayoutParams(
						TableRow.LayoutParams.MATCH_PARENT,
						TableRow.LayoutParams.WRAP_CONTENT));

				TextView dtText = new TextView(
						mActivity.getApplicationContext());
				dtText.setGravity(Gravity.CENTER);
				dtText.setLines(1);
				dtText.setTextSize(17);
				dtText.setTextColor(mActivity.getResources().getColor(
						android.R.color.black));
				dtText.setBackgroundResource(R.drawable.cell_course_title);
				dtText.setTypeface(null, Typeface.BOLD);
				dtText.setText(uneNote.getModu());

				table.addView(dtText, new TableLayout.LayoutParams(
						TableLayout.LayoutParams.MATCH_PARENT,
						TableLayout.LayoutParams.WRAP_CONTENT));
			}

			// Row with Intitule - Note - Moyenne
			TableRow tr = new TableRow(mActivity.getApplicationContext());
			tr.setLayoutParams(new TableRow.LayoutParams(
					TableRow.LayoutParams.MATCH_PARENT,
					TableRow.LayoutParams.WRAP_CONTENT));

			if (i == 0) {
				tr.setPadding(0, -2, 0, 0);
			}
			// intitule
			TextView d = new TextView(mActivity.getApplicationContext());
			d.setGravity(Gravity.CENTER);
			d.setLines(2);
			d.setTextColor(mActivity.getResources().getColor(
					android.R.color.black));
			d.setBackgroundResource(R.drawable.cell_data);
			d.setText(uneNote.getIntitule());

			// note
			TextView s = new TextView(mActivity.getApplicationContext());
			s.setGravity(Gravity.CENTER);
			s.setLines(2);
			s.setTextColor(mActivity.getResources().getColor(
					android.R.color.black));
			s.setBackgroundResource(R.drawable.cell_data);
			s.setText(uneNote.getNote());
			// moyenne
			TextView e = new TextView(mActivity.getApplicationContext());
			e.setGravity(Gravity.CENTER);
			e.setBackgroundResource(R.drawable.cell_data);
			e.setTextColor(mActivity.getResources().getColor(
					android.R.color.black));
			e.setLines(2);
			e.setText(uneNote.getMoyenneClasse());

			/* Add elements to row. */
			tr.addView(d);
			tr.addView(s);
			tr.addView(e);

			/* Add row to TableLayout. */
			// tr.setBackgroundResource(R.drawable.sf_gradient_03);
			table.addView(tr, new TableLayout.LayoutParams(
					TableLayout.LayoutParams.WRAP_CONTENT,
					TableLayout.LayoutParams.MATCH_PARENT));

			if (i == liste.size() - 1) {
				TextView dtText1 = new TextView(
						mActivity.getApplicationContext());
				dtText1.setBackgroundResource(R.drawable.table_row_line_end_ab_table);

				table.addView(dtText1, new TableLayout.LayoutParams(
						TableLayout.LayoutParams.MATCH_PARENT,
						TableLayout.LayoutParams.WRAP_CONTENT));
			}
		}
	}
}
