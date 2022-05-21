package com.example.imagecropper;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    ImageView image;
    Button gallery, camera;
    ActivityResultLauncher<String> mgetContent;
    BitmapDrawable drawable;
    Bitmap bitmapsave;
    Uri imageUri;
    Bitmap thumbnail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MainActivity");
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.imageView);
        gallery = findViewById(R.id.button);
        camera = findViewById(R.id.button2);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mgetContent.launch("image/*");
            }
        });
        mgetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(MainActivity.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                imageUri = result;
                startActivityForResult(intent, 100);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ContentValues values;
                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 10);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 10 && resultCode == RESULT_OK) {
            try {
                Uri image;
                thumbnail = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri);


                // image =  getImageUri(MainActivity.this, thumbnail);


                Intent cameraintent = new Intent(MainActivity.this, CropperActivity.class);
                cameraintent.putExtra("Value", imageUri.toString());
                startActivityForResult(cameraintent, 101);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        if (resultCode == RESULT_OK && requestCode == 100) {
            String result = data.getStringExtra("RESULT");
            Log.e("Hello", "hii");
            Uri resulturi = null;
            if (result != null) {
                resulturi = Uri.parse(result);
                Log.e("Hello", String.valueOf(resulturi));
                image.setImageURI(resulturi);
                drawable = (BitmapDrawable) image.getDrawable();
                bitmapsave = drawable.getBitmap();


                FileOutputStream outputStream = null;
                File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                String filename = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(storage, filename);
                try {
                    outputStream = new FileOutputStream(outFile);
                    bitmapsave.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outFile));
                    MainActivity.this.sendBroadcast(intent);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        if (resultCode == RESULT_OK && requestCode == 101) {
            String resu = data.getStringExtra("RESULT");
            Log.e("HelloWorld", "hiii");
            Uri resUri = null;
            if (resu != null) {
                resUri = Uri.parse(resu);
                image.setImageURI(resUri);
                drawable = (BitmapDrawable) image.getDrawable();
                bitmapsave = drawable.getBitmap();
                FileOutputStream outputStream = null;
                File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String filename = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(storage, filename);
                try {

                    outputStream = new FileOutputStream(outFile);
                    bitmapsave.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outFile));
                    MainActivity.this.sendBroadcast(intent);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        else{
            if(imageUri!=null){
                image.setImageURI(imageUri);
                drawable = (BitmapDrawable) image.getDrawable();
                bitmapsave = drawable.getBitmap();
                FileOutputStream outputStream = null;
                File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String filename = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(storage, filename);
                try {

                    outputStream = new FileOutputStream(outFile);
                    bitmapsave.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outFile));
                    MainActivity.this.sendBroadcast(intent);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}