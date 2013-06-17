package fr.alexdet.android.model.data;

/**
 * User object containing all the user login
 * 
 * @author alexisdetalhouet
 * 
 */
public class User {

	/*
	 * ID of the item given by the SQLite database when the item is saved
	 */
	private int idLocal;

	/*
	 * user login
	 */
	private String mLogin;

	public User(String login) {
		this.setLogin(login);
	}

	public String getLogin() {
		return mLogin;
	}

	public void setLogin(String mLogin) {
		this.mLogin = mLogin;
	}

	public void setIdLocal(int lastInsert) {
		this.idLocal = lastInsert;
	}

	public int getIdLocal() {
		return this.idLocal;
	}

}
