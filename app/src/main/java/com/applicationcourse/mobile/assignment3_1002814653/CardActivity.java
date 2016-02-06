package com.applicationcourse.mobile.assignment3_1002814653;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.widget.Toast;

import com.applicationcourse.mobile.assignment3_1002814653.camera2basic.R;

import java.io.File;
import java.util.ArrayList;

public class CardActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        // setItems();
       /* mAdapter = new CardAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        setItems();
    }

    private void setItems() {
        ArrayList<ImageItem> imageItems = getData();
        if(imageItems.isEmpty()) {
            Toast.makeText(CardActivity.this, getString(R.string.no_images), Toast.LENGTH_SHORT).show();
            finish();
        }
        mAdapter = new CardAdapter(CardActivity.this, imageItems);
        mRecyclerView.setAdapter(mAdapter);
    }

    // Prepare some dummy data for gridview
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        if (mediaStorageDir.exists()) {
            File[] listOfFiles = mediaStorageDir.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                Bitmap myBitmap = setPic(listOfFiles[i].getAbsolutePath());
                float[] latlng = new float[2];
                String location = "";
                try {
                    ExifInterface exif = new ExifInterface(listOfFiles[i].getAbsolutePath());
                    if (exif.getLatLong(latlng)) {
                        /*String latitude = String.format("%.6f", latlng[0]);
                        String longitude = String.format("%.6f", latlng[1]);*/
                        location = latlng[0] + ", " + latlng[1];
                    }
                    if(latlng[0] == 0.0 && latlng[1] == 0.0) {
                        location = "Location not available";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imageItems.add(new ImageItem(myBitmap, location, listOfFiles[i].getAbsolutePath()));
            }
        }
        return imageItems;
    }

    private Bitmap setPic(String mCurrentPhotoPath) {
        // Get the dimensions of the View

        float dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        float scaleFactor = Math.min(photoW/dimension, photoH/dimension);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = Math.round(scaleFactor);
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        return bitmap;
    }
}