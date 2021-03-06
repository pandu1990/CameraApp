package com.applicationcourse.mobile.assignment3_1002814653;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.applicationcourse.mobile.assignment3_1002814653.camera2basic.R;

import java.io.File;

/**
 * Created by teresa on 6/2/16.
 */
public class DetailsActivity extends AppCompatActivity {

    private String imagePath;

    private Thread mBackgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        imagePath = getIntent().getStringExtra("path");
        String location = getIntent().getStringExtra("location");
        this.setTitle(location);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(decodeSampledBitmapFromResource(imagePath, 500, 500));
        mBackgroundHandler = new Thread(new ImageDelete(imagePath));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.delete) {
            Toast.makeText(DetailsActivity.this, getString(R.string.image_deleted), Toast.LENGTH_SHORT).show();
            mBackgroundHandler.start();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ImageDelete implements Runnable {

        private String mFile;

        public ImageDelete(String toDelete) {
            mFile = toDelete;
        }

        @Override
        public void run() {
            File fileToBeRemoved = new File(mFile);
            fileToBeRemoved.delete();
            removePicFromGallery(fileToBeRemoved);
        }
    }

    private void removePicFromGallery(File fileToBeRemoved) {
        try {
            getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.Media.DATA + "='" + fileToBeRemoved.getPath() + "'",
                    null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(String filePath,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
}
