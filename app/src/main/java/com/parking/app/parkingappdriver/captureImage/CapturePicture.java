package com.parking.app.parkingappdriver.captureImage;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.utils.AppConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CapturePicture extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CapturePicture";
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private RelativeLayout captureButton, cancel_action_capture;
    private String imagePath;
    private long createdAt;
    private ImageView clicked_img;
    private RelativeLayout rotate_btn_rl, done_btn_rl;
    private Bitmap bitmap;

    private String filename = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_image);
        initViews();
        assignClicks();
    }

    private void assignClicks() {
        captureButton.setOnClickListener(this);
        cancel_action_capture.setOnClickListener(this);
        done_btn_rl.setOnClickListener(this);
        rotate_btn_rl.setOnClickListener(this);
    }

    private void initViews() {


        if (getIntent().getExtras().getString("imagepath") != null) {
            filename = getIntent().getExtras().getString("imagepath");
            Log.d(TAG, filename + " filename");
        }


        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);


        clicked_img = (ImageView) findViewById(R.id.clicked_img);
        rotate_btn_rl = (RelativeLayout) findViewById(R.id.rotate_btn_rl);
        done_btn_rl = (RelativeLayout) findViewById(R.id.done_btn_rl);
        captureButton = (RelativeLayout) findViewById(R.id.button_capture);
        cancel_action_capture = (RelativeLayout) findViewById(R.id.cancel_action_capture);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                preview.addView(mPreview);

            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        if (isDeviceSupportCamera(this)) {
            // initialize camera and display it
//            capture();
        } else {
            Toast.makeText(this, "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
        }
    }

    public void capture() {
        // get an image from the camera
        mCamera.takePicture(null, null, mPicture);
        clicked_img.setVisibility(View.GONE);
        done_btn_rl.setVisibility(View.GONE);
        rotate_btn_rl.setVisibility(View.GONE);
        cancel_action_capture.setVisibility(View.GONE);

    }

    private boolean isDeviceSupportCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(0);
        } catch (Exception e) {
        }
        return c;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Camera call back");
                    File pictureFile = null;
                    try {
                        pictureFile = getOutputMediaFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (pictureFile == null) {
                        return;
                    }

                    try {
                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        fos.write(data);
                        fos.close();
                        int rotation = getImageRotationInDegrees();
                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        bitmap = BitmapFactory.decodeFile(imagePath, options);
                        Log.d(TAG, "rotation is " + rotation);
                        if (rotation != 0) {
                            bitmap = getAdjustedBitmap(bitmap, rotation);
                            Log.d(TAG, "calling replace image");
                            replaceImg(bitmap);
                            Log.d("TAG", "bitmap compressed successfully");
                        }

                        clicked_img.setVisibility(View.VISIBLE);
                        done_btn_rl.setVisibility(View.VISIBLE);
                        rotate_btn_rl.setVisibility(View.VISIBLE);
                        cancel_action_capture.setVisibility(View.VISIBLE);
                        Log.d(TAG, imagePath + "");
                        BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                        clicked_img.setBackgroundDrawable(ob);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    };

    private File getOutputMediaFile() throws IOException {
        createdAt = System.currentTimeMillis();
        File storageDir = new File(AppConstants.SNAPSHOT_DIRECTORY_PATH);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        String fileName = filename + ".jpg";
        File file = new File(storageDir, fileName);
        file.createNewFile();
        imagePath = file.getAbsolutePath();
        return file;
    }

    private int getImageRotationInDegrees() {
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
                return 90;
            } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
                return 180;
            } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
                return 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Bitmap getAdjustedBitmap(Bitmap bitmap, int rotate) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                true);
        return bitmap;
    }

    private void replaceImg(Bitmap bitmap) {
        File file = new File(imagePath);
        file.delete();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_capture:
                Log.d(TAG, "capture clicked");
//                Toast.makeText(this, "capture clicked", Toast.LENGTH_LONG).show();
                capture();
                break;

            case R.id.cancel_action_capture:
                Log.d(TAG, "cancel clicked");

                clicked_img.setVisibility(View.GONE);
                done_btn_rl.setVisibility(View.GONE);
                rotate_btn_rl.setVisibility(View.GONE);
                cancel_action_capture.setVisibility(View.GONE);
                mCamera.stopPreview();
                mCamera.startPreview();
                break;
            case R.id.done_btn_rl:
                mCamera.stopPreview();
                mCamera.release();

                replaceImg(bitmap);


                Intent intent = new Intent();
                intent.putExtra(AppConstants.CLICKED_IMAGE_PATH, imagePath);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.rotate_btn_rl:
                rotateImage();
                break;
        }
    }


    private void rotateImage() {
        Log.d(TAG, "image rotated");
        Matrix matrix = new Matrix();

        matrix.postRotate(90);


        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


        BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
        clicked_img.setBackgroundDrawable(ob);

    }


}