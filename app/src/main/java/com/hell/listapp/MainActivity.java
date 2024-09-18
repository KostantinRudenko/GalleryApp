
package com.hell.listapp;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private ListView LV;
    private final String [] names = new String[] {"Alex", "Bobr", "Dan"};

    private final String READ_IMAGES_PERMISSION = "android.permission.READ_MEDIA_IMAGES";
    private final int GALLERY_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askForPermission();

        list();
    }

    private void askForPermission()
    {
        if (ContextCompat.checkSelfPermission(this, READ_IMAGES_PERMISSION)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {READ_IMAGES_PERMISSION}, GALLERY_PERMISSION_CODE);
        } else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == GALLERY_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
            }
            else if (grantResults[0] == PackageManager.PERMISSION_DENIED)
            {
                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
                Log.d("Debug", "Denied");
            }
        }
    }

    public void list(){
        LV = (ListView)findViewById(R.id.ListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.names, names);
        LV.setAdapter(adapter);
    }

    public void getImages()
    {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = getContentResolver();

        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME};

        Cursor cursor = cr.query(uri, projection, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);

                Uri imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));

                Toast.makeText(this, "Image: " + name + " URI: " + imageUri.toString(), Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }
    }
}
