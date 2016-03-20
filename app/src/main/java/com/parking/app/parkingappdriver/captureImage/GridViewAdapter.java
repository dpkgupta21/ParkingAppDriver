package com.parking.app.parkingappdriver.captureImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.parking.app.parkingappdriver.R;

import java.io.File;
import java.util.List;



public class GridViewAdapter extends ArrayAdapter<File> {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    List<File> snapshortFiles;
    private SparseBooleanArray mSelectedItemsIds;

    public GridViewAdapter(Context context, int resource,
                           List<File> snapshortFiles) {
        super(context, resource, snapshortFiles);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.snapshortFiles = snapshortFiles;
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder {
        ImageView snapshortImage;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.snapshot_item_view, null);
            // Locate the ImageView in listview_item.xml
            holder.snapshortImage = (ImageView) view
                    .findViewById(R.id.snapshortImage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        Bitmap bitmap = BitmapFactory.decodeFile(snapshortFiles.get(position)
                .getAbsolutePath(), bmOptions);

        // Capture position and set to the ImageView
        holder.snapshortImage.setImageBitmap(bitmap);
        // holder.selectedView.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void remove(File object) {
        snapshortFiles.remove(object);
        notifyDataSetChanged();
    }

    public List<File> getWorldPopulation() {
        return snapshortFiles;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));

    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}