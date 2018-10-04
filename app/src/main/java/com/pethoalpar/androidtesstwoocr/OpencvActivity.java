package com.pethoalpar.androidtesstwoocr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.zelory.compressor.Compressor;


public class OpencvActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opencv);

        int preference = 4;
        startScan(preference);
    }

    protected void startScan(int preference) {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;

            OutputStream os;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);

                File photoFile = createImageFile();

                os = new FileOutputStream(photoFile);
                int imageWidth = bitmap.getWidth();
                int imageHeight = bitmap.getHeight();
                int newHeight = (imageHeight * 2000)/imageWidth;
                bitmap = Bitmap.createScaledBitmap(bitmap, 2000, newHeight, true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, os);

//                File compressedImageFile = new Compressor(this).compressToFile(photoFile);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
                byte[] byteArray = stream.toByteArray();

                Intent in1 = new Intent(getBaseContext(), TesseractActivity.class);
                in1.putExtra("image", byteArray);
                startActivityForResult(in1, 0);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            String result = data.getStringExtra("result");
            intent.putExtra("result", result);
            this.setResult(Activity.RESULT_OK, intent);
            finish();
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
