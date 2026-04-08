package com.example.cameras_photos_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.documentfile.provider.DocumentFile;

public class HubActivity extends AppCompatActivity {
    Button cameraBtn;
    Button photosBtn;
    private Uri pendingFileUri = null;

    private final ActivityResultLauncher<Uri> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (success) {
                    Toast.makeText(this, "Photo saved!", Toast.LENGTH_SHORT).show();
                } else {
                    // we need to delete the created file since the capture image process is cancelled by user
                    if (pendingFileUri != null) {
                        // getting file from uri
                        DocumentFile file = DocumentFile.fromSingleUri(this, pendingFileUri);
                        if (file != null) file.delete(); // deleting the file
                        pendingFileUri = null; // setting uri to null again
                    }
                    Toast.makeText(this, "Capture cancelled", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hub);
        // getting view from xml file using ids
        cameraBtn = findViewById(R.id.cameraBtn);
        photosBtn = findViewById(R.id.photosBtn);

        cameraBtn.setOnClickListener(v -> {
            // when user opens the camera for the first time we need to get permission to use camera and then start camera
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA}, 101);
            } else {
                openCamera();
            }
        });

        photosBtn.setOnClickListener(v -> {
            Intent goToPhotosActivity = new Intent(HubActivity.this, PhotosActivity.class);
            startActivity(goToPhotosActivity);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCamera() {
        SharedPreferences prefs = getSharedPreferences("folderPrefs", MODE_PRIVATE);
        String uriString = prefs.getString("folderUri", null);
        if (uriString == null) {
            Toast.makeText(this, "No folder selected", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri folderUri = Uri.parse(uriString);
        DocumentFile folder = DocumentFile.fromTreeUri(this, folderUri);
        String fileName = "IMG_" + System.currentTimeMillis();
        DocumentFile newFile = folder.createFile("image/jpeg", fileName);
        if (newFile == null) {
            Toast.makeText(this, "Failed to create file", Toast.LENGTH_SHORT).show();
            return;
        }
        pendingFileUri = newFile.getUri();
        cameraLauncher.launch(pendingFileUri);
    }
}