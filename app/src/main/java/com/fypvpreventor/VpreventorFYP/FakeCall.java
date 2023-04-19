package com.fypvpreventor.VpreventorFYP;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class FakeCall extends AppCompatActivity {
    static public   String s1 ,s2;
    public static Ringtone r;
    public static int sche = 0;
    public static int sound = 0;
    public static ImageView IVPreviewImage;
    int SELECT_PICTURE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_call);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        //EditText e1 ,e2;
        //e1 = findViewById(R.id.name2);
        //e2 = findViewById(R.id.number2);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Button b = findViewById(R.id.button);
        IVPreviewImage = findViewById(R.id.profile_image);
        ImageView ch = findViewById(R.id.imageView2);
        ImageView ring = findViewById(R.id.imageView3);
        ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri currentTone= RingtoneManager.getActualDefaultRingtoneUri(FakeCall.this, RingtoneManager.TYPE_ALARM);
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                startActivityForResult(intent, 999);
            }
        });

        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FakeCall.sche = 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        Intent i = new Intent(FakeCall.this,call.class);
                        startActivity(i);
                        finish();
                    }
                },500);
                s1 = "MS Tina";
                s2 = "0108089521";

              //  if(!s1.isEmpty() && !s2.isEmpty()){
                   //Intent i = new Intent(FakeCall.this,schedule.class);
                   //startActivity(i);
              //  }
               // else{
               //     Toast.makeText(FakeCall.this , "FILL NAME AND NUMBER" , Toast.LENGTH_SHORT).show();
               // }

            }
        });
    }

    private void imageChooser()
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                            IVPreviewImage.setImageBitmap(
                                    selectedImageBitmap);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    //selected ringtune saving method

    @SuppressLint("SetTextI18n")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            //this thing will convert your selected tune to ringtune.
//            RingtoneManager.setActualDefaultRingtoneUri( this,
//                   RingtoneManager.TYPE_RINGTONE,
//                   uri
//          );
            r = RingtoneManager.getRingtone(getApplicationContext(), uri);
            sound = 1;
        }
    }

}