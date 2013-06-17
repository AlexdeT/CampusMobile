package fr.alexdet.android.model.data;

/**
 * Attendance object containing all the data needed for the object
 * 
 * @author alexisdetalhouet
 * 
 */
public class Attendance {

	/*
	 * ID of the item given by the SQLite database when the item is saved
	 */
	private int idLocal;

	/*
	 * Attendance data
	 */
	private String semestre;
	private String date;
	private String heureDebut;
	private String heureFin;
	private String type;

	/**
	 * Create an Attendance
	 * 
	 * @param semestre
	 * @param date
	 * @param heureDebut
	 * @param heureFin
	 * @param type
	 */
	public Attendance(String semestre, String date, String heureDebut,
			String heureFin, String type) {
		this.idLocal = -1;
		this.semestre = semestre;
		this.date = date;
		this.heureDebut = heureDebut;
		this.heureFin = heureFin;
		this.type = type;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getHeureDebut() {
		return heureDebut;
	}

	public void setHeureDebut(String heureDebut) {
		this.heureDebut = heureDebut;
	}

	public String getHeureFin() {
		return heureFin;
	}

	public void setHeureFin(String heureFin) {
		this.heureFin = heureFin;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getIdLocal() {
		return idLocal;
	}

	public void setIdLocal(int lastInsert) {
		this.idLocal = lastInsert;
	}

	/**
	 * Return the Attendance into a string
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ("le " + date + " de " + heureDebut + " a " + heureFin);
	}
}
