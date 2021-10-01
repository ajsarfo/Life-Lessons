package com.sarftec.lifelessons.application.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.enums.Destination
import com.sarftec.lifelessons.databinding.LayoutLoadingDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

@AndroidEntryPoint
class LoadActivity : BaseActivity() {

    private val dialogBinding by lazy {
        LayoutLoadingDialogBinding.inflate(
            LayoutInflater.from(this)
        )
    }

    override fun canShowInterstitial(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLoadingDialog()
        lifecycleScope.launchWhenCreated {
            repository.setupDatabase()
            delay(1000)
            navigateTo(SplashActivity::class.java, true, R.anim.no_anim, R.anim.no_anim)
        }
    }

    private fun showLoadingDialog() {
        object : AlertDialog(this) {}.apply {
            setView(dialogBinding.root)
            show()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}