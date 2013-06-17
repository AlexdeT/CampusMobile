package fr.alexdet.android.model.data;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Notification object containing all the data needed for the object
 * 
 * @author alexisdetalhouet
 * 
 */

public class Notification {

	/**
	 * ID of the item given by the SQLite database when the item is saved
	 */
	private int idLocal;

	/**
	 * Notification data
	 */
	private String msg;
	private String date;
	private String heure;
	private String type;

	/**
	 * Create a Notification
	 * 
	 * @param msg
	 * 
	 * @param type
	 *            type of notification
	 */
	public Notification(String msg, String type) {
		this.msg = msg;
		this.type = type;

		Date ddate = new Date();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(ddate);
		String h = calendar.get(Calendar.HOUR_OF_DAY) + " : "
				+ calendar.get(Calendar.MINUTE);
		String d = calendar.get(Calendar.DAY_OF_WEEK) + "/"
				+ calendar.get(Calendar.MONTH) + "/"
				+ calendar.get(Calendar.YEAR);

		this.date = d;
		this.heure = h;

	}

	public Notification(String m, String d, String h, String t) {
		this.msg = m;
		this.date = d;
		this.heure = h;
		this.type = t;
	}

	public int getIdLocal() {
		return idLocal;
	}

	public String getDate() {
		return this.date;
	}

	public String getHeure() {
		return this.heure;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setIdLocal(int idLocal) {
		this.idLocal = idLocal;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setHeure(String heure) {
		this.heure = heure;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * Return the Notification into a String
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return msg + "\n" + date + " - " + heure;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
