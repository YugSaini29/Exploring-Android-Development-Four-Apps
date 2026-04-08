package com.example.cameras_photos_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class PhotosActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Uri> imageUris;
    PhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_photos);

        recyclerView = findViewById(R.id.recyclerView);

        // grid layout 3 images per row
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        imageUris = new ArrayList<>();
        // filling our arraylist with file uris
        loadImages();

        adapter = new PhotoAdapter(this, imageUris);
        recyclerView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        imageUris.clear();   // clear old data
        loadImages();        // reload from folder
        adapter.notifyDataSetChanged(); // refresh UI
    }
    private void loadImages() {
        // getting folder uri from pref
        SharedPreferences prefs = getSharedPreferences("folderPrefs", MODE_PRIVATE);
        String uriString = prefs.getString("folderUri", null);

        if (uriString == null) {
            Toast.makeText(this, "No folder selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri folderUri = Uri.parse(uriString);
        DocumentFile folder = DocumentFile.fromTreeUri(this, folderUri);

        if (folder == null) return;
        // getting every file in that folder
        for (DocumentFile file : folder.listFiles()) {
            if (file.isFile() && file.getType() != null && file.getType().startsWith("image/")) {
                imageUris.add(file.getUri());
            }
        }
    }


    class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

        Context context;
        ArrayList<Uri> imageUris;

        public PhotoAdapter(Context context, ArrayList<Uri> imageUris) {
            this.context = context;
            this.imageUris = imageUris;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_photo, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Uri uri = imageUris.get(position);

            // small fix for refresh issues
            holder.imageView.setImageURI(null);
            holder.imageView.setImageURI(uri);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("imageUri", uri.toString());
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return imageUris.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }
}