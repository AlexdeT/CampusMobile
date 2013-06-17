package fr.alexdet.android.model.bdd;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Abstract class to manage the SQLite database
 * 
 * @author alexisdetalhouet
 * 
 * @param <T>
 *            is the type of data you want to store
 */
public abstract class DataRepository<T> implements IDataRepository<T> {

	/**
	 * My database
	 */
	protected SQLiteDatabase maBDD;
	/**
	 * My SQLite helper class
	 */
	protected SQLiteOpenHelper sqLiteOpenHelper;

	public DataRepository() {
	}

	/**
	 * Open the database
	 */
	public void open() {
		maBDD = sqLiteOpenHelper.getWritableDatabase();
	}

	/**
	 * Close the database
	 */
	public void close() {
		maBDD.close();
		sqLiteOpenHelper.close();
	}
}
