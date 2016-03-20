package com.parking.app.parkingappdriver.preferences;

/**
 * Preference Helper Class
 *
 * @author Anshuman
 */
public interface PreferenceHelper {
    // Shared Preferences file name
    String PREFERENCE_NAME = "PARKING_Preference";
    // Shared Preferences mode
    int PRIVATE_MODE = 0;
    String KEY_EMAIL_ID = "emailId";
    String KEY_AUTHTOKEN = "authentication_token";
    String IS_LOGIN = "is_login";
    String KEY_PWD = "password";
}
