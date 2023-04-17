package com.fypvpreventor.fypvpreventor.viewmodels

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaRecorder
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.fypvpreventor.VpreventorFYP.AudioListAdapter
import com.fypvpreventor.VpreventorFYP.database.ContactDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RecordingController(private val activity: Activity, private val contactDao:ContactDao) {
    val fileName = MutableLiveData<String>()
    private var mediaRecorder: MediaRecorder? = null
    private var recordFile: String? = null
    private val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1
    private var timer: Timer? = null
    private var audioFile: File? = null

    fun startRecording() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION
            )
            return
        }

        // Check if the device has a microphone
        if (!activity.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            Toast.makeText(activity, "Your device does not have a microphone.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Check if the microphone is available
        val audioManager = activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioManager.mode == AudioManager.MODE_IN_CALL || audioManager.mode == AudioManager.MODE_IN_COMMUNICATION) {
            Toast.makeText(
                activity,
                "The microphone is currently in use by another application.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Start recording audio
        val recordPath = activity.getExternalFilesDir("/")!!.absolutePath
        val formatter = SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA)
        val now = Date()
        recordFile = "Recording_" + formatter.format(now) + ".mp3"

        fileName.value = "Recording, File Name: $recordFile"

        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder!!.setOutputFile("$recordPath/$recordFile")
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        try {
            mediaRecorder!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mediaRecorder!!.start()

        // Start a timer to stop recording after 20 seconds
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                stopRecording()
            }
        }, 20000)
    }


    fun stopRecording() {
        mediaRecorder?.stop()
        mediaRecorder?.release()

        // Cancel the timer
        timer?.cancel()
        timer = null
    }
}



