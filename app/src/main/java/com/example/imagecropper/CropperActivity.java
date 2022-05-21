package com.example.imagecropper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class CropperActivity extends AppCompatActivity {
String result;
Uri fileuri;
String resultcamera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("SecondActivity");
        setContentView(R.layout.activity_cropper);

        readintent();
        UCrop.Options options = new UCrop.Options();

        String dest_Uri = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();

        UCrop.of(fileuri,Uri.fromFile(new File(getCacheDir(),dest_Uri))).withOptions(options).withAspectRatio(0,0).useSourceImageAspectRatio().withMaxResultSize(2000,2000).start(CropperActivity.this);

    }

    private void readintent() {
        Intent intent = getIntent();
        if(intent.getExtras()!=null){
            result = intent.getStringExtra("DATA");
            resultcamera = intent.getStringExtra("Value");
            if(result!=null){

                fileuri = Uri.parse(result);
                Log.e("Gallery",fileuri.toString());
            }
            else if(resultcamera!=null){
                fileuri = Uri.parse(resultcamera);
                Log.e("Camera",fileuri.toString());
            }



        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==UCrop.REQUEST_CROP){
            final Uri resulturi = UCrop.getOutput(data);
            Log.e("FinalRes",String.valueOf(resulturi));
            Intent returnintent = new Intent();
            returnintent.putExtra("RESULT",resulturi.toString());
            setResult(RESULT_OK,returnintent);
            finish();
        }
        else if(resultCode==UCrop.RESULT_ERROR){
            final Throwable croperror = UCrop.getError(data);

        }
        else{

            finish();
        }

    }
}