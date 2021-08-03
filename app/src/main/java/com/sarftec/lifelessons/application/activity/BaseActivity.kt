package com.sarftec.lifelessons.application.activity

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sarftec.lifelessons.application.Container
import com.sarftec.lifelessons.application.enums.Destination
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.Dependency
import com.sarftec.lifelessons.application.listener.NavigationListener
import com.sarftec.lifelessons.application.listener.StatusNavigationListener
import com.sarftec.lifelessons.application.image.BitmapImageLoader
import com.sarftec.lifelessons.application.image.ImageStore
import com.sarftec.lifelessons.application.manager.NetworkManager
import com.sarftec.lifelessons.data.repository.Repository
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), StatusNavigationListener, NavigationListener {

    //These are been used by dependency
    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var imageStore: ImageStore

    @Inject
    lateinit var imageLoader: BitmapImageLoader

    @Inject
    lateinit var container: Container

    @Inject
    lateinit var networkManager: NetworkManager
    /////////////////////////////////////

    protected val dependency by lazy {
        Dependency(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBackground()
    }

    protected fun removeStatusBarBackgroundLight() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                window.decorView.windowInsetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
        }
    }

    protected fun removeNavigationBackgroundLight() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                window.decorView.windowInsetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                )
            }
        }
    }

    private fun changeStatusBackground() {
        when(resources.configuration.uiMode and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> { }
            Configuration.UI_MODE_NIGHT_NO -> setStatusBarBackgroundLight()
            Configuration.UI_MODE_NIGHT_UNDEFINED -> { }
        }
    }

    protected fun setStatusBarBackgroundLight() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                window.decorView.windowInsetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun setNavigationBarBackgroundLight() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                window.decorView.windowInsetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun navigate(destination: Destination) {}


    override fun statusColor(color: Int) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = color
                if(color == Color.WHITE) setStatusBarBackgroundLight()
            }
        }
    }


    override fun navigationColor(color: Int) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                navigationBarColor = color
                if(color == Color.WHITE) setNavigationBarBackgroundLight()
            }
        }
    }
}