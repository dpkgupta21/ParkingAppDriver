package com.parking.app.parkingappdriver.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.customViews.CustomProgressDialog;
import com.parking.app.parkingappdriver.view.LoginScreen;

/**
 * Created by Harish on 12/19/2015.
 */
public class AppDialogs {
    private static final String TAG = AppDialogs.class.getSimpleName();
    /**
     * select country code dialog
     */

    private static Dialog mModelDialog;





    public static void forgetPasswordSuccessfulDialog(final Activity mActivity, String msg) {
        try {
            if (mModelDialog != null && mModelDialog.isShowing()) {
                mModelDialog.dismiss();
                mModelDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mActivity != null) {
            mModelDialog = new Dialog(mActivity);
            // hide to default title for Dialog
            mModelDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            // inflate the layout dialog_layout.xml and set it as
            // contentView
            LayoutInflater inflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.simple_dialog_layout, null,
                    false);
            mModelDialog.setCanceledOnTouchOutside(false);
            mModelDialog.setContentView(view);
            mModelDialog.setCancelable(false);
            mModelDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            WindowManager.LayoutParams lp = mModelDialog.getWindow().getAttributes();
            lp.dimAmount = 0.8f;
            mModelDialog.getWindow().setAttributes(lp);
            TextView headerText = (TextView) mModelDialog.findViewById(R.id.txt_header);
            TextView msgText = (TextView) mModelDialog.findViewById(R.id.txt_msg);
            msgText.setText(msg);
            headerText.setText("Message");

            Button okButton = (Button) mModelDialog.findViewById(R.id.ok_btn);

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity,
                            LoginScreen.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();

                    CustomProgressDialog.hideProgressDialog();
                }
            });
        }

        try {
            // Display the dialog
            mModelDialog.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
