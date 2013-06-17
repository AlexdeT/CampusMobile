package fr.alexdet.android.model.bdd;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import fr.alexdet.android.model.data.Notification;

/**
 * NotificationRepository is used to store the notification data in the SQLite
 * database
 * 
 * @author alexisdetalhouet
 * 
 */
public class NotificationRepository extends DataRepository<Notification> {

	public NotificationRepository(Context context) {
		sqLiteOpenHelper = new MySQLiteOpenHelper(context, null);
	}

	@Override
	public List<Notification> GetAll() {
		this.open();
		Cursor cursor = maBDD.query(MySQLiteOpenHelper.TABLE_NOTIFICATION,
				null, null, null, null, null, null);
		return ConvertCursorToListObject(cursor);
	}

	@Override
	public Notification GetById(int id) {
		Cursor cursor = maBDD.query(MySQLiteOpenHelper.TABLE_NOTIFICATION,
				new String[] { MySQLiteOpenHelper.COL_ID,
						MySQLiteOpenHelper.COL_NOTIFICATION_MESSAGE,
						MySQLiteOpenHelper.COL_NOTIFICATION_DATE,
						MySQLiteOpenHelper.COL_NOTIFICATION_HOUR,
						MySQLiteOpenHelper.COL_NOTIFICATION_TYPE },
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		return ConvertCursorToOneObject(cursor);
	}

	@Override
	public void Save(Notification entite) {

		ContentValues contentValues = new ContentValues();
		contentValues.put(MySQLiteOpenHelper.COL_NOTIFICATION_MESSAGE,
				entite.getMsg());
		contentValues.put(MySQLiteOpenHelper.COL_NOTIFICATION_DATE,
				entite.getDate());
		contentValues.put(MySQLiteOpenHelper.COL_NOTIFICATION_HOUR,
				entite.getHeure());
		contentValues.put(MySQLiteOpenHelper.COL_NOTIFICATION_TYPE,
				entite.getType());

		this.open();
		Integer lastInsert = (int) maBDD.insert(
				MySQLiteOpenHelper.TABLE_NOTIFICATION, null, contentValues);
		close();

		if (lastInsert > 0)
			entite.setIdLocal((int) lastInsert);
		else
			Log.i("SAVE ITEM FAIL", lastInsert + "");

	}

	@Override
	public void Update(Notification entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MySQLiteOpenHelper.COL_NOTIFICATION_MESSAGE,
				entite.getMsg());
		contentValues.put(MySQLiteOpenHelper.COL_NOTIFICATION_DATE,
				entite.getDate());
		contentValues.put(MySQLiteOpenHelper.COL_NOTIFICATION_HOUR,
				entite.getHeure());
		contentValues.put(MySQLiteOpenHelper.COL_NOTIFICATION_TYPE,
				entite.getType());

		this.open();
		maBDD.update(MySQLiteOpenHelper.TABLE_NOTIFICATION, contentValues,
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(entite.getIdLocal()) });
		this.close();
	}

	@Override
	public void DeleteById(int id) {
		maBDD.delete(MySQLiteOpenHelper.TABLE_NOTIFICATION,
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(id) });
	}

	@Override
	public void Delete(Notification entite) {
		Log.i("LOC_ID_BDD", entite.getIdLocal() + "");
		maBDD.delete(MySQLiteOpenHelper.TABLE_NOTIFICATION,
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(entite.getIdLocal()) });
	}

	@Override
	public List<Notification> ConvertCursorToListObject(Cursor c) {
		List<Notification> liste = new ArrayList<Notification>();

		if (c.getCount() == 0)
			return liste;
		c.moveToFirst();

		do {
			Notification notif = ConvertCursorToObject(c);
			liste.add(notif);
		} while (c.moveToNext());

		c.close();
		close();
		return liste;
	}

	@Override
	public Notification ConvertCursorToObject(Cursor c) {
		Notification notification = new Notification(
				c.getString(MySQLiteOpenHelper.NUM_COL_NOTIFICATION_MESSAGE),
				c.getString(MySQLiteOpenHelper.NUM_COL_NOTIFICATION_DATE),
				c.getString(MySQLiteOpenHelper.NUM_COL_NOTIFICATION_HOUR),
				c.getString(MySQLiteOpenHelper.NUM_COL_NOTIFICATION_TYPE));
		return notification;
	}

	@Override
	public Notification ConvertCursorToOneObject(Cursor c) {
		c.moveToFirst();
		Notification notif = ConvertCursorToObject(c);
		c.close();
		return notif;
	}

}
