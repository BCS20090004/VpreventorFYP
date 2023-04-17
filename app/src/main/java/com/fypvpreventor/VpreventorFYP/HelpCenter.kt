package com.fypvpreventor.VpreventorFYP

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

class HelpCenterActivity : AppCompatActivity() {


    private lateinit var sliderView: SliderView
    private val images = intArrayOf(
        R.drawable.page1,
        R.drawable.page2,
        R.drawable.page3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_center)

        sliderView = findViewById(R.id.image_slider)

        val sliderAdapter = SliderAdapter(images)

        sliderView.setSliderAdapter(sliderAdapter)
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)
        sliderView.startAutoCycle()
    }
}
