package com.parking.app.parkingappdriver.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utils of this application use static methods of application.
 */
public class AppUtils {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    /**
     * Object instance of progress bar
     */
    private static boolean ISDEBUGGING = true;
    private static ProgressDialog mProgressDialog;
    private static String TAG = AppUtils.class.getSimpleName();

    private static final String PATTERN_IP_ADDRESS = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private static final String MAC_PATTERN = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
    private static final String EMAIL_ID_PATTERN = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

    // ^ # Start of the line
    // [a-z0-9_-] # Match characters and symbols in the list, a-z, 0-9,
    // underscore, hyphen
    // {3,15} # Length at least 3 characters and maximum length of 15
    // $ # End of the line
    // ^[a-z0-9_-]{3,15}$
    private static final String USER_NAME_PATTERN = "^[a-z0-9_-]{2,20}$";

    /**
     * Show debug Message into logcat
     *
     * @param TAG
     * @param msg
     */
    public static void showLog(String TAG, String msg) {
        if (ISDEBUGGING) {
            try {
                Log.d(TAG, msg);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }

    }

    /**
     * Show debug Error Message into logcat
     *
     * @param TAG
     * @param msg
     */
    public static void showErrorLog(String TAG, String msg) {
        if (ISDEBUGGING) {
            Log.e(TAG, msg);
        }

    }

    /**
     * Show debug Verbose into logcat
     *
     * @param TAG
     * @param msg
     */
    public static void showVerboseLog(String TAG, String msg) {
        if (ISDEBUGGING) {
            Log.v(TAG, msg);
        }

    }

    /**
     * Show debug Info into logcat
     *
     * @param TAG
     * @param msg
     */
    public static void showInfoLog(String TAG, String msg) {
        if (ISDEBUGGING) {
            Log.i(TAG, msg);
        }

    }

    /**
     * Show debug Warning into logcat
     *
     * @param TAG
     * @param msg
     */
    public static void showWarningLog(String TAG, String msg) {
        if (ISDEBUGGING) {
            Log.w(TAG, msg);
        }

    }

    /**
     * Show Toast
     *
     * @param mActivity
     * @param msg
     */
    public static void showToast(final Activity mActivity, final String msg) {
        try {
            if (mActivity != null && msg != null && msg.length() > 0) {
                mActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Showing progress dialog
     *
     * @param msg
     */
    public static void showProgressDialog(final Activity mActivity,
                                          final String msg, final boolean isCancelable) {
        try {
            if (mActivity != null && mProgressDialog != null
                    && mProgressDialog.isShowing()) {
                try {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            mProgressDialog = null;
            if (mProgressDialog == null && mActivity != null) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog = new ProgressDialog(mActivity);
                        mProgressDialog.setMessage(msg);
                        mProgressDialog.setCancelable(isCancelable);
                    }
                });

            }
            if (mActivity != null && mProgressDialog != null
                    && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide progress dialog
     */
    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
                mProgressDialog = null;

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        } else {
            showErrorLog(TAG, "mProgressDialog is null");
        }
    }

    /**
     * Check device have internet connection or not
     *
     * @param activity
     * @return
     */
    public static boolean isOnline(Activity activity) {
        {
            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;

            ConnectivityManager cm = (ConnectivityManager) activity
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                    if (ni.isConnected()) {
                        haveConnectedWifi = true;
                        showInfoLog(TAG, "WIFI CONNECTION : AVAILABLE");
                    } else {
                        showInfoLog(TAG, "WIFI CONNECTION : NOT AVAILABLE");
                    }
                }
                if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                    if (ni.isConnected()) {
                        haveConnectedMobile = true;
                        showInfoLog(TAG,
                                "MOBILE INTERNET CONNECTION : AVAILABLE");
                    } else {
                        showInfoLog(TAG,
                                "MOBILE INTERNET CONNECTION : NOT AVAILABLE");
                    }
                }
            }
            return haveConnectedWifi || haveConnectedMobile;
        }

    }

    /**
     * Down Keyboard
     *
     * @param mActivity
     */
    public static void keyboardDown(Activity mActivity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) mActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(mActivity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    /**
     * Get Data & time from timestamp
     *
     * @param timeStamp
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String convertTimeStampToDate(long timeStamp) {

        try {
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date netDate = (new Date(timeStamp * 1000));
            return sdf.format(netDate);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";

        }
    }

    /**
     * Get Data & time from timestamp
     *
     * @param timeStamp
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String convertTimeStampToDateMonthFirst(long timeStamp) {

        try {
            DateFormat sdf = new SimpleDateFormat("MMMM dd,yyyy");
            Date netDate = (new Date(timeStamp * 1000));
            return sdf.format(netDate);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";

        }
    }

    /**
     * Converts string to MD5 format.
     *
     * @param string_expression : String to be converted.
     * @return
     */

    public static String convertStringToMD5(String string_expression) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(string_expression.getBytes(), 0,
                    string_expression.length());
            String hash = new BigInteger(1, digest.digest()).toString(16);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Convert Date into DD MMM formate
     *
     * @param indate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String convertDateFormteToDDMMM(String indate) {
        String dateResult = null;
        try {
            SimpleDateFormat sdf = null;
            if (indate.contains("/")) {
                sdf = new SimpleDateFormat("dd/MM/yyyy");
            } else if (indate.contains("-")) {
                sdf = new SimpleDateFormat("dd-MM-yyyy");
            }

            Date date = sdf.parse(indate);
            sdf = new SimpleDateFormat("dd MMM");
            dateResult = sdf.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return dateResult;
    }

    /**
     * Convert Time to AM/PM
     *
     * @param mtime
     * @return
     */
    @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
    public static String convertTimeFormteToAMPM(String mtime) {
        String resultTime = "00:00";
        try {
            DateFormat f1 = new SimpleDateFormat("HH:mm:ss"); // HH for hour of
            // the
            Date d = f1.parse(mtime);
            DateFormat f2 = new SimpleDateFormat("hh:mma");
            resultTime = f2.format(d).toLowerCase(); // "12:18am"
            return resultTime;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultTime;
    }

    /**
     * Make string perfect remove space from start and end
     *
     * @param Str
     * @return
     */
    public static String trimStringFromStartAndEnd(String Str) {
        Str = Str.replaceAll("^\\s+|\\s+$", "");
        return Str;

    }

    /**
     * Validate IP address
     *
     * @param ip
     * @return
     */
    public static boolean isIPAddressValidate(final String ip) {
        Pattern pattern = Pattern.compile(PATTERN_IP_ADDRESS);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * Validate MAC Address
     *
     * @param mac
     * @return
     */
    public static boolean isMACAddressValidate(final String mac) {
        Pattern pattern = Pattern.compile(MAC_PATTERN);
        Matcher matcher = pattern.matcher(mac);
        return matcher.matches();
    }

    /**
     * Validate Email Id of user
     *
     * @param emailID
     * @return
     */
    public static boolean isEmailIDValidate(String emailID) {
        Pattern pat = Pattern.compile(EMAIL_ID_PATTERN,
                Pattern.CASE_INSENSITIVE);
        Matcher match = pat.matcher(emailID);
        return match.matches();
    }

    /**
     * Validate USER Name
     *
     * @param name
     * @return
     */
    public static boolean isUserNameValidate(String name) {
        Pattern pat = Pattern.compile(USER_NAME_PATTERN,
                Pattern.CASE_INSENSITIVE);
        Matcher match = pat.matcher(name);
        return match.matches();
    }

    /**
     * Check URI is present or not
     *
     * @param mContext
     * @param mUri
     * @return
     */
    public static Uri checkUriExists(Context mContext, Uri mUri) {
        Drawable d = null;
        if (mUri != null) {
            if ("content".equals(mUri.getScheme())) {
                try {
                    d = Drawable.createFromStream(mContext.getContentResolver()
                            .openInputStream(mUri), null);
                } catch (Exception e) {
                    showWarningLog(TAG,
                            "checkUriExists -> Unable to open content: " + mUri
                                    + " Error is -> " + e);
                    mUri = null;
                }
            } else {
                d = Drawable.createFromPath(mUri.toString());
            }

            if (d == null) {
                // Invalid uri
                mUri = null;

            }
        }

        return mUri;
    }

    /**
     * Check both Array list is equal or not
     *
     * @param one
     * @param two
     * @return
     */
    public static boolean isEqualLists(ArrayList<String> one,
                                       ArrayList<String> two) {
        if (one == null && two == null) {
            return true;
        }

        if ((one == null && two != null) || one != null && two == null
                || one.size() != two.size()) {
            return false;
        }

        // to avoid messing the order of the lists we will use a copy
        // as noted in comments by A. R. S.
        one = new ArrayList<String>(one);
        two = new ArrayList<String>(two);

        Collections.sort(one);
        Collections.sort(two);
        return one.equals(two);
    }

    /**
     * to convert string to hexadecimal
     *
     * @param row
     * @return
     */
    public static String convertInHex(String row) {
        Float abc;
        int pqr;
        String final_row = "";
        // TODO Auto-generated method stub
        if (row != "" && row != null) {
            abc = Float.parseFloat(row);
            pqr = (int) Math.round(abc);
            final_row = Integer.toHexString(pqr);
        }
        return final_row;

    }

    /**
     * to get id of relative layout by passing string
     *
     * @param resourceName
     * @param c
     * @return
     */
    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }

    public static Date convertInUTCDate(String dateString) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
        //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = formatter.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convertoUTCFormat(Date date) {

        String format = DATE_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);

    }

    public static AlertDialog showDialog(Context ctx, String title, String msg) {

        return showDialog(ctx, title, msg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });

    }


    public static AlertDialog showDialog(Context ctx, String title, String msg,
                                         DialogInterface.OnClickListener listener) {

        return showDialog(ctx, title, msg, "Ok", null, listener, null);
    }


    public static AlertDialog showDialog(Context ctx, String title, String msg,
                                         String btn1, String btn2, DialogInterface.OnClickListener listener) {

        return showDialog(ctx, title, msg, btn1, btn2, listener,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

    }


    public static AlertDialog showDialog(Context ctx, String title, String msg,
                                         String btn1, String btn2,
                                         DialogInterface.OnClickListener listener1,
                                         DialogInterface.OnClickListener listener2) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(msg).setCancelable(false)
                .setPositiveButton(btn1, listener1);
        if (btn2 != null)
            builder.setNegativeButton(btn2, listener2);

        AlertDialog alert = builder.create();
        alert.show();
        return alert;

    }

    public static String getWebServiceErrorCode(JSONObject json) {
        try {
            return json.getString("errorCode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getWebServiceErrorMsg(JSONObject json) {
        try {
            return json.getString("errMsg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
