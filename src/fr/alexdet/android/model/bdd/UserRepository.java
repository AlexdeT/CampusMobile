package fr.alexdet.android.model.bdd;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import fr.alexdet.android.model.data.User;

/**
 * UserRepository is used to store the user login in the SQLite database
 * 
 * @author alexisdetalhouet
 * 
 */
public class UserRepository extends DataRepository<User> {

	public UserRepository(Context context) {
		sqLiteOpenHelper = new MySQLiteOpenHelper(context, null);
	}

	@Override
	public List<User> GetAll() {
		Cursor cursor = maBDD.query(MySQLiteOpenHelper.TABLE_USERS, null, null,
				null, null, null, null);
		return ConvertCursorToListObject(cursor);
	}

	@Override
	public User GetById(int id) {
		Cursor cursor = maBDD.query(MySQLiteOpenHelper.TABLE_USERS,
				new String[] { MySQLiteOpenHelper.COL_ID,
						MySQLiteOpenHelper.COL_USER_LOGIN },
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		return ConvertCursorToOneObject(cursor);
	}

	@Override
	public void Save(User entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MySQLiteOpenHelper.COL_USER_LOGIN, entite.getLogin());

		Integer lastInsert = (int) maBDD.insert(MySQLiteOpenHelper.TABLE_USERS,
				null, contentValues);

		if (lastInsert > 0)
			entite.setIdLocal((int) lastInsert);
		else
			Log.i("SAVE ITEM FAIL", lastInsert + "");

	}

	@Override
	public void Update(User entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MySQLiteOpenHelper.COL_USER_LOGIN, entite.getLogin());

		open();
		maBDD.update(MySQLiteOpenHelper.TABLE_USERS, contentValues,
				MySQLiteOpenHelper.COL_ID + "=?",
				new String[] { String.valueOf(entite.getIdLocal()) });
		close();
	}

	@Override
	public void DeleteById(int id) {
		maBDD.delete(MySQLiteOpenHelper.TABLE_USERS, MySQLiteOpenHelper.COL_ID
				+ "=?", new String[] { String.valueOf(id) });
	}

	@Override
	public void Delete(User entite) {
		Log.i("LOC_ID_BDD", entite.getIdLocal() + "");
		maBDD.delete(MySQLiteOpenHelper.TABLE_USERS, MySQLiteOpenHelper.COL_ID
				+ "=?", new String[] { String.valueOf(entite.getIdLocal()) });
	}

	@Override
	public List<User> ConvertCursorToListObject(Cursor c) {
		List<User> liste = null;

		if (c.getCount() == 0)
			return liste;
		else
			liste = new ArrayList<User>();

		c.moveToFirst();

		do {
			User user = ConvertCursorToObject(c);
			liste.add(user);
		} while (c.moveToNext());

		c.close();

		return liste;
	}

	@Override
	public User ConvertCursorToObject(Cursor c) {
		User user = new User(c.getString(MySQLiteOpenHelper.NUM_COL_USER_LOGIN));
		return user;
	}

	@Override
	public User ConvertCursorToOneObject(Cursor c) {
		c.moveToFirst();
		User user = ConvertCursorToObject(c);
		c.close();
		return user;
	}

}
