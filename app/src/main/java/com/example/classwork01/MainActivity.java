package com.example.classwork01;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {

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
     * Handles the "onClick" event for the "Take Picture" button.
     * @param view
     * AndroidManifest.xml has XML added to it to enable saving images, etc.
     */
    public void takePicture(View view) {

        // There are two types of Intent objects: explicit (when you specify the class),
        // and implicit, when you are asking for whether an app can meet the need without having
        // to know the class
        Intent x = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check to see if there is an app that can handle this intent. If not, then return.
        // There is a warning here:
        // Consider adding a <queries> declaration to your manifest when calling this method
        // Why?
        ComponentName componentName = x.resolveActivity(getPackageManager());

        // Stop here if componentName is null; this means that no activity from any other app
        // matches our requested Intent type
        if (componentName == null) {
            return;
        }

        // Create the File where the photo should go
        File photoFile = null;



        /*
        Generally, any photos that the user captures with the device camera should be saved on the
        device in the public external storage so they are accessible by all apps. The proper
        directory for shared photos is provided by getExternalStoragePublicDirectory(), with the
        DIRECTORY_PICTURES argument.
         */



    }
}