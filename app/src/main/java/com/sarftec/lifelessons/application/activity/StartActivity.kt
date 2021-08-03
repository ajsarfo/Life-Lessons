package com.sarftec.lifelessons.application.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.enums.Destination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            if(!repository.isDatabaseCreated()) navigate(Destination.LOAD)
            else navigate(Destination.SPLASH)
        }
    }

    override fun navigate(destination: Destination) {
       val intent = when(destination) {
            Destination.LOAD -> Intent(this, LoadActivity::class.java)
           else -> Intent(this, SplashActivity::class.java)
        }
        startActivity(intent)
        finish()
        overridePendingTransition (R.anim.no_anim, R.anim.no_anim)
    }
}