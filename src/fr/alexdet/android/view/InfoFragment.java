package fr.alexdet.android.view;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import fr.alexdet.android.R;


/**
 * User information view
 * 
 * @author alexisdetalhouet
 * 
 */
public class InfoFragment extends SherlockFragment {

	private TableLayout mTableLayout;

	/**
	 * Create the view TODO For now the view doesn't have any user data - need
	 * the user data - field are fill with text content
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.info, container, false);
		mTableLayout = (TableLayout) v.findViewById(R.id.tableLayout);

		loadTableTitle(R.string.info_ece);
		setTableLayout(v, R.string.promotion, R.string.promotion);
		setTableLayout(v, R.string.majeure, R.string.majeure);
		setTableLayout(v, R.string.mineure, R.string.mineure);
		setTableLayout(v, R.string.va, R.string.va);
		setTableLayout(v, R.string.groupe, R.string.groupe);
		setTableLayout(v, R.string.section_inter, R.string.section_inter);

		loadTableTitle(R.string.info_admin);
		setTableLayout(v, R.string.nom, R.string.nom);
		setTableLayout(v, R.string.tel, R.string.tel);
		setTableLayout(v, R.string.email, R.string.email);
		setTableLayout(v, R.string.birth, R.string.birth);
		setTableLayout(v, R.string.nationalite, R.string.nationalite);
		setTableLayout(v, R.string.secsoc, R.string.secsoc);

		loadTableTitle(R.string.assurance);
		setTableLayout(v, R.string.assurance, R.string.assurance);
		setTableLayout(v, R.string.societe, R.string.societe);
		setTableLayout(v, R.string.dates, R.string.dates);

		v.setTag("info");
		return v;
	}

	/**
	 * Set the Row Title
	 * 
	 * @param value
	 *            resId of the value
	 */
	private void loadTableTitle(int value) {
		TableRow tr = new TableRow(getSherlockActivity()
				.getApplicationContext());
		tr.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT, getResources()
						.getDimensionPixelSize(R.dimen.tableRowTitleHeight)));
		tr.setBackgroundResource(R.drawable.table_row_info);
		tr.setGravity(Gravity.CENTER);

		TextView d = new TextView(getSherlockActivity().getApplicationContext());
		d.setGravity(Gravity.CENTER);
		d.setTextColor(getResources().getColor(android.R.color.white));
		d.setText(value);
		d.setLines(2);
		d.setTextSize(25);
		d.setTypeface(Typeface.createFromAsset(getSherlockActivity()
				.getAssets(), "fonts/Champagne&LimousinesBold.ttf"));

		tr.addView(d);

		mTableLayout.addView(tr);

	}

	/**
	 * Set the tableLayout
	 * 
	 * @param v
	 *            the root view
	 * @param titre
	 *            the title
	 * @param value
	 *            resId of the value
	 */
	private void setTableLayout(View v, int titre, int value) {

		TableRow tr = new TableRow(getSherlockActivity()
				.getApplicationContext());
		tr.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.WRAP_CONTENT));

		TextView d = new TextView(getSherlockActivity().getApplicationContext());
		d.setGravity(Gravity.LEFT);
		d.setLines(2);
		d.setPadding(8, 0, 0, 0);
		d.setTextColor(getResources().getColor(android.R.color.black));
		d.setText(titre);

		TextView s = new TextView(getSherlockActivity().getApplicationContext());
		s.setGravity(Gravity.LEFT);
		s.setLines(2);
		s.setTextColor(getResources().getColor(android.R.color.darker_gray));
		s.setText(value);

		/* Add Button to row. */
		tr.addView(d);
		tr.addView(s);

		/* Add row to TableLayout. */
		// tr.setBackgroundResource(R.drawable.sf_gradient_03);
		mTableLayout.addView(tr, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT));

	}
}
