package fr.alexdet.android.model.bdd;

import java.util.List;

import android.database.Cursor;


/**
 * Interface containing all the functions needed to the use of the database
 * 
 * @author alexisdetalhouet
 * 
 * @param <T>
 *            is the type of data you want to store
 */
interface IDataRepository<T> {

	/**
	 * Get all the item
	 * 
	 * @return a list of all the data
	 */
	public List<T> GetAll();

	/**
	 * Get the item per id
	 * 
	 * @param id
	 *            id to retrieve the data
	 * @return the item retrieved
	 */
	public T GetById(int id);

	/**
	 * Save an item
	 * 
	 * @param entite
	 *            the item to save
	 */
	public void Save(T entite);

	/**
	 * Update an item
	 * 
	 * @param entite
	 *            the item to update
	 */
	public void Update(T entite);

	/**
	 * Delete an item by id
	 * 
	 * @param id
	 *            id of the item to delete
	 */
	public void DeleteById(int id);

	/**
	 * Delete an item
	 * 
	 * @param entite
	 *            the item to delete
	 */
	public void Delete(T entite);

	/**
	 * Convert the SQLite object
	 * 
	 * @param c
	 *            the actual cursor that need to be converted
	 * @return the list of item
	 */
	public List<T> ConvertCursorToListObject(Cursor c);

	/**
	 * Convert the SQLite object
	 * 
	 * @param c
	 *            the actual cursor that need to be converted
	 * @return the item
	 */
	public T ConvertCursorToObject(Cursor c);

	/**
	 * Convert the SQLite object
	 * 
	 * @param c
	 *            the actual cursor that need to be converted
	 * @return the item
	 */
	public T ConvertCursorToOneObject(Cursor c);

}