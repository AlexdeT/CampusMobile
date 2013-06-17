package fr.alexdet.android.model.bdd;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import fr.alexdet.android.model.data.Attendance;

/**
 * AttendanceRepository is used to store the attendance data in the SQLite
 * database
 * 
 * @author alexisdetalhouet
 * 
 */
public class AttendanceRepository extends DataRepository<Attendance> {

	public AttendanceRepository(Context context) {
		sqLiteOpenHelper = new MySQLiteOpenHelper(context, null);
	}

	@Override
	public List<Attendance> GetAll() {
		Cursor cursor = maBDD.query(MySQLiteOpenHelper.TABLE_ATTENDANCES, null,
				null, null, null, null, null);
		return ConvertCursorToListObject(cursor);
	}

	@Override
	public Attendance GetById(int id) {
		Cursor cursor = maBDD.query(MySQLiteOpenHelper.TABLE_ATTENDANCES,
				new String[] { MySQLiteOpenHelper.COL_ID,
						MySQLiteOpenHelper.COL_ATTENDANCE_SEMESTER,
						MySQLiteOpenHelper.COL_ATTENDANCE_DATE,
						MySQLiteOpenHelper.COL_ATTENDANCE_HEURE_DEBUT,
						MySQLiteOpenHelper.COL_ATTENDANCE_HEURE_FIN,
						MySQLiteOpenHelper.COL_ATTENDANCE_TYPE },
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		return ConvertCursorToOneObject(cursor);
	}

	@Override
	public void Save(Attendance entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MySQLiteOpenHelper.COL_ATTENDANCE_SEMESTER,
				entite.getSemestre());
		contentValues.put(MySQLiteOpenHelper.COL_ATTENDANCE_DATE,
				entite.getDate());
		contentValues.put(MySQLiteOpenHelper.COL_ATTENDANCE_HEURE_DEBUT,
				entite.getHeureDebut());
		contentValues.put(MySQLiteOpenHelper.COL_ATTENDANCE_HEURE_FIN,
				entite.getHeureFin());
		contentValues.put(MySQLiteOpenHelper.COL_ATTENDANCE_TYPE,
				entite.getType());

		Integer lastInsert = (int) maBDD.insert(
				MySQLiteOpenHelper.TABLE_ATTENDANCES, null, contentValues);

		if (lastInsert > 0)
			entite.setIdLocal((int) lastInsert);
		else
			Log.i("SAVE ITEM FAIL", lastInsert + "");

	}

	@Override
	public void Update(Attendance entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MySQLiteOpenHelper.COL_ATTENDANCE_SEMESTER,
				entite.getSemestre());
		contentValues.put(MySQLiteOpenHelper.COL_ATTENDANCE_DATE,
				entite.getDate());
		contentValues.put(MySQLiteOpenHelper.COL_ATTENDANCE_HEURE_DEBUT,
				entite.getHeureDebut());
		contentValues.put(MySQLiteOpenHelper.COL_ATTENDANCE_HEURE_FIN,
				entite.getHeureFin());
		contentValues.put(MySQLiteOpenHelper.COL_ATTENDANCE_TYPE,
				entite.getType());

		maBDD.update(MySQLiteOpenHelper.TABLE_ATTENDANCES, contentValues,
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(entite.getIdLocal()) });
	}

	@Override
	public void DeleteById(int id) {
		maBDD.delete(MySQLiteOpenHelper.TABLE_ATTENDANCES,
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(id) });
	}

	@Override
	public void Delete(Attendance entite) {
		Log.i("LOC_ID_BDD", entite.getIdLocal() + "");
		maBDD.delete(MySQLiteOpenHelper.TABLE_ATTENDANCES,
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(entite.getIdLocal()) });
	}

	@Override
	public List<Attendance> ConvertCursorToListObject(Cursor c) {
		List<Attendance> liste = new ArrayList<Attendance>();

		if (c.getCount() == 0)
			return liste;
		c.moveToFirst();

		do {
			Attendance absence = ConvertCursorToObject(c);
			liste.add(absence);
		} while (c.moveToNext());

		c.close();
		return liste;
	}

	@Override
	public Attendance ConvertCursorToObject(Cursor c) {
		Attendance absence = new Attendance(
				c.getString(MySQLiteOpenHelper.NUM_COL_ATTENDANCE_SEMESTER),
				c.getString(MySQLiteOpenHelper.NUM_COL_ATTENDANCE_DATE),
				c.getString(MySQLiteOpenHelper.NUM_COL_ATTENDANCE_HEURE_DEBUT),
				c.getString(MySQLiteOpenHelper.NUM_COL_ATTENDANCE_HEURE_FIN),
				c.getString(MySQLiteOpenHelper.NUM_COL_ATTENDANCE_TYPE));
		return absence;
	}

	@Override
	public Attendance ConvertCursorToOneObject(Cursor c) {
		c.moveToFirst();
		Attendance absence = ConvertCursorToObject(c);
		c.close();
		return absence;
	}
}
