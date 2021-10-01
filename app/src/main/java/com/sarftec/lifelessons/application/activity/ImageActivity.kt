package com.sarftec.lifelessons.application.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.adapter.ImageAdapter
import com.sarftec.lifelessons.application.dialog.LoadingScreen
import com.sarftec.lifelessons.application.file.vibrate
import com.sarftec.lifelessons.application.manager.BannerManager
import com.sarftec.lifelessons.application.tools.ImageHandler
import com.sarftec.lifelessons.databinding.ActivityImageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class ImageActivity : BaseActivity() {

    private val binding by lazy {
        ActivityImageBinding.inflate(
            LayoutInflater.from(this)
        )
    }

    private lateinit var imageHandler: ImageHandler

    private val imageAdapter by lazy {
        ImageAdapter(
            dependency,
            imageHandler,
            imageStore.placeholderImages(this) +
                    imageStore.gradientImages()
        ) { action, uri ->
           vibrate()
            val intent = Intent()
            intent.putExtra(QuoteActivity.IMAGE_URI, uri.toString())
            intent.putExtra(QuoteActivity.IMAGE_ACTION, action)
            setResult(RESULT_OK, intent)
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private val scrimDialog by lazy {
        LoadingScreen(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        statusColor(ContextCompat.getColor(this, R.color.color_primary))
        /*************** Admob Configuration ********************/
        BannerManager(this, adRequestBuilder).attachBannerAd(
            getString(R.string.admob_banner_image),
            binding.mainBanner
        )
        /**********************************************************/
        lifecycleScope.launchWhenCreated {
            scrimDialog.show()
            delay(1000)
            scrimDialog.dismiss()
        }
        imageHandler = ImageHandler(this)
        binding.imageToolbar.setNavigationOnClickListener {
           vibrate()
            onBackPressed()
        }
        binding.imageRecycler.apply {
            layoutManager = GridLayoutManager(this@ImageActivity, 3)
            adapter = imageAdapter
            setHasFixedSize(true)
        }
    }
}