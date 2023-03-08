package com.fypvpreventor.fypvpreventor.viewmodels

import android.os.SystemClock
import android.widget.Chronometer
import com.fypvpreventor.VpreventorFYP.Record
import java.util.Collections.list

class chronometerController( val chronometer: Chronometer) {

    fun start() {
        val record = Record()
        if (record.startRecording()==true) {
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.start()
        }
    }

    fun stop() {
        val record = Record()
        if (record.stopRecording()==true) {
            chronometer.stop()
        }
    }

    fun setFormat(format: String) {
        chronometer.format = format
    }

    fun setOnChronometerTickListener(listener: Chronometer.OnChronometerTickListener) {
        chronometer.setOnChronometerTickListener(listener)
    }
}
