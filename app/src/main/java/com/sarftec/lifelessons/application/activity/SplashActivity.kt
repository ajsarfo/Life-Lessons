package com.sarftec.lifelessons.application.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.enums.Destination
import com.sarftec.lifelessons.application.splash.Splash
import com.sarftec.lifelessons.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private val binding by lazy {
     ActivitySplashBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val splashItem = Splash(this).fetchSplashItem()
        binding.apply {
            layout.setBackgroundColor(splashItem.background.first)
            message.apply {
                splashItem.font.setTypefaceAndShadows(this)
                text = splashItem.quote.first
                setTextColor(splashItem.background.second)
            }
            initial.apply {
                text = splashItem.quote.second
                setTextColor(splashItem.background.second)
            }
        }
        navigationColor(splashItem.background.first)
        statusColor(splashItem.background.first)

        lifecycleScope.launchWhenCreated {
           val timeSpent = measureTimeMillis {
               with(mutableListOf<Uri>()) {
                   addAll(imageStore.gradientImageUris())
                   addAll(imageStore.pictureImageUris())
                   imageLoader.preloadImages(this)
               }
           }
            (TimeUnit.SECONDS.toMillis(3) - timeSpent).let { if(it > 0) delay(it) }
            navigate(Destination.MAIN)
        }
    }

    override fun navigate(destination: Destination) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}