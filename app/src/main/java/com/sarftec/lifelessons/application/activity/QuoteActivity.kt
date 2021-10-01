package com.sarftec.lifelessons.application.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.adapter.QuotePagerAdapter
import com.sarftec.lifelessons.application.dialog.ScrimDialog
import com.sarftec.lifelessons.application.file.copy
import com.sarftec.lifelessons.application.file.toBitmap
import com.sarftec.lifelessons.application.file.toast
import com.sarftec.lifelessons.application.file.vibrate
import com.sarftec.lifelessons.application.manager.AdCountManager
import com.sarftec.lifelessons.application.manager.BannerManager
import com.sarftec.lifelessons.application.model.QuoteBottomPanel
import com.sarftec.lifelessons.application.panel.TextPanelManager
import com.sarftec.lifelessons.application.viewmodel.QuoteBackground
import com.sarftec.lifelessons.application.viewmodel.QuoteViewModel
import com.sarftec.lifelessons.databinding.ActivityQuoteBinding
import com.sarftec.lifelessons.databinding.LayoutTextPanelBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class QuoteActivity : BaseActivity(), QuoteBottomPanel.Listener, ColorPickerDialogListener {

    private lateinit var binding: ActivityQuoteBinding

    private val viewModel by viewModels<QuoteViewModel>()

    private val quotePagerAdapter by lazy {
        QuotePagerAdapter(lifecycleScope)
    }

    private val quoteBottomPanel by lazy {
        QuoteBottomPanel(this)
    }

    private val textPanelManager by lazy {
        TextPanelManager(
            lifecycleScope,
            viewModel,
            LayoutTextPanelBinding.inflate(
                layoutInflater,
                binding.textPanelContainer,
                true
            )
        )
    }

    private val scrimDialog by lazy {
        ScrimDialog(this)
    }

    private lateinit var imageResultLauncher: ActivityResultLauncher<Intent>

    override fun createAdCounterManager(): AdCountManager {
        return AdCountManager(
            listOf(2, 3, 4, 3)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*************** Admob Configuration ********************/
        BannerManager(this, adRequestBuilder).attachBannerAd(
            getString(R.string.admob_banner_quote),
            binding.mainBanner
        )
        /**********************************************************/
        savedInstanceState ?: kotlin.run {
            intent.getBundleExtra(ACTIVITY_BUNDLE)?.let {
                viewModel.setBundle(it)
            }
        }
        window.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
        configureQuotePager()
        configureBottomSelection()
        configureImageResultLauncher()
        configureBackgroundImage()
        viewModel.fetch()
        viewModel.indexedQuotes.observe(this) {
            quotePagerAdapter.submitData(it.quotes)
            binding.quotePager.setCurrentItem(it.index, false)
        }
        viewModel.currentQuote.observe(this) { currentQuote ->
            quoteBottomPanel.setQuote(currentQuote)
        }
        viewModel.panelState.observe(this) { state ->
            if (state.opacity != -1) binding.quoteImageOverlay.setBackgroundColor(state.opacity)
            quotePagerAdapter.changePanelState(state)
        }
        // statusColor(ContextCompat.getColor(this, R.color.color_primary))
    }

    private fun showColorPicker() {
        lifecycleScope.launch {
            scrimDialog.show()
            delay(500)
            scrimDialog.dismiss()
            ColorPickerDialog
                .newBuilder()
                .show(this@QuoteActivity)
        }
    }

    private fun loadAndSetImage(uri: Uri) {
        lifecycleScope.launch {
            scrimDialog.show()
            delay(500)
            scrimDialog.dismiss()
            binding.quoteBackground.load(uri, imageLoader.imageLoader) {
                allowHardware(false)
            }
        }
    }

    private fun configureImageResultLauncher() {
        imageResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { activityResult ->
            activityResult.data?.apply {
                getStringExtra(IMAGE_ACTION)?.let {
                    if (it == IS_COLOR) {
                        showColorPicker()
                    } else if (it == IS_IMAGE) {
                        getStringExtra(IMAGE_URI)?.let { uriString ->
                            loadAndSetImage(Uri.parse(uriString))
                        }
                    }
                }
            }
        }
    }

    private fun configureBottomSelection() {
        binding.bottomSelection.binding = quoteBottomPanel
        binding.bottomSelection.executePendingBindings()
    }

    private fun configureQuotePager() {
        binding.quotePager.apply {
            adapter = quotePagerAdapter
            registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        viewModel.setCurrentQuoteIndex(position)
                        super.onPageSelected(position)
                    }
                }
            )
        }
    }

    private fun configureBackgroundImage() {
        when (val item = viewModel.getQuoteBackground()) {
            is QuoteBackground.BackgroundImage -> setQuoteBackground(Uri.parse(item.path))
            is QuoteBackground.BackgroundColor -> binding.quoteBackground.setBackgroundColor(
                item.color
            )
            else -> setQuoteBackground(
                imageStore.randomGradientImage().also {
                    viewModel.setBackgroundImage(it)
                }
            )
        }
    }

    private fun setQuoteBackground(image: Uri) {
        binding.quoteBackground.load(
            image,
            imageLoader.imageLoader
        ) {
            allowHardware(false)
        }
    }

    /**
     * Jaredrummler color picker listeners
     */
    override fun onColorSelected(dialogId: Int, color: Int) {
        binding.quoteBackground.setImageDrawable(
            ColorDrawable(color)
        )
        viewModel.setBackgroundColor(color)
    }

    override fun onDialogDismissed(dialogId: Int) {

    }

    /**
     * Bottom panel listeners
     */
    override fun randomBackground() {
        setQuoteBackground(
            imageStore.randomGradientImage().also {
                viewModel.setBackgroundImage(it)
            }
        )
        vibrate()
    }

    override fun launchTextPanel() {
        vibrate()
        textPanelManager.show()
    }

    override fun chooseBackground() {
        vibrate()
        interstitialManager?.showAd {
            imageResultLauncher.launch(Intent(this, ImageActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun launchImagePreview() {
        vibrate()
        val imageName = "temp_image"
        val file = File.createTempFile(imageName, ".jpg", cacheDir)
        file.outputStream().use { outputStream ->
            binding.captureFrame.toBitmap {
                val compressed = it.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                if (compressed) outputStream.flush()
            }
        }
        interstitialManager?.showAd {
            navigateTo(
                PreviewActivity::class.java,
                bundle = Bundle().apply {
                    putString("imageName", file.name)
                }
            )
        }
    }

    override fun copy() {
        viewModel.getCurrentQuote()?.let {
            vibrate()
            toast("Copied to clipboard")
            copy(it.message, "label")
        }
    }

    override fun changeFavorite() {
        vibrate()
        viewModel.changeCurrentQuoteFavorite()
        viewModel.getCurrentQuote()?.let {
            toast(if (it.favorite) "Added to favorites" else "Removed from favorites")
        }
    }

    companion object {
        const val IMAGE_URI = "image_result"
        const val IMAGE_ACTION = "image_action"
        const val IS_COLOR = "is_color"
        const val IS_IMAGE = "is_image"
    }
}