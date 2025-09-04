package com.example.classwork01;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String currentPhotoPath;
    Uri currentPhotoUri;

    int REQUEST_IMAGE_CAPTURE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Returns a File object for saving the full-size photo.
     * @return
     * @throws IOException
     * <a href="https://developer.android.com/media/camera/camera-deprecated/photobasics#TaskPath">...</a>
     */
    private File createImageFile() throws IOException {
        // Create the filename first
        // The Locale.US is optional, sets the timezone for the date
        String timeStamp = new SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.US
        ).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".png";

        // Seems like you have to create a File object for the parent directory of the photo
        // that will be returned from the camera
        File imageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.v("takePicture", "imageDir (abs path): " + imageDir.getAbsolutePath());
        Log.v("takePicture", "imageDir (getPath): " + imageDir.getPath());
        Log.v("takePicture", "imageDir exists: " + imageDir.isDirectory());

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                imageDir      /* directory */
        );

//        File imageFile = new File(imageDir, "IMG_" + timeStamp + ".png");

        // save the absolute path of the image file (just in case, I'm not sure it's needed)
        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    /**
     * This method waits for the picture to be returned from the camera and then updates
     * the imageview. Without using this, the application will be checking for the photo
     * before it exists yet.
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.v("takePicture", "The camera activity has been returned.");

            // Update the imageview
            Bitmap image = BitmapFactory.decodeFile(currentPhotoPath);
            ImageView v = findViewById(R.id.iv);
            v.setImageBitmap(image);
        }
    }

    /**
     * Handles the "onClick" event for the "Take Picture" button.
     * @param view
     * AndroidManifest.xml has XML added to it to enable saving images, etc.
     */
    public void takePicture(View view) {

        // There are two types of Intent objects: explicit (when you specify the class),
        // and implicit, when you are asking for whether an app can meet the need without having
        // to know the class
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check to see if there is an app that can handle this intent. If not, then return.
        // There is a warning here:
        // Consider adding a <queries> declaration to your manifest when calling this method
        // Why? Never did it and this works.
        ComponentName componentName = takePictureIntent.resolveActivity(getPackageManager());

        // Stop here if componentName is null; this means that no activity from any other app
        // matches our requested Intent type
        if (componentName == null) {
            Log.v("takePicture", "No app found to take the picture.");
            return;
        }

        // Create the File where the photo should go
        File photoFile = null;

        try {
            // This will always be not null unless an error occurs
            photoFile = createImageFile();

        } catch (IOException ex) {

            Log.v("takePicture", "Error occurred creating the image file.");
            return;
        }

        Uri photoURI = FileProvider.getUriForFile(this,
                "com.example.classwork01.fileprovider", // "com.example.android.fileprovider",
                photoFile);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

    }
}