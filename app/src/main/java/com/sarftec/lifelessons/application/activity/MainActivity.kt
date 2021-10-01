package com.sarftec.lifelessons.application.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.adapter.MainItemAdapter
import com.sarftec.lifelessons.application.binding.AboutDialogBinding
import com.sarftec.lifelessons.application.binding.MainToolbarBinding
import com.sarftec.lifelessons.application.dialog.AboutDialog
import com.sarftec.lifelessons.application.dialog.LoadingDialog
import com.sarftec.lifelessons.application.file.vibrate
import com.sarftec.lifelessons.application.manager.AppReviewManager
import com.sarftec.lifelessons.application.manager.BannerManager
import com.sarftec.lifelessons.application.viewmodel.MainViewModel
import com.sarftec.lifelessons.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(
            LayoutInflater.from(this)
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
        MainItemAdapter(dependency, viewModel) { item ->
            interstitialManager?.showAd {
                navigateTo(
                    ListActivity::class.java,
                    bundle = Bundle().apply {
                        putString(CATEGORY_SELECTED_NAME, item.category)
                    }
                )
            }
        }
    }

    private val viewModel by viewModels<MainViewModel>()

    fun configureToolbarLayout() {
        with(binding.toolbarLayout) {
            binding = MainToolbarBinding(
                dependency,
                {
                    vibrate()
                    navigateTo(FavoriteActivity::class.java)
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
        statusColor(ContextCompat.getColor(this, R.color.main_status))
        setStatusBarBackgroundLight()
        /*************** Admob Configuration ********************/
        BannerManager(this, adRequestBuilder).attachBannerAd(
            getString(R.string.admob_banner_main),
            binding.mainBanner
        )
        /**********************************************************/
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

    override fun onBackPressed() {
        finish()
    }
}