package com.parking.app.parkingappdriver.drivermodel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;


public class VehicleInspect extends BaseActivity {
    /**
     * This String TAG is used to show TAG into logcat at debug time.
     */
    private String TAG = VehicleInspect.class.getSimpleName();
    /**
     * Snapshot image files list object
     */
    private List<File> snapshotFiles;
    /**
     * Grid view object instance
     */
    private GridView snapShotGrid;
    private GridViewAdapter mGridViewAdapter;
    private Toolbar mToolbar;
    private Activity mActivity;
    private VehicleInspectDTO inspectDTO;
    private List<VehicleImagesDTO> imagesDTOs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_inspect);
        mActivity = VehicleInspect.this;
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
        Log.d(TAG, snapshotFiles.size() + "");
    }


    /**
     * Manage Grid View Adapter
     */
    private void manageGridAdapter() {
        if (snapshotFiles != null && !snapshotFiles.isEmpty()) {
            mGridViewAdapter = new GridViewAdapter(this,
                    R.layout.snapshot_item_view, snapshotFiles);
            setViewVisibility(R.id.noImageFound, View.GONE);
            snapShotGrid.setVisibility(View.VISIBLE);
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
                    if (file.getName().endsWith(".jpg")) {
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
                startActivityForResult(new Intent(mActivity, CapturePicture.class), 1000);
                break;
            case R.id.toolbar_right_tv:
                try {
                    inspectDTO.setImagesDTO(imagesDTOs);
                    Intent customerIntent = new Intent(mActivity, CustomerComments.class);
                    customerIntent.putExtra("InspectDTO", inspectDTO);
                    startActivity(customerIntent);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                snapshotFiles.clear();
                imagesDTOs.clear();
                snapshotFiles = getDirectoryFiles(new File(
                        AppConstants.SNAPSHOT_DIRECTORY_PATH));
                manageGridAdapter();
            }
        }
    }
}
