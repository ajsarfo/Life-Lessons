package com.sarftec.lifelessons.application.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.appodeal.ads.Appodeal
import com.appodeal.ads.Native
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.ACTIVITY_BUNDLE
import com.sarftec.lifelessons.application.ApplicationScope
import com.sarftec.lifelessons.application.adapter.MainItemAdapter
import com.sarftec.lifelessons.application.binding.AboutDialogBinding
import com.sarftec.lifelessons.application.binding.MainToolbarBinding
import com.sarftec.lifelessons.application.dialog.AboutDialog
import com.sarftec.lifelessons.application.dialog.LoadingDialog
import com.sarftec.lifelessons.application.enums.Destination
import com.sarftec.lifelessons.application.file.vibrate
import com.sarftec.lifelessons.application.manager.AppReviewManager
import com.sarftec.lifelessons.application.manager.InterstitialManager
import com.sarftec.lifelessons.application.viewmodel.MainViewModel
import com.sarftec.lifelessons.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject
    lateinit var applicationScope: ApplicationScope

    private val binding by lazy {
        ActivityMainBinding.inflate(
            LayoutInflater.from(this)
        )
    }

    private val interstitialManager by lazy {
        InterstitialManager(
            this,
            networkManager,
            listOf(1, 3, 4, 3)
        )
    }

    private val aboutDialog by lazy {
        AboutDialog(
            AboutDialogBinding(dependency).also {
                it.init()
            },
            binding.root
        )
    }

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    private val mainItemAdapter by lazy {
        MainItemAdapter(dependency, viewModel)
    }

    private val viewModel by viewModels<MainViewModel>()

    fun configureToolbarLayout() {
        with(binding.toolbarLayout) {
            binding = MainToolbarBinding(
                dependency,
                {
                    vibrate()
                    navigate(Destination.FAVORITE)
                },
                {
                    vibrate()
                    aboutDialog.show()
                }
            ).also {
                it.init()
            }
            executePendingBindings()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //Initialize appodeal sdk
        Appodeal.setBannerViewId(R.id.main_banner)
        Appodeal.initialize(
            this,
            getString(R.string.appodeal_id),
            Appodeal.BANNER_VIEW or Appodeal.INTERSTITIAL
        )
        loadingDialog.show()
        configureToolbarLayout()
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = mainItemAdapter
        }
        viewModel.quotes.observe(this) {
            mainItemAdapter.submitData(it)
            loadingDialog.dismiss()
        }
        with(AppReviewManager(this)) {
            init()
            lifecycleScope.launchWhenCreated {
                triggerReview()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Appodeal.show(this, Appodeal.BANNER_VIEW)
        container.reset()
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            applicationScope.execute()
        }
    }

    override fun onPause() {
        super.onPause()
        Appodeal.hide(this, Appodeal.BANNER_VIEW)
    }

    override fun onDestroy() {
        imageLoader.destroy()
        super.onDestroy()
    }

    override fun navigate(destination: Destination) {
        when (destination) {
            Destination.LIST -> interstitialManager.showAd {
                startActivity(
                    Intent(this, ListActivity::class.java).apply {
                        viewModel.getBundle()?.let { putExtra(ACTIVITY_BUNDLE, it) }
                    }
                )
            }
            else -> startActivity(Intent(this, FavoriteActivity::class.java))
        }
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}