package fr.alexdet.android.model.bdd;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import fr.alexdet.android.model.data.Grade;

/**
 * GradeRepository is used to store the attendance data in the SQLite database
 * 
 * @author alexisdetalhouet
 * 
 */
public class GradeReposiroty extends DataRepository<Grade> {

	public GradeReposiroty(Context context) {
		sqLiteOpenHelper = new MySQLiteOpenHelper(context, null);
	}

	@Override
	public List<Grade> GetAll() {
		Cursor cursor = maBDD.query(MySQLiteOpenHelper.TABLE_GRADES, null,
				null, null, null, null, null);
		return ConvertCursorToListObject(cursor);
	}

	@Override
	public Grade GetById(int id) {
		Cursor cursor = maBDD.query(MySQLiteOpenHelper.TABLE_GRADES,
				new String[] { MySQLiteOpenHelper.COL_ID,
						MySQLiteOpenHelper.COL_GRADE_MODU,
						MySQLiteOpenHelper.COL_GRADE_INTITULE,
						MySQLiteOpenHelper.COL_GRADE_SEMESTER,
						MySQLiteOpenHelper.COL_GRADE_NOTE,
						MySQLiteOpenHelper.COL_GRADE_MOYENNE_CLASSE },
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		return ConvertCursorToOneObject(cursor);
	}

	@Override
	public void Save(Grade entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MySQLiteOpenHelper.COL_GRADE_MODU, entite.getModu());
		contentValues.put(MySQLiteOpenHelper.COL_GRADE_INTITULE,
				entite.getIntitule());
		contentValues.put(MySQLiteOpenHelper.COL_GRADE_SEMESTER,
				entite.getSemestre());
		contentValues.put(MySQLiteOpenHelper.COL_GRADE_NOTE, entite.getNote());
		contentValues.put(MySQLiteOpenHelper.COL_GRADE_MOYENNE_CLASSE,
				entite.getMoyenneClasse());

		Integer lastInsert = (int) maBDD.insert(
				MySQLiteOpenHelper.TABLE_GRADES, null, contentValues);

		if (lastInsert > 0)
			entite.setIdLocal((int) lastInsert);
		else
			Log.i("SAVE ITEM FAIL", lastInsert + "");
	}

	@Override
	public void Update(Grade entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MySQLiteOpenHelper.COL_GRADE_MODU, entite.getModu());
		contentValues.put(MySQLiteOpenHelper.COL_GRADE_INTITULE,
				entite.getIntitule());
		contentValues.put(MySQLiteOpenHelper.COL_GRADE_SEMESTER,
				entite.getSemestre());
		contentValues.put(MySQLiteOpenHelper.COL_GRADE_NOTE, entite.getNote());
		contentValues.put(MySQLiteOpenHelper.COL_GRADE_MOYENNE_CLASSE,
				entite.getMoyenneClasse());
		maBDD.update(MySQLiteOpenHelper.TABLE_GRADES, contentValues,
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(entite.getIdLocal()) });
	}

	@Override
	public void DeleteById(int id) {
		maBDD.delete(MySQLiteOpenHelper.TABLE_GRADES, MySQLiteOpenHelper.COL_ID
				+ "=?", new String[] { String.valueOf(id) });
	}

	@Override
	public void Delete(Grade entite) {
		Log.i("LOC_ID_BDD", entite.getIdLocal() + "");
		maBDD.delete(MySQLiteOpenHelper.TABLE_GRADES, MySQLiteOpenHelper.COL_ID
				+ "=?", new String[] { String.valueOf(entite.getIdLocal()) });
	}

	@Override
	public List<Grade> ConvertCursorToListObject(Cursor c) {
		List<Grade> liste = new ArrayList<Grade>();

		if (c.getCount() == 0)
			return liste;
		c.moveToFirst();

		do {
			Grade Grade = ConvertCursorToObject(c);
			liste.add(Grade);
		} while (c.moveToNext());

		c.close();
		return liste;
	}

	@Override
	public Grade ConvertCursorToObject(Cursor c) {
		Grade grade = new Grade(
				c.getString(MySQLiteOpenHelper.NUM_COL_GRADE_MODU),
				c.getString(MySQLiteOpenHelper.NUM_COL_GRADE_INTITULE),
				c.getString(MySQLiteOpenHelper.NUM_COL_GRADE_SEMESTER),
				c.getString(MySQLiteOpenHelper.NUM_COL_GRADE_NOTE),
				c.getString(MySQLiteOpenHelper.NUM_COL_GRADE_MOYENNE_CLASSE));
		return grade;
	}

	@Override
	public Grade ConvertCursorToOneObject(Cursor c) {
		c.moveToFirst();
		Grade grade = ConvertCursorToObject(c);
		c.close();
		return grade;
	}
}
