package fr.alexdet.android.model.bdd;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import fr.alexdet.android.model.data.Course;

/**
 * CourseRepository is used to store the attendance data in the SQLite database
 * 
 * @author alexisdetalhouet
 * 
 */
public class CourseRepository extends DataRepository<Course> {

	public CourseRepository(Context context) {
		sqLiteOpenHelper = new MySQLiteOpenHelper(context, null);
	}

	@Override
	public List<Course> GetAll() {
		Cursor cursor = maBDD.query(MySQLiteOpenHelper.TABLE_COURSES, null,
				null, null, null, null, null);
		return ConvertCursorToListObject(cursor);
	}

	@Override
	public Course GetById(int id) {
		Cursor cursor = maBDD.query(MySQLiteOpenHelper.TABLE_COURSES,
				new String[] { MySQLiteOpenHelper.COL_ID,
						MySQLiteOpenHelper.COL_COURSE_USERNAME,
						MySQLiteOpenHelper.COL_COURSE_SHORTNAME,
						MySQLiteOpenHelper.COL_COURSE_FULLNAME,
						MySQLiteOpenHelper.COL_COURSE_COURSEID,
						MySQLiteOpenHelper.COL_COURSE_NB_NOTIF },
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		return ConvertCursorToOneObject(cursor);
	}

	@Override
	public void Save(Course entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_USERNAME,
				entite.getUsername());
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_SHORTNAME,
				entite.getShortname());
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_FULLNAME,
				entite.getFullname());
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_COURSEID,
				entite.getCourseid());
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_NB_NOTIF,
				entite.getNbNotif());

		Integer lastInsert = (int) maBDD.insert(
				MySQLiteOpenHelper.TABLE_COURSES, null, contentValues);

		if (lastInsert > 0)
			entite.setIdLocal((int) lastInsert);
		else
			Log.i("SAVE ITEM FAIL", lastInsert + "");

	}

	@Override
	public void Update(Course entite) {

		ContentValues contentValues = new ContentValues();
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_USERNAME,
				entite.getUsername());
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_SHORTNAME,
				entite.getShortname());
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_FULLNAME,
				entite.getFullname());
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_COURSEID,
				entite.getCourseid());
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_NB_NOTIF,
				entite.getNbNotif());

		maBDD.update(MySQLiteOpenHelper.TABLE_COURSES, contentValues,
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(entite.getIdLocal()) });

		Log.v("CourseRepository", "update item " + entite.toString()
				+ " nbNotif: " + entite.getNbNotif());
	}

	@Override
	public void DeleteById(int id) {
		maBDD.delete(MySQLiteOpenHelper.TABLE_COURSES,
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(id) });
	}

	@Override
	public void Delete(Course entite) {
		Log.i("LOC_ID_BDD", entite.getIdLocal() + "");
		maBDD.delete(MySQLiteOpenHelper.TABLE_COURSES,
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(entite.getIdLocal()) });
	}

	@Override
	public List<Course> ConvertCursorToListObject(Cursor c) {
		List<Course> liste = new ArrayList<Course>();

		if (c.getCount() == 0)
			return liste;
		c.moveToFirst();

		do {
			Course cours = ConvertCursorToObject(c);
			liste.add(cours);
		} while (c.moveToNext());

		c.close();
		return liste;
	}

	@Override
	public Course ConvertCursorToObject(Cursor c) {
		Course cours = new Course(
				c.getString(MySQLiteOpenHelper.NUM_COL_COURSE_USERNAME),
				c.getString(MySQLiteOpenHelper.NUM_COL_COURSE_SHORTNAME),
				c.getString(MySQLiteOpenHelper.NUM_COL_COURSE_FULLNAME),
				c.getInt(MySQLiteOpenHelper.NUM_COL_COURSE_COURSEID),
				c.getInt(MySQLiteOpenHelper.NUM_COL_COURSE_COURSE_NB_NOTIF));
		return cours;
	}

	@Override
	public Course ConvertCursorToOneObject(Cursor c) {
		c.moveToFirst();
		Course cours = ConvertCursorToObject(c);
		c.close();
		return cours;
	}
}
