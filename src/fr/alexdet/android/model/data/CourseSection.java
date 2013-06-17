package fr.alexdet.android.model.data;

/**
 * CourseSection object containing all the data needed for the object
 * 
 * @author alexisdetalhouet
 * 
 */
public class CourseSection {

	/*
	 * ID of the item given by the SQLite database when the item is saved
	 */
	private int idLocal;

	/*
	 * CourseSection data
	 */
	private int id;
	private int course;
	private String name;
	private String summary;

	/**
	 * Create a CourseSection
	 * 
	 * @param idLocal
	 * @param id
	 * @param course
	 * @param name
	 * @param summary
	 */
	public CourseSection(int id, int course, String name, String summary) {
		super();
		this.idLocal = -1;
		this.id = id;
		this.course = course;
		this.name = name;
		this.summary = summary;
	}

	public int getIdLocal() {
		return idLocal;
	}

	public void setIdLocal(int idLocal) {
		this.idLocal = idLocal;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCourseId() {
		return course;
	}

	public void setCourse(int course) {
		this.course = course;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary.toString();
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

}
