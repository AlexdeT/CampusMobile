package fr.alexdet.android.model.bdd;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import fr.alexdet.android.model.data.CourseSection;

/**
 * CourseSectionRepository is used to store the attendance data in the SQLite
 * database
 * 
 * @author alexisdetalhouet
 * 
 */
public class CourseSectionRepository extends DataRepository<CourseSection> {

	public CourseSectionRepository(Context context) {
		sqLiteOpenHelper = new MySQLiteOpenHelper(context, null);
	}

	@Override
	public List<CourseSection> GetAll() {
		Cursor cursor = maBDD.query(MySQLiteOpenHelper.TABLE_COURSE_SECTION,
				null, null, null, null, null, null);
		return ConvertCursorToListObject(cursor);
	}

	@Override
	public CourseSection GetById(int id) {
		Cursor cursor = maBDD.query(MySQLiteOpenHelper.TABLE_COURSES,
				new String[] { MySQLiteOpenHelper.COL_ID,
						MySQLiteOpenHelper.COL_COURSE_SECTION_ID,
						MySQLiteOpenHelper.COL_COURSE_SECTION_COURSE,
						MySQLiteOpenHelper.COL_COURSE_SECTION_NAME,
						MySQLiteOpenHelper.COL_COURSE_SECTION_SUMMARY },
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		return ConvertCursorToOneObject(cursor);
	}

	@Override
	public void Save(CourseSection entite) {

		ContentValues contentValues = new ContentValues();
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_SECTION_ID,
				entite.getId());
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_SECTION_COURSE,
				entite.getCourseId());
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_SECTION_NAME,
				entite.getName());
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_SECTION_SUMMARY,
				entite.getSummary());

		Integer lastInsert = (int) maBDD.insert(
				MySQLiteOpenHelper.TABLE_COURSE_SECTION, null, contentValues);

		if (lastInsert > 0)
			entite.setIdLocal((int) lastInsert);
		else
			Log.i("SAVE ITEM FAIL", lastInsert + "");

	}

	@Override
	public void Update(CourseSection entite) {

		ContentValues contentValues = new ContentValues();
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_SECTION_ID,
				entite.getId());
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_SECTION_COURSE,
				entite.getCourseId());
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_SECTION_NAME,
				entite.getName());
		contentValues.put(MySQLiteOpenHelper.COL_COURSE_SECTION_SUMMARY,
				entite.getSummary());

		maBDD.update(MySQLiteOpenHelper.TABLE_COURSE_SECTION, contentValues,
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(entite.getIdLocal()) });

	}

	@Override
	public void DeleteById(int id) {
		maBDD.delete(MySQLiteOpenHelper.TABLE_COURSE_SECTION,
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(id) });

	}

	@Override
	public void Delete(CourseSection entite) {
		Log.i("LOC_ID_BDD", entite.getIdLocal() + "");
		maBDD.delete(MySQLiteOpenHelper.TABLE_COURSE_SECTION,
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(entite.getIdLocal()) });

	}

	@Override
	public List<CourseSection> ConvertCursorToListObject(Cursor c) {
		List<CourseSection> liste = new ArrayList<CourseSection>();

		if (c.getCount() == 0)
			return liste;
		c.moveToFirst();

		do {
			CourseSection coursSection = ConvertCursorToObject(c);
			liste.add(coursSection);
		} while (c.moveToNext());

		c.close();
		return liste;
	}

	@Override
	public CourseSection ConvertCursorToObject(Cursor c) {
		CourseSection coursSection = new CourseSection(
				c.getInt(MySQLiteOpenHelper.NUM_COL_COURSE_SECTION_ID),
				c.getInt(MySQLiteOpenHelper.NUM_COL_COURSE_SECTION_COUSRE),
				c.getString(MySQLiteOpenHelper.NUM_COL_COURSE_SECTION_NAME),
				c.getString(MySQLiteOpenHelper.NUM_COL_COURSE_SECTION_SUMMARY));
		return coursSection;
	}

	@Override
	public CourseSection ConvertCursorToOneObject(Cursor c) {
		c.moveToFirst();
		CourseSection coursSection = ConvertCursorToObject(c);
		c.close();
		return coursSection;
	}

}
