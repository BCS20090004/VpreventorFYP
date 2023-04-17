package com.fypvpreventor.VpreventorFYP;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class receive extends AppCompatActivity {
    private int seconds = 0;

    @SuppressLint({"WrongThread", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }

        ImageView rej = findViewById(R.id.reject);
        rej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        BitmapDrawable drawable = (BitmapDrawable) FakeCall.IVPreviewImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        CircleImageView image2 =  findViewById(R.id.cont);
        image2.setImageBitmap(bmp);
        TextView name = findViewById(R.id.tf1);
        name.setText(""+FakeCall.s1);
        TextView no = findViewById(R.id.tf2);
        no.setText("+60 "+FakeCall.s2);
        runTimer();

    }
    private void runTimer()
    {
        TextView times = findViewById(R.id.time);
        final Handler handler
                = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run()
            {
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(),"%02d:%02d",minutes, secs);
                times.setText(time);

                // If running is true, increment the
                // seconds variable.

                seconds++;


                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000);
            }
        });
    }
}