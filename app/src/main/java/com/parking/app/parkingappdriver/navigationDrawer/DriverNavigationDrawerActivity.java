package com.parking.app.parkingappdriver.navigationDrawer;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.WakeLocker;
import com.parking.app.parkingappdriver.currentjobs.CurrentJobsFragment;
import com.parking.app.parkingappdriver.iClasses.GlobalKeys;
import com.parking.app.parkingappdriver.myjobs.MyJobsFragment;
import com.parking.app.parkingappdriver.notification.NotificationFragment;
import com.parking.app.parkingappdriver.utils.AppUtils;
import com.parking.app.parkingappdriver.preferences.SessionManager;
import com.parking.app.parkingappdriver.utils.AppUtils;
import com.parking.app.parkingappdriver.view.UserProfileScreen;
import com.parking.app.parkingappdriver.webservices.handler.AddTokenPushAPIHandler;
import com.parking.app.parkingappdriver.webservices.handler.LogoutAPIHandler;
import com.parking.app.parkingappdriver.webservices.ihelper.WebAPIResponseListener;

import java.sql.Driver;

import static com.parking.app.parkingappdriver.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.parking.app.parkingappdriver.CommonUtilities.EXTRA_MESSAGE;
import static com.parking.app.parkingappdriver.CommonUtilities.SENDER_ID;

public class DriverNavigationDrawerActivity extends AppCompatActivity {

    private static final String TAG = "DriverNavigationDrawerActivity";
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private int mCurrentSelectedPosition;
    private View headerView;
    public static Activity mActivity;
    private AsyncTask<Void, Void, Void> mRegisterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_nav_drawer_activity);
        initViews();
        assignClickOnView();
        assignClickonNavigationMenu();

        displayView(getIntent().getIntExtra("fragmentNumber", 0));

        String pushRegistrationId = SessionManager.getInstance(mActivity)
                .getPushNotificationID();
        if (pushRegistrationId == null || pushRegistrationId.equalsIgnoreCase("")) {
            registrationPushNotification();
        }


    }


    private void addTokenHandler() {
        new AddTokenPushAPIHandler(mActivity, SessionManager.getInstance(mActivity)
                .getPushNotificationID(), "deviceID", GlobalKeys.ANDROID,
                new WebAPIResponseListener() {
                    @Override
                    public void onSuccessOfResponse(Object... arguments) {


                    }

                    @Override
                    public void onFailOfResponse(Object... arguments) {
                        AppUtils.showToast(mActivity, "Login Failed");
                    }
                }
        );
    }

    private void initViews() {

        mActivity = this;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(mActivity, mDrawerLayout,
                mToolbar, R.string.drawer_open, R.string.drawer_close);
        headerView = mNavigationView.inflateHeaderView(R.layout.drawer_header);
        TextView userName = (TextView) headerView.findViewById(R.id.txt_name);
        TextView userEmail = (TextView) headerView.findViewById(R.id.txt_age_city);
        userName.setText(mActivity.getIntent().getStringExtra("userName"));
        userEmail.setText(SessionManager.getInstance(mActivity).getEmail());
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        mDrawerToggle.syncState();
    }

    private void assignClickOnView() {


        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);

                Intent user_profile_intent = new Intent(mActivity, UserProfileScreen.class);
                startActivity(user_profile_intent);
            }
        });

    }


    private void assignClickonNavigationMenu() {

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.nav_item_my_jobs:
                        addTokenHandler();
                        displayView(0);
                        mCurrentSelectedPosition = 0;
                        return true;
                    case R.id.nav_item_current_job:
                        displayView(1);
                        mCurrentSelectedPosition = 1;
                        return true;
                    case R.id.nav_item_boooking_history:
                        displayView(2);
                        mCurrentSelectedPosition = 2;
                        return true;
                    case R.id.nav_item_notifications:
                        displayView(3);
                        mCurrentSelectedPosition = 3;
                        return true;
                    case R.id.nav_item_logout:
                        displayView(4);
                        mCurrentSelectedPosition = 4;
                        return true;


                    default:
                        return true;
                }

            }
        });

    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {

            case 0:
                fragment = new MyJobsFragment();
                title = "My Jobs";
                break;
            case 1:
                //fragment = new StartJobFragment();
                fragment = new CurrentJobsFragment();
                title = "My Current Job";
                break;
            case 2:
                // fragment = new AlertFragment();
                title = "Booking History";
                break;
            case 3:
                fragment = new NotificationFragment();
                title = "Notification";
                break;
            case 4:
                // fragment = new AlertFragment();
                new LogoutAPIHandler(mActivity, SessionManager.getInstance(mActivity).getEmail(),
                        SessionManager.getInstance(mActivity).getAuthToken(),
                        manageLogoutAPIHandler()
                );
                break;
            default:
                break;
        }


        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.body_layout, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    private WebAPIResponseListener manageLogoutAPIHandler() {
        WebAPIResponseListener responseListener = new WebAPIResponseListener() {
            @Override
            public void onSuccessOfResponse(Object... arguments) {
                AppUtils.showToast(mActivity, "You have logged out.");
            }

            @Override
            public void onFailOfResponse(Object... arguments) {
                AppUtils.showToast(mActivity, "Logout Failed");
            }
        };
        return responseListener;
    }


    // For Push notification
    private void registrationPushNotification() {
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(mActivity);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(mActivity);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        final String regId = GCMRegistrar
                .getRegistrationId(mActivity);
        SessionManager.getInstance(mActivity).setPushNotificationID(regId);
        Log.i("info", "RegId :" + regId);
        // Check if regid already presents
        if (regId.equals("")) {
            Log.i("info", "RegId :" + regId);
            // Registration is not present, register now with GCM
            GCMRegistrar.register(mActivity, SENDER_ID);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar
                    .isRegisteredOnServer(mActivity)) {
                // Skips registration.
                Log.i("info", "Already registered with GCM");
            } else {
                Log.i("info", "Not registered with GCM");
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }

    /**
     * Receiving push messages
     */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message depending upon your app
             * requirement For now i am just displaying it on the screen
             * */

            // Showing received message

            // Releasing wake lock
            WakeLocker.release();
        }
    };


    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(mActivity);
        } catch (Exception e) {
            AppUtils.showLog(TAG, "UnRegister Receiver Error " + e.getMessage());
        }
        super.onDestroy();
    }

}
