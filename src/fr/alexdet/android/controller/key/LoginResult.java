package fr.alexdet.android.controller.key;

/**
 * Enum containing all type of result from the login process
 */
public enum LoginResult {

	/**
	 * CAS protocol error
	 */
	CAS_PROTOCOL_ERROR,
	
	/**
	 * Login and/or password are/is wrong
	 */
	CAS_AUTH_FAILED,
	
	/**
	 * Network error
	 */
	NETWORK_ERROR,
	
	/**
	 * Network unavailable
	 */
	NETWORK_UNAVAIL;
}
