package com.fypvpreventor.VpreventorFYP;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Transaction;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;


public class Record extends Fragment implements View.OnClickListener {

    private int PERMISSION_CODE = 21;
    private NavController navController;
    private ImageButton listbtn;
    private ImageButton recordbtn;
    private boolean isRecording = false;
    private MediaRecorder mediaRecorder;
    private String recordFile;
    private Chronometer timer;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;

    private TextView fileName;
    public Record (){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        navController= Navigation.findNavController(view);
        listbtn = view.findViewById(R.id.record_list_btn);
        recordbtn =view.findViewById(R.id.record_btn);
        timer =view.findViewById(R.id.record_timer);
        fileName = view.findViewById(R.id.record_filename);

        listbtn.setOnClickListener(this);
        recordbtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.record_list_btn:
                if(isRecording){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            navController.navigate(R.id.action_record_to_audiolist);
                            isRecording = false;
                        }
                    });
                    alertDialog.setTitle("Audio still recording");
                    alertDialog.setMessage("Are you sure, you want to stop recording?");
                    alertDialog.create().show();
                }else {}

                navController.navigate(R.id.action_record_to_audiolist);
                break;

            case R.id.record_btn:
              if (isRecording){
                  //stoprecord
                  stopRecording();
                  recordbtn.setImageDrawable(getResources().getDrawable(R.drawable.baseline_mic_none_24,null));
                  isRecording=false;
              } else {
                  //isrecord
                  if(checkPermissions()) {
                      startRecording();
                      recordbtn.setImageDrawable(getResources().getDrawable(R.drawable.baseline_mic_24, null));
                      isRecording = true;
                  }
              }
                break;
        }
    }

    public boolean startRecording() {
        boolean success =false;
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now = new Date();
        recordFile="Recording_"+formatter.format(now)+".3gp";

        fileName.setText("Recording, File Name: " + recordFile);

        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath+"/"+ recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(startRecording()) {
            success=true;
        }

        mediaRecorder.start();
        return success;
    }

    public boolean stopRecording() {
        boolean success =false;

        timer.stop();

        fileName.setText("Recording Stopped, File Saved: " + recordFile);

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder=null;

        if(stopRecording()) {
            success=true;
        }
        return success;
    }

    private boolean checkPermissions() {
        if(ActivityCompat.checkSelfPermission(getContext(), recordPermission) == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isRecording){
            stopRecording();
        }
    }
}