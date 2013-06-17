package fr.alexdet.android.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import fr.alexdet.android.R;
import fr.alexdet.android.model.data.Course;
import fr.alexdet.android.model.data.CourseSection;
import fr.alexdet.android.model.webservices.CourseSectionsQuery;
import fr.alexdet.android.model.webservices.CoursesQuery;

/**
 * Course View
 * 
 * @author alexisdetalhouet
 * 
 */
public class CourseFragment extends SherlockFragment {

	/*
	 * Course Title
	 */
	private TextView mCourseTitleView;

	private ExpandableListView expandableList = null;

	/**
	 * ExpandableList group
	 * 
	 * @author alexisdetalhouet
	 * 
	 */
	public class Groupe {
		/*
		 * Group name
		 */
		private String nom;
		/*
		 * Arraylist of object that the group contain
		 */
		private ArrayList<Objet> objets;

		public Groupe(String nom) {
			super();
			this.nom = nom;
			this.objets = new ArrayList<Objet>();
		}

		public String getNom() {
			return nom;
		}

		public void setNom(String nom) {
			this.nom = nom;
		}

		public ArrayList<Objet> getObjets() {
			return objets;
		}

		public void setObjets(ArrayList<Objet> objets) {
			this.objets = objets;
		}
	}

	/**
	 * ExpandableList object
	 * 
	 * @author alexisdetalhouet
	 * 
	 */
	public class Objet {

		/*
		 * Group name where the object relies
		 */
		private Groupe groupe;
		/*
		 * Data of the object
		 */
		private String nom;

		public Objet(Groupe groupe, String nom) {
			super();
			this.groupe = groupe;
			this.nom = nom;
		}

		public Groupe getGroupe() {
			return groupe;
		}

		public void setGroupe(Groupe groupe) {
			this.groupe = groupe;
		}

		public String getNom() {
			return nom;
		}

		public void setNom(String nom) {
			this.nom = nom;
		}
	}

	/**
	 * Create the course view
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.course, container, false);

		String courseName = getArguments().getString("course_name");

		expandableList = (ExpandableListView) v
				.findViewById(R.id.expandableView);

		mCourseTitleView = (TextView) v.findViewById(R.id.TextView01);
		mCourseTitleView.setText(courseName);

		ArrayList<Groupe> listeCours = new ArrayList<Groupe>();

		// Getting all the course name
		Course thisCourse = null;
		List<Course> liste_cours = CoursesQuery.getListCourses();
		for (Course c : liste_cours) {
			if (c.getShortname().equals(courseName)) {
				thisCourse = c;
				break;
			}
		}

		// Getting all the course section per course
		List<CourseSection> coursSections = CourseSectionsQuery
				.getListCourseSections();
		for (CourseSection section : coursSections) {
			if (section.getCourseId() == thisCourse.getCourseid()) {
				Groupe groupe = new Groupe(section.getName());
				ArrayList<Objet> listeSections = new ArrayList<Objet>();
				listeSections.add(new Objet(groupe, " "
						+ Html.fromHtml(section.getSummary())));
				groupe.setObjets(listeSections);
				listeCours.add(groupe);

			}
		}

		// Create the view adapter
		MyExpandableListViewAdapter adapter = new MyExpandableListViewAdapter(
				getSherlockActivity(), listeCours);

		// Set the adapter
		expandableList.setAdapter(adapter);

		return v;
	}
}
