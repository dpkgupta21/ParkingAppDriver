package com.parking.app.parkingappdriver.drivermodel;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.parking.app.parkingappdriver.R;
import com.parking.app.parkingappdriver.activity.BaseActivity;
import com.parking.app.parkingappdriver.captureImage.CapturePicture;
import com.parking.app.parkingappdriver.captureImage.GridViewAdapter;
import com.parking.app.parkingappdriver.captureImage.ShowFullSnapShotOnGUI;
import com.parking.app.parkingappdriver.model.VehicleImagesDTO;
import com.parking.app.parkingappdriver.model.VehicleInspectDTO;
import com.parking.app.parkingappdriver.utils.AppConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class VehicleInspect extends BaseActivity {
    /**
     * This String TAG is used to show TAG into logcat at debug time.
     */
    private String TAG = VehicleInspect.class.getSimpleName();
    /**
     * Snapshot image files list object
     */
    private List<File> snapshotFiles = new ArrayList<>();
    /**
     * Grid view object instance
     */
    private GridView snapShotGrid;
    private GridViewAdapter mGridViewAdapter;
    private Toolbar mToolbar;
    private Activity mActivity;
    private VehicleInspectDTO inspectDTO;
    private List<VehicleImagesDTO> imagesDTOs;


    private int i = 0;
    // Use
    private File _dir;
    private File _file;
    private Uri contentUri;
    private List<String> filePathList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_inspect);
        mActivity = VehicleInspect.this;
        createDirectoryForPictures();
        initViews();
        manageGridAdapter();
        singleClickOnGridItem();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * Single Click on grid Item for showing full screen mode
     */
    private void singleClickOnGridItem() {
        snapShotGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();

                AppConstants.fullScreenBitmap = BitmapFactory.decodeFile(
                        snapshotFiles.get(position).getAbsolutePath(),
                        bmOptions);
                AppConstants.imageName = snapshotFiles.get(position).getName();
                Intent mFullScreenIntent = new Intent(mActivity, ShowFullSnapShotOnGUI.class);
                startActivity(mFullScreenIntent);
            }
        });
    }

    /**
     * Init view of application and initialize data into this
     */
    private void initViews() {
        setClick(R.id.take_vehicle_button);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Vehicle Inspection");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_button);

        inspectDTO = (VehicleInspectDTO) getIntent().getSerializableExtra("InspectDTO");
        imagesDTOs = new ArrayList<>();

        setViewText(R.id.toolbar_right_tv, "Next");
        setViewVisibility(R.id.toolbar_right_rl, View.VISIBLE);
        setClick(R.id.toolbar_right_tv);
        /* Assign id to grid view object */
        snapShotGrid = (GridView) findViewById(R.id.snapshortGrid);
        setViewVisibility(R.id.noImageFound, View.GONE);
        snapshotFiles = getDirectoryFiles(new File(
                AppConstants.SNAPSHOT_DIRECTORY_PATH));
        //Log.d(TAG, snapshotFiles.size() + "");
    }


    /**
     * Manage Grid View Adapter
     */
    private void manageGridAdapter() {
        if (snapshotFiles != null && !snapshotFiles.isEmpty()) {
            if (mGridViewAdapter == null) {
                mGridViewAdapter = new GridViewAdapter(this,
                        R.layout.snapshot_item_view, snapshotFiles);
                setViewVisibility(R.id.noImageFound, View.GONE);
                snapShotGrid.setVisibility(View.VISIBLE);
            } else {
                mGridViewAdapter.setSnapshotFiles(snapshotFiles);
                mGridViewAdapter.notifyDataSetChanged();
            }
        } else {
            setViewVisibility(R.id.noImageFound, View.VISIBLE);
            snapShotGrid.setVisibility(View.GONE);
        }
        // Binds the Adapter to the Gridview
        snapShotGrid.setAdapter(mGridViewAdapter);
        snapShotGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);

        // Capture ListView item click
        snapShotGrid.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = snapShotGrid.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                mGridViewAdapter.toggleSelection(position);
                mToolbar.setVisibility(View.GONE);
            }


            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = mGridViewAdapter
                                .getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                File selecteditem = mGridViewAdapter
                                        .getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                mGridViewAdapter.remove(selecteditem);
                                selecteditem.delete();
                                for (VehicleImagesDTO dto : imagesDTOs) {
                                    if (dto.getImage_name().equalsIgnoreCase(selecteditem.getName())) {
                                        imagesDTOs.remove(dto);
                                    }
                                }

                            }
                        }
                        // Close CAB
                        mode.finish();
                        Toast.makeText(mActivity, "Images deleted Successfully!!", Toast.LENGTH_LONG).show();
                        mToolbar.setVisibility(View.VISIBLE);

                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.snapshot_menu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mGridViewAdapter.removeSelection();
                mToolbar.setVisibility(View.VISIBLE);

            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
        });

    }

    /**
     * Get all files from directory
     *
     * @param parentDir
     */
    private List<File> getDirectoryFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files;
        try {
            try {
                if (parentDir.listFiles() == null) {
                    return null;
                }
                files = parentDir.listFiles();
            } catch (NullPointerException e) {
                return null;
            }

            for (File file : files) {
                if (file.isDirectory()) {
                    inFiles.addAll(getDirectoryFiles(file));
                } else {
                    if (file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) {
                        VehicleImagesDTO dto = new VehicleImagesDTO();
                        dto.setImage_name(file.getName());
                        imagesDTOs.add(dto);
                        inFiles.add(file);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        return inFiles;
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.take_vehicle_button:
                takeAPicture();
                //startActivityForResult(new Intent(mActivity, CapturePicture.class), 1000);
                break;
            case R.id.toolbar_right_tv:
                try {
                    inspectDTO.setImagesDTO(imagesDTOs);
                    Intent customerIntent = new Intent(mActivity, CustomerComments.class);
                    customerIntent.putExtra("InspectDTO", inspectDTO);
                    startActivity(customerIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1000) {
//            if (resultCode == RESULT_OK) {
//                if(snapshotFiles!=null && !snapshotFiles.isEmpty()) {
//                    snapshotFiles.clear();
//                }
//                if(imagesDTOs!=null && imagesDTOs.isEmpty()) {
//                    imagesDTOs.clear();
//                }
//
//                snapshotFiles = getDirectoryFiles(new File(
//                        AppConstants.SNAPSHOT_DIRECTORY_PATH));
//                manageGridAdapter();
//            }
//        }
//    }


    private void takeAPicture() {
        try {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            _file = new File(_dir, System.currentTimeMillis() + ".png");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(_file));
            startActivityForResult(intent, 2000);


        } catch (Exception e1) {
            e1.getStackTrace();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {


            // Make it available in the gallery
            if (resultCode == RESULT_OK) {
                //Intent mediaScanIntent = new Intent(MediaStore.ActionImageCapture);
                Uri contentUri = Uri.fromFile(_file);
                if (snapshotFiles != null) {
                    snapshotFiles.add(_file);
                }
                filePathList.add(_file.getPath());
                Bitmap bitmap = getImageResized(contentUri);


                exportBitmapAsFile(bitmap, _file.getPath());

                manageGridAdapter();


            }


        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    public Bitmap getImageResized(Uri selectedImage) {
        Bitmap bm = null;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(selectedImage, sampleSizes[i]);
            //Log.i("info", "GetImageResized resizer: new bitmap width = " + bm.getWidth());
            i++;
        } while (bm.getWidth() < 400 && i < sampleSizes.length);

        int rotation = getRotation(selectedImage, true);
        bm = rotate(bm, rotation);

        return bm;
    }

    public void exportBitmapAsFile(Bitmap bitmap, String filePath) {
        //var sdCardPath = Android.OS.Environment.ExternalStorageDirectory.AbsolutePath;
        //var filePath = System.IO.Path.Combine(sdCardPath, "test.png");
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(filePath, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void createDirectoryForPictures() {
        try {


            _dir = new File(AppConstants.SNAPSHOT_DIRECTORY_PATH);
            if (!_dir.exists()) {
                _dir.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap decodeBitmap(Uri theUri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        }
        Bitmap actuallyUsableBitmap = null;
        if (fileDescriptor != null) {
            actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                    fileDescriptor.getFileDescriptor(), null, options);

        }
        return actuallyUsableBitmap;
    }

    private int getRotation(Uri imageUri, boolean isCamera) {
        int rotation;
        if (isCamera) {
            rotation = getRotationFromCamera(imageUri);
        } else {
            rotation = getRotationFromCamera(imageUri);
        }
        return rotation;
    }

    private int getRotationFromCamera(Uri imageFile) {
        int rotate = 0;
        try {

            getContentResolver().notifyChange(imageFile, null);
            ExifInterface exif = new ExifInterface(imageFile.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return rotate;
    }

    private static Bitmap rotate(Bitmap bm, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bmOut = Bitmap.createBitmap(bm, 0, 0,
                    bm.getWidth(), bm.getHeight(), matrix, true);
            return bmOut;
        }
        return bm;
    }


    public Bitmap LoadAndResizeBitmap(String fileName, int width, int height) {


        try {


            // First we get the the dimensions of the file on disk
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, options);

            // Next we calculate the ratio that we need to resize the image by
            // in order to fit the requested dimensions.
            int outHeight = options.outHeight;
            int outWidth = options.outWidth;
            int inSampleSize = 1;

            if (outHeight > height || outWidth > width) {
                inSampleSize = outWidth > outHeight
                        ? outHeight / height
                        : outWidth / width;
            }

            // Now we will load the image and have BitmapFactory resize it for us.
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            Bitmap resizedBitmap = BitmapFactory.decodeFile(fileName, options);

            resizedBitmap = GetPictureStreamWithRotation(resizedBitmap, fileName);

            return resizedBitmap;


        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }

    public static Bitmap GetPictureStreamWithRotation(Bitmap bitmap, String picturePath) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(picturePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                (int) ExifInterface.ORIENTATION_UNDEFINED);

        //Bitmap resultBitmap = BitmapFactory.DecodeFile(picturePath);
        Matrix mtx = new Matrix();

        switch (orientation) {
            case ExifInterface.ORIENTATION_UNDEFINED: // Nexus 7 landscape...
                break;
            case ExifInterface.ORIENTATION_NORMAL: // landscape
                break;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                mtx.preRotate(180);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), mtx, false);
                // mtx.Dispose();
                mtx = null;
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                break;
            case ExifInterface.ORIENTATION_ROTATE_90: // portrait
                mtx.preRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), mtx, false);
                //mtx.Dispose();
                mtx = null;
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                break;
            case ExifInterface.ORIENTATION_ROTATE_270: // might need to flip horizontally too...
                mtx.preRotate(270);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), mtx, false);
                //mtx.Dispose();
                mtx = null;
                break;
            default:
                mtx.preRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), mtx, false);
                //mtx.Dispose();
                mtx = null;
                break;
        }

        return bitmap;

    }
}
