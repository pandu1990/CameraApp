package com.applicationcourse.mobile.assignment3_1002814653;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.applicationcourse.mobile.assignment3_1002814653.camera2basic.R;

import java.io.File;
import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        gridView = (GridView) findViewById(R.id.gridView);
        setItems();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(ViewActivity.this, DetailsActivity.class);
                intent.putExtra("location", item.getTitle());
                intent.putExtra("path", item.getPath());

                //Start details activity
                startActivity(intent);
            }
        });

    }

    private void setItems() {
        ArrayList<ImageItem> imageItems = getData();
        if(imageItems.isEmpty()) {
            Toast.makeText(ViewActivity.this, getString(R.string.no_images), Toast.LENGTH_SHORT).show();
            finish();
        }
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, imageItems);
        gridView.setAdapter(gridAdapter);
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
                        String latitude = String.format("%.4f", latlng[0]);
                        String longitude = String.format("%.4f", latlng[1]);
                        location = latitude + ", " + longitude;
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

    @Override
    protected void onResume() {
        setItems();
        super.onResume();
    }
}
