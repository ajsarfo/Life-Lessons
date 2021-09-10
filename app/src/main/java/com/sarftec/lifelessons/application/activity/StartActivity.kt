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
            val kclass = if(!repository.isDatabaseCreated()) LoadActivity::class.java
            else SplashActivity::class.java
            navigateTo(kclass, true, R.anim.no_anim, R.anim.no_anim)
        }
    }
}