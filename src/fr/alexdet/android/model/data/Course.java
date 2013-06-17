package fr.alexdet.android.model.data;

/**
 * Course object containing all the data needed for the object
 * 
 * @author alexisdetalhouet
 * 
 */
public class Course {

	/*
	 * ID of the item given by the SQLite database when the item is saved
	 */
	private int idLocal;

	/*
	 * Course Data
	 */
	private String username;
	private String shortname;
	private String fullname;
	private int courseid;
	private int nbNotif = 0;

	/**
	 * Create a Course
	 * 
	 * @param idLocal
	 * @param username
	 * @param shortname
	 * @param fullname
	 * @param courseid
	 */
	public Course(String username, String shortname, String fullname,
			int courseid, int nbNotif) {
		super();
		this.idLocal = -1;
		this.username = username;
		this.shortname = shortname;
		this.fullname = fullname;
		this.courseid = courseid;
		this.nbNotif = nbNotif;
	}

	public int getIdLocal() {
		return idLocal;
	}

	public void setIdLocal(int idLocal) {
		this.idLocal = idLocal;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public int getCourseid() {
		return courseid;
	}

	public void setCourseid(int courseid) {
		this.courseid = courseid;
	}

	public int getNbNotif() {
		return nbNotif;
	}

	public void setNbNotif(int nbNotif) {
		this.nbNotif = nbNotif;
	}

	@Override
	public String toString() {
		return shortname;
	}
}
