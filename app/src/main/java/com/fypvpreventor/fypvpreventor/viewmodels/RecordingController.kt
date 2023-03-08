package com.fypvpreventor.fypvpreventor.viewmodels

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.SystemClock
import android.widget.Chronometer
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RecordingController(application: Application) : AndroidViewModel(application) {

    val fileName = MutableLiveData<String>()
    private var mediaRecorder: MediaRecorder? = null
    private var recordFile: String? = null
    private val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1

    fun startRecording() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(getApplication(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION)
            return
        }

        val recordPath = getApplication<Application>().getExternalFilesDir("/")!!.absolutePath
        val formatter = SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA)
        val now = Date()
        recordFile = "Recording_" + formatter.format(now) + ".3gp"

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
    }
    override fun onCleared() {
        super.onCleared()
        mediaRecorder?.stop()
        mediaRecorder?.release()
    }

}

