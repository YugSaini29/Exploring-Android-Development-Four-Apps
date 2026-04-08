package com.example.cameras_photos_app;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView details;
    Button deleteBtn;

    Uri imageUri;
    DocumentFile file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // get view by id
        imageView = findViewById(R.id.imageView);
        details = findViewById(R.id.details);
        deleteBtn = findViewById(R.id.deleteBtn);

        // get uri which we passed on from the onclick function in PhotosAcitivity
        String uriString = getIntent().getStringExtra("imageUri");

        if (uriString == null) {
            finish();
            return;
        }
        // get the file from the uri
        imageUri = Uri.parse(uriString);
        file = DocumentFile.fromSingleUri(this, imageUri);

        imageView.setImageURI(imageUri);

        if (file != null) {
            // get info from the file
            String name = file.getName();
            long size = file.length();
            long lastModified = file.lastModified();

            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm",
                    Locale.getDefault()).format(new Date(lastModified));

            // getting info in one string
            String info = "Name: " + name +
                    "\nSize: " + (size / 1024) + " KB" +
                    "\nDate: " + date +
                    "\nURI: " + imageUri.toString();

            details.setText(info);
        }
        // confirm delete function will launch an alert dialog box, which will confirm the deletion.
        deleteBtn.setOnClickListener(v -> confirmDelete());
    }

    private void confirmDelete() {

        new AlertDialog.Builder(this)
                .setTitle("Delete Image")
                .setMessage("Are you sure you want to delete this image?")
                .setPositiveButton("Yes", (dialog, which) -> deleteImage())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteImage() {
        if (file != null && file.delete()) {
            finish(); // go back to gallery
        }
    }
}