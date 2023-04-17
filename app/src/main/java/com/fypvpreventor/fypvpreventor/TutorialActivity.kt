package com.fypvpreventor.VpreventorFYP

import android.graphics.PixelFormat
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class TutorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        window.setFormat(PixelFormat.UNKNOWN)

        //displays a video file
        val mVideoView2: VideoView = findViewById(R.id.videoView2)

        val uriPath2 = "android.resource://com.fypvpreventor.VpreventorFYP/" + R.raw.video
        val uri2 = Uri.parse(uriPath2)
        mVideoView2.setVideoURI(uri2)
        mVideoView2.requestFocus()
        mVideoView2.start()


        val mediaController = MediaController(this)
        mVideoView2.setMediaController(mediaController)
        mediaController.setAnchorView(mVideoView2)
    }
}
