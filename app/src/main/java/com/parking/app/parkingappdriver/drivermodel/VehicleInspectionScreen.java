package com.parking.app.parkingappdriver.drivermodel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.activity.BaseActivity;
import com.parking.app.parkingappdriver.captureImage.CapturePicture;
import com.parking.app.parkingappdriver.captureImage.ShowingSnapshotScreen;
import com.parking.app.parkingappdriver.model.ClearDentDTO;
import com.parking.app.parkingappdriver.model.VehicleInspectDTO;

import java.util.ArrayList;
import java.util.List;


public class VehicleInspectionScreen extends BaseActivity {

    private Toolbar mToolbar;
    private TextView toolbar_title;
    private static Activity mActivity;
    private VehicleInspectDTO inspectDTO;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_inspection_screen);
        initViews();
        assignClicks();
    }

    private void checkForPartsConditions() {
        List<ClearDentDTO> clearDentDTOs = new ArrayList<>();
        try {
            for (int i = 0; i < 6; i++) {
                ClearDentDTO clearDentDTO = new ClearDentDTO();
                if (i == 0) {
                    clearDentDTO.setPartName("rightFrontDoorDent");
                    clearDentDTO.setClearDent(isRadioChecked(R.id.rf_dent));
                }
                if (i == 1) {
                    clearDentDTO.setPartName("leftFrontDoorDent");
                    clearDentDTO.setClearDent(isRadioChecked(R.id.lf_dent));
                }
                if (i == 2) {
                    clearDentDTO.setPartName("rightBackDoorDent");
                    clearDentDTO.setClearDent(isRadioChecked(R.id.rb_dent));
                }
                if (i == 3) {
                    clearDentDTO.setPartName("leftBackDoorDent");
                    clearDentDTO.setClearDent(isRadioChecked(R.id.lb_dent));
                }
                if (i == 4) {
                    clearDentDTO.setPartName("frontBumperDent");
                    clearDentDTO.setClearDent(isRadioChecked(R.id.fb_dent));
                }
                if (i == 5) {
                    clearDentDTO.setPartName("backBumperDent");
                    clearDentDTO.setClearDent(isRadioChecked(R.id.bb_dent));
                }

                clearDentDTOs.add(clearDentDTO);
            }
            inspectDTO.setPartsCondition(clearDentDTOs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void assignClicks() {
//        setClick(R.id.rt_frnt_dr_tv);
//        setClick(R.id.lt_frnt_dr_tv);
//        setClick(R.id.rt_bck_dr_tv);
//        setClick(R.id.lt_back_dr_tv);
//        setClick(R.id.frnt_bmpr_tv);
//        setClick(R.id.back_bmpr_tv);
//        setClick(R.id.view_all_pics);
        setClick(R.id.take_vehicle_button);
    }


    private void initViews() {

        mActivity = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.vehicle_inspection));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_button);

        inspectDTO = new VehicleInspectDTO();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        Intent captureImageIntent = new Intent(mActivity, CapturePicture.class);

        switch (v.getId()) {
            case R.id.take_vehicle_button:
                //showMessageChooseDialog();
                checkForPartsConditions();
                Intent inspectIntent = new Intent(mActivity, VehicleInspect.class);
                inspectIntent.putExtra("InspectDTO", inspectDTO);
                startActivity(inspectIntent);
                break;

            case R.id.rt_frnt_dr_tv:
                captureImageIntent.putExtra("imagepath", "Right Front Door");
                startActivityForResult(captureImageIntent, 0);
                break;

            case R.id.lt_frnt_dr_tv:
                captureImageIntent.putExtra("imagepath", "Left Front Door");
                startActivityForResult(captureImageIntent, 1);
                break;

            case R.id.rt_bck_dr_tv:
                captureImageIntent.putExtra("imagepath", "Right Back Door");
                startActivityForResult(captureImageIntent, 2);
                break;

            case R.id.lt_back_dr_tv:
                captureImageIntent.putExtra("imagepath", "Left Back Door");
                startActivityForResult(captureImageIntent, 3);
                break;

            case R.id.frnt_bmpr_tv:
                captureImageIntent.putExtra("imagepath", "Front Bumper");
                startActivityForResult(captureImageIntent, 4);
                break;

            case R.id.back_bmpr_tv:
                captureImageIntent.putExtra("imagepath", "Back Bumper");
                startActivityForResult(captureImageIntent, 5);
                break;

            case R.id.view_all_pics:
                startActivity(new Intent(mActivity, ShowingSnapshotScreen.class));
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Toast.makeText(this, "Image Saved Successfully!!", Toast.LENGTH_LONG).show();

        }

    }


    public void showMessageChooseDialog() {
        final Dialog mDialog = new Dialog(mActivity);
        // hide to default title for Dialog
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // inflate the layout dialog_layout.xml and set it as
        // contentView

        LayoutInflater inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(
                R.layout.text_and_button_dialog, null, false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(view);
        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(0));

        // Retrieve views from the inflated dialog layout and update
        // their
        // values
//        TextView messageText = (TextView) mDialog
//                .findViewById(R.id.messageText);
//        TextView buttonMsgText = (TextView) mDialog
//                .findViewById(R.id.button_text);
        setViewText(view, R.id.messageText, mActivity.getString(R.string.job_completed));
        setViewText(view, R.id.button_text, mActivity.getString(R.string.done));

        // messageText.setText(R.string.job_completed);
        //buttonMsgText.setText(R.string.done);

        RelativeLayout closeButtonView = (RelativeLayout) mDialog
                .findViewById(R.id.closeButtonView);
        closeButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog
                mDialog.dismiss();


            }
        });

        try {
            mDialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
