package com.apposit.training.video.rental.security;



/**
 * This interface defines the functions that are used to manage the security
 * configuration
 *
 */
public interface SecurityService {
	
	/**
	 * Gets the current user's username
	 * @return
	 */
	public String getUsername();
	
	
	/**
	 * Get the current user (if available)
	 * @return
	 */
	public Object getUser();
	
	/**
	 * Returns true if the current user has been authenticated
	 * @return
	 */
	public boolean isAuthenticated();

	/**
	 * Returns true id the current user is an anonymous authenticated user
	 * @return
	 */
	public boolean isAnonymousAuthenticated();
	
	/**
	 * Removed the current authenticated user (if any). De-authentication them
	 * @return true if a user was deAuthenticated. Return false if no-one was authenticated to begin with
	 */
	public boolean deAuthenticate();

	
	/**
	 * Returns true if the current user can access the specified resource
	 * @param uri
	 * @return
	 */
	public boolean isAuthorized(String uri);
	
	
	/**
	 * Returns true if the current user can access the specified resource and 
	 * resource value
	 * @param uri
	 * @param value
	 * @return
	 */
	public boolean isAuthorized(String uri, String value);
	
	/**
	 * Returns true if the supplied resource is secured
	 * @param uri
	 * @return
	 */
	public boolean isSecured(String uri);

	/**
	 * Encode a password using a configured encoding scheme and the provided encryption 'salt'
	 * @param password
	 * @param salt
	 * @return
	 */
	public String encodePassword(String password, String salt);

	/**
	 * Check an encoded password against a supplied string for a match using a configured encryption scheme and the provided encryption 'salt'
	 * @param encodedPassword
	 * @param rawPass
	 * @param salt
	 * @return
	 */
	public boolean isPasswordValid(String encodedPassword, String rawPass, String salt);
	
	/**
	 * Check if a new password is valid per the password policies
	 * @param password
	 * @return
	 */
	public boolean isNewPasswordValid(String password);
	
	/**
	 * Check if a new pin number is valid per the pin policies
	 * @param pin
	 * @return
	 */
	public boolean isNewPinValid(String pin);

	/**
	 * Generate a randomized string of the provided length
	 * @param passwordLength
	 * @return
	 */
	public String generateRandomPassword(Integer passwordLength);
	
	/**
	 *  at least 4 characters,
	 *  at least one uppercase,
     *  at least one lowercase,
     *  at least one number,
     *  at least one symbol #$%=:?
	 * @param passwordLength
	 * @return
	 */
	public String generateRandomPasswordWithSymbols(Integer passwordLength);
}
