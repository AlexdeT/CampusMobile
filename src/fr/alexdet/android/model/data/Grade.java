package fr.alexdet.android.model.data;

/**
 * Grade object containing all the data needed for the object
 * 
 * @author alexisdetalhouet
 * 
 */
public class Grade {

	/*
	 * ID of the item given by the SQLite database when the item is saved
	 */
	private int idLocal;

	private String modu;
	private String intitule;
	private String semestre;
	private String note;
	private String moyenneClasse;

	/**
	 * Create a Grade
	 * 
	 * @param modu
	 * @param intitule
	 * @param semestre
	 * @param note
	 * @param moyenneClasse
	 */
	public Grade(String modu, String intitule, String semestre, String note,
			String moyenneClasse) {
		this.idLocal = -1;
		this.modu = modu;
		this.intitule = intitule.substring(0, 15);
		this.semestre = semestre;
		this.note = note;
		this.moyenneClasse = moyenneClasse;
	}

	public String getModu() {
		return modu;
	}

	public void setModu(String modu) {
		this.modu = modu;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getMoyenneClasse() {
		return moyenneClasse;
	}

	public void setMoyenneClasse(String moyenneClasse) {
		this.moyenneClasse = moyenneClasse;
	}

	public int getIdLocal() {
		return idLocal;
	}

	public void setIdLocal(int lastInsert) {
		this.idLocal = lastInsert;
	}

	/**
	 * Return the Grade into a string
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return intitule + "\n" + note;
	}
}
