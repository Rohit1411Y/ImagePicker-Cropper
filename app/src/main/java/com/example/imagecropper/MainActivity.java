package com.example.imagecropper;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    ImageView image;
    Button gallery,camera;
   ActivityResultLauncher<String> mgetContent;
   ActivityResultLauncher<Intent> activityResultLauncher;
   BitmapDrawable drawable;
   Bitmap bitmapsave;
   Uri imageUri;
   Bitmap thumbnail;
   String imageurl;
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
             intent.putExtra("DATA",result.toString());
             startActivityForResult(intent,100);
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
         // activityResultLauncher.launch(inten);
            }
        });
//        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult resul) {
//                Bundle extras = resul.getData().getExtras();
//                Uri imageuri;
//                Bitmap imagebitmap  = (Bitmap) extras.get("data");
////                WeakReference<Bitmap> resl = new WeakReference<>(Bitmap.createScaledBitmap(imagebitmap,imagebitmap.getHeight(),imagebitmap.getWidth(),true).copy(Bitmap.Config.RGB_565,true));
////                Bitmap bmp = resl.get();
//               // imageuri = saveImage(imagebitmap,MainActivity.this);
//                imageuri = getImageUri(MainActivity.this, imagebitmap);
//                Intent cameraintent = new Intent(MainActivity.this,CropperActivity.class);
//                cameraintent.putExtra("Value",imageuri.toString());
//                startActivityForResult(cameraintent,101);
//                //image.setImageURI(imageuri);
//                //Tv_Uri.setText(""+imageuri);
//            }
//
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 10:
                if (requestCode == 10)
                    if (resultCode == RESULT_OK) {
                        try {
                            thumbnail = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), imageUri);
                          // image.setImageBitmap(thumbnail);
                           imageUri =  getImageUri(MainActivity.this, thumbnail);
                           String Url = thumbnail.toString();


                           // imageurl = getRealPathFromURI(imageUri);
                            Intent cameraintent =  new Intent(MainActivity.this,CropperActivity.class);
                            cameraintent.putExtra("Value", imageUri.toString());
                            startActivityForResult(cameraintent,101);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
        }

        if(resultCode==RESULT_OK &&requestCode== 100){
            String result = data.getStringExtra("RESULT");
            Log.e("Hello","hii");
            Uri resulturi = null;
            if(result!=null){
                resulturi = Uri.parse(result);
                Log.e("Hello",String.valueOf(resulturi));
                image.setImageURI(resulturi);
                drawable = (BitmapDrawable)image.getDrawable();
                bitmapsave = drawable.getBitmap();
//                ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
//                File file  = contextWrapper.getDir("images",MODE_PRIVATE);
//                String filename = String.format("%d.jpg",System.currentTimeMillis());
//
//                file = new File(file,filename);
//                OutputStream outputStream = null;
//                try {
//                    outputStream = new FileOutputStream(file);
//                    bitmapsave.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
//                    Log.e("SaveImage","imagesaved");
//                    Toast.makeText(MainActivity.this,"ImageSaved",Toast.LENGTH_SHORT).show();
//                    outputStream.flush();
//                    outputStream.close();
//                   // Uri savedImageURI = Uri.parse(file.getAbsolutePath());
//                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                    intent.setData(Uri.fromFile(file));
//                   sendBroadcast(intent);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }






//                hii
//
                FileOutputStream outputStream = null;
                File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//                File directory =  new File(storage.getAbsolutePath()+"/NewFolderImages");
//                directory.mkdir();
                String filename = String.format("%d.jpg",System.currentTimeMillis());
                File outFile = new File(storage,filename);
                try {
                    outputStream = new FileOutputStream(outFile);
                    bitmapsave.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
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
        if(resultCode==RESULT_OK&&requestCode==101){
            String resu = data.getStringExtra("RESULT");
            Log.e("HelloWorld","hiii");
            Uri resUri = null;
            if(resu!=null){
                resUri = Uri.parse(resu);
                image.setImageURI(resUri);
                drawable = (BitmapDrawable)image.getDrawable();
                bitmapsave = drawable.getBitmap();
                FileOutputStream outputStream = null;
                File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String filename = String.format("%d.jpg",System.currentTimeMillis());
                File outFile = new File(storage,filename);
                try {
                    outputStream = new FileOutputStream(outFile);
                    bitmapsave.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
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
//    private Uri saveImage(Bitmap image, Context context) {
//        File imagesfolder = new File(context.getCacheDir(),"rohitimages");
//        Uri uri = null;
//        try {
//            imagesfolder.mkdir();
//            File file = new File(imagesfolder,"captured_image.jpg");
//            FileOutputStream stream = new FileOutputStream(file);
//
//            image.compress(Bitmap.CompressFormat.JPEG,100,stream);
//            stream.flush();
//            stream.close();
//             uri = FileProvider.getUriForFile(context.getApplicationContext(),"com.example.imagecropper"+".provider",file);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return uri;
//    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
//    public String getRealPathFromURI(Uri contentUri) {
//        String[] proj = { MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
//        int column_index = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }
}