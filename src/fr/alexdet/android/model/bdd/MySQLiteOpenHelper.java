package fr.alexdet.android.model.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Extends SQLiteOpenHeleper class - This class define all the tables, the row
 * and columns for a table
 * 
 * @author alexisdetalhouet
 * 
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	// Nom de la base
	private static final String DATABASE_NAME = "myeceparis.db";
	private static final int DATABASE_VERSION = 1;

	// Nom des tables
	public static final String TABLE_USERS = "table_users";
	public static final String TABLE_ATTENDANCES = "table_attendances";
	public static final String TABLE_GRADES = "table_grades";
	public static final String TABLE_COURSES = "table_cours";
	public static final String TABLE_COURSE_SECTION = "table_cours_section";
	public static final String TABLE_NOTIFICATION = "table_notifications";

	public static final String COL_ID = "id";
	public static final int NUM_COL_ID = 0;

	// USERS
	public static final String COL_USER_LOGIN = "userLogin";
	public static final int NUM_COL_USER_LOGIN = 1;

	// ATTENDANCE
	public static final String COL_ATTENDANCE_SEMESTER = "attendanceSemester";
	public static final int NUM_COL_ATTENDANCE_SEMESTER = 1;
	public static final String COL_ATTENDANCE_DATE = "attendanceDate";
	public static final int NUM_COL_ATTENDANCE_DATE = 2;
	public static final String COL_ATTENDANCE_HEURE_DEBUT = "attendanceHeureDebut";
	public static final int NUM_COL_ATTENDANCE_HEURE_DEBUT = 3;
	public static final String COL_ATTENDANCE_HEURE_FIN = "attendanceHeureFin";
	public static final int NUM_COL_ATTENDANCE_HEURE_FIN = 4;
	public static final String COL_ATTENDANCE_TYPE = "attendanceType";
	public static final int NUM_COL_ATTENDANCE_TYPE = 5;

	// GRADE
	public static final String COL_GRADE_MODU = "gradeModu";
	public static final int NUM_COL_GRADE_MODU = 1;
	public static final String COL_GRADE_INTITULE = "gradeIntitule";
	public static final int NUM_COL_GRADE_INTITULE = 2;
	public static final String COL_GRADE_SEMESTER = "gradeSemester";
	public static final int NUM_COL_GRADE_SEMESTER = 3;
	public static final String COL_GRADE_NOTE = "gradeNote";
	public static final int NUM_COL_GRADE_NOTE = 4;
	public static final String COL_GRADE_MOYENNE_CLASSE = "gradeMoyenneClasse";
	public static final int NUM_COL_GRADE_MOYENNE_CLASSE = 5;

	// COURSE
	public static final String COL_COURSE_USERNAME = "userName";
	public static final int NUM_COL_COURSE_USERNAME = 1;
	public static final String COL_COURSE_SHORTNAME = "shortName";
	public static final int NUM_COL_COURSE_SHORTNAME = 2;
	public static final String COL_COURSE_FULLNAME = "fullName";
	public static final int NUM_COL_COURSE_FULLNAME = 3;
	public static final String COL_COURSE_COURSEID = "courseId";
	public static final int NUM_COL_COURSE_COURSEID = 4;
	public static final String COL_COURSE_NB_NOTIF = "nbNotif";
	public static final int NUM_COL_COURSE_COURSE_NB_NOTIF = 5;

	// COURSE SECTION
	public static final String COL_COURSE_SECTION_ID = "courseSectionId";
	public static final int NUM_COL_COURSE_SECTION_ID = 1;
	public static final String COL_COURSE_SECTION_COURSE = "courseSectionCourse";
	public static final int NUM_COL_COURSE_SECTION_COUSRE = 2;
	public static final String COL_COURSE_SECTION_NAME = "courseSectionName";
	public static final int NUM_COL_COURSE_SECTION_NAME = 3;
	public static final String COL_COURSE_SECTION_SUMMARY = "courseSectionSummary";
	public static final int NUM_COL_COURSE_SECTION_SUMMARY = 4;

	// NOTIFICATION
	public static final String COL_NOTIFICATION_MESSAGE = "notifMsg";
	public static final int NUM_COL_NOTIFICATION_MESSAGE = 1;
	public static final String COL_NOTIFICATION_DATE = "notifDate";
	public static final int NUM_COL_NOTIFICATION_DATE = 2;
	public static final String COL_NOTIFICATION_HOUR = "notifHeure";
	public static final int NUM_COL_NOTIFICATION_HOUR = 3;
	public static final String COL_NOTIFICATION_TYPE = "notifType";
	public static final int NUM_COL_NOTIFICATION_TYPE = 4;

	private static final String CREATE_BDD_USERS = "CREATE TABLE "
			+ TABLE_USERS + " ( " + COL_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_USER_LOGIN
			+ " TEXT ); ";

	private static final String CREATE_BDD_ATTENDANCES = "CREATE TABLE "
			+ TABLE_ATTENDANCES + " ( " + COL_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_ATTENDANCE_SEMESTER
			+ " TEXT, " + COL_ATTENDANCE_DATE + " TEXT, "
			+ COL_ATTENDANCE_HEURE_DEBUT + " TEXT, " + COL_ATTENDANCE_HEURE_FIN
			+ " TEXT, " + COL_ATTENDANCE_TYPE + " TEXT ); ";

	private static final String CREATE_BDD_GRADES = "CREATE TABLE "
			+ TABLE_GRADES + " ( " + COL_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_GRADE_MODU
			+ " TEXT, " + COL_GRADE_INTITULE + " TEXT, " + COL_GRADE_SEMESTER
			+ " TEXT, " + COL_GRADE_NOTE + " TEXT, " + COL_GRADE_MOYENNE_CLASSE
			+ " TEXT ); ";

	private static final String CREATE_BDD_COURSES = "CREATE TABLE "
			+ TABLE_COURSES + " ( " + COL_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_COURSE_USERNAME
			+ " TEXT, " + COL_COURSE_SHORTNAME + " TEXT, "
			+ COL_COURSE_FULLNAME + " TEXT, " + COL_COURSE_COURSEID + " TEXT, "
			+ COL_COURSE_NB_NOTIF + " TEXT ); ";

	private static final String CREATE_BDD_COURSE_SECTION = "CREATE TABLE "
			+ TABLE_COURSE_SECTION + " ( " + COL_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_COURSE_SECTION_ID
			+ " TEXT, " + COL_COURSE_SECTION_COURSE + " TEXT, "
			+ COL_COURSE_SECTION_NAME + " TEXT, " + COL_COURSE_SECTION_SUMMARY
			+ " TEXT ); ";

	private static final String CREATE_BDD_NOTIFICATION = "CREATE TABLE "
			+ TABLE_NOTIFICATION + " ( " + COL_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOTIFICATION_MESSAGE
			+ " TEXT, " + COL_NOTIFICATION_DATE + " TEXT, "
			+ COL_NOTIFICATION_HOUR + " TEXT, " + COL_NOTIFICATION_TYPE
			+ " TEXT ); ";

	public MySQLiteOpenHelper(Context context, CursorFactory factory) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("SQL_CREATE", CREATE_BDD_USERS);
		db.execSQL(CREATE_BDD_USERS);
		Log.i("SQL_CREATE", CREATE_BDD_ATTENDANCES);
		db.execSQL(CREATE_BDD_ATTENDANCES);
		Log.i("SQL_CREATE", CREATE_BDD_GRADES);
		db.execSQL(CREATE_BDD_GRADES);
		Log.i("SQL_CREATE", CREATE_BDD_COURSES);
		db.execSQL(CREATE_BDD_COURSES);
		Log.i("SQL_CREATE", CREATE_BDD_COURSE_SECTION);
		db.execSQL(CREATE_BDD_COURSE_SECTION);
		Log.i("SQL_CREATE", CREATE_BDD_NOTIFICATION);
		db.execSQL(CREATE_BDD_NOTIFICATION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_USERS + ";");
		db.execSQL("DROP TABLE " + TABLE_ATTENDANCES + ";");
		db.execSQL("DROP TABLE " + TABLE_GRADES + ";");
		db.execSQL("DROP TABLE " + TABLE_COURSES + ";");
		db.execSQL("DROP TABLE " + TABLE_COURSE_SECTION + ";");
		db.execSQL("DROP TABLE " + TABLE_NOTIFICATION + ";");
		if (newVersion > DATABASE_VERSION) {
			onCreate(db);
		}
		onCreate(db);
	}
}