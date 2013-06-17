package fr.alexdet.android.controller.key;

/**
 * Enum containing all type of result from the change password process
 */
public enum ChangePasswordResult {

	/**
	* Change succeed
	*/
	ACCEPTED, 
	
	/**
	* A change has recently been made
	*/
	RECENTLY_ACCEPTED, 
	
	/**
	* Change refused
	*/
	REFUSED, 
	
	/**
	*CAS protocol error
	*/
	CAS_PROTOCOL_ERROR, 
	
	/**
	* Change undetermined
	*/
	UNDETERMINED, 
	
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
	NETWORK_UNAVAIL, 
	
	/**
	* The passwords given are different
	*/
	NEWPASS_DIFFERENT;
}
