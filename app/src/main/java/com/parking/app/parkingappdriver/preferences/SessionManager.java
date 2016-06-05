package com.parking.app.parkingappdriver.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
    // Shared Preferences
    private SharedPreferences pref;
    // Editor for Shared preferences
    private Editor editor;
    // Context
    private Context mContext;
    // make private static instance of Sessionmanager class
    private static SessionManager sessionManager;

    /**
     * getInstance method is used to initialize SessionManager singelton
     * instance
     *
     * @param context context instance
     * @return Singelton session manager instance
     */
    public static SessionManager getInstance(Context context) {
        if (sessionManager == null) {
            sessionManager = new SessionManager(context);
        }
        return sessionManager;
    }

    // Constructor
    @SuppressLint("CommitPrefEdits")
    private SessionManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PreferenceHelper.PREFERENCE_NAME,
                PreferenceHelper.PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String email, String password, String auth_token,
                                   String user_name, String userId) {
        // Storing login value as TRUE
        editor.putBoolean(PreferenceHelper.IS_LOGIN, true);
        // Storing email id in pref
        editor.putString(PreferenceHelper.KEY_EMAIL_ID, email);
        editor.putString(PreferenceHelper.KEY_PWD, password);
        editor.putString(PreferenceHelper.KEY_AUTHTOKEN, auth_token);
        editor.putString(PreferenceHelper.KEY_USER_NAME, user_name);
        editor.putString(PreferenceHelper.KEY_USER_ID, userId);

        // commit changes
        editor.commit();
    }

    /**
     * Clear session details
     */
    public void clearSession() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    /**
     * �����* Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(PreferenceHelper.IS_LOGIN, false);
    }

    public String getEmail() {
        return pref.getString(PreferenceHelper.KEY_EMAIL_ID, " ");

    }

    public String getPwd() {
        return pref.getString(PreferenceHelper.KEY_PWD, " ");
    }

    public String getAuthToken() {
        return pref.getString(PreferenceHelper.KEY_AUTHTOKEN, "auth");

    }

    public void setVallet_Id(String id) {
        editor.putString(PreferenceHelper.VALLET_ID, id);
        editor.commit();
    }

    public String getVallet_Id() {
        return pref.getString(PreferenceHelper.VALLET_ID, "");
    }

    public void setValletNumber(String valletNumber) {
        editor.putString(PreferenceHelper.VALLET_NUMBER, valletNumber);
        editor.commit();
    }

    public String getValletNumber() {
        return pref.getString(PreferenceHelper.VALLET_NUMBER, "");
    }


    public void setPushNotificationID(String pushNotificationID) {
        editor.putString(PreferenceHelper.PUSH_NOTIFICATION_ID, pushNotificationID);
        editor.commit();
    }

    public String getPushNotificationID() {
        return pref.getString(PreferenceHelper.PUSH_NOTIFICATION_ID, "");
    }

    public String getUserId() {
        return pref.getString(PreferenceHelper.KEY_USER_ID, "");
    }


    public void setUserId(String userId) {
        editor.putString(PreferenceHelper.KEY_USER_ID, userId);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString(PreferenceHelper.KEY_USER_NAME, "");
    }


    public void setUserName(String userName) {
        editor.putString(PreferenceHelper.KEY_USER_NAME, userName);
        editor.commit();
    }

}
