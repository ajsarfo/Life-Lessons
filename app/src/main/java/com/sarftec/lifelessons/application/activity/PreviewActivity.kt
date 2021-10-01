package com.sarftec.lifelessons.application.activity

import android.app.WallpaperManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.core.content.ContextCompat
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.file.*
import com.sarftec.lifelessons.application.manager.BannerManager
import com.sarftec.lifelessons.application.tools.PermissionHandler
import com.sarftec.lifelessons.application.viewmodel.PreviewViewModel
import com.sarftec.lifelessons.databinding.ActivityPreviewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.InputStream

@AndroidEntryPoint
class PreviewActivity : BaseActivity() {

    private val binding by lazy {
        ActivityPreviewBinding.inflate(
            LayoutInflater.from(this)
        )
    }

    private val viewModel by viewModels<PreviewViewModel>()

    private lateinit var permissionHandler: PermissionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        /*************** Admob Configuration ********************/
        BannerManager(this, adRequestBuilder).attachBannerAd(
            getString(R.string.admob_banner_preview),
            binding.mainBanner
        )
        /**********************************************************/
        statusColor(ContextCompat.getColor(this, R.color.color_primary))
        //Show banner
        permissionHandler = PermissionHandler(this)
        intent.getBundleExtra(ACTIVITY_BUNDLE)?.let {
            viewModel.bundle = it
        }

        binding.previewToolbar.setNavigationOnClickListener {
            vibrate()
            onBackPressed()
        }
        viewModel.getImageName()?.let { name ->
            binding.previewImage.setImageURI(
                Uri.fromFile(File(cacheDir, name))
            )
        }
        binding.previewSave.setOnClickListener {
            vibrate()
            if (viewModel.imageUri != null) return@setOnClickListener
            val imageName = viewModel.getImageName()
            permissionHandler.requestReadWrite {
                savePicture { uri, outputStream ->
                    if (imageName == null || outputStream == null) return@savePicture
                    viewModel.imageUri = uri
                    val file = File(cacheDir, imageName)
                    try {
                        file.inputStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                        toast("Image saved to gallery")
                        binding.previewView.visibility = View.VISIBLE
                    } catch (e: Exception) {
                        toast("Error occurred!. Try again")
                    }
                }
            }
        }

        binding.previewShare.setOnClickListener {
            vibrate()
            viewModel.getImageName()?.let { imageName ->
                //Assumes file exists in the cacheDir
                shareImage(imageName)
            }
        }

        binding.previewApply.setOnClickListener {
            vibrate()
            showDropDownMenu(it, R.menu.menu_drop_down)
        }

        binding.previewView.visibility = if (viewModel.imageUri == null) View.GONE else View.VISIBLE
        binding.previewView.setOnClickListener {
            vibrate()
            viewModel.imageUri?.let {
                viewInGallery(it)
            }
        }
    }

    private fun showDropDownMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this, v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.home_screen -> {
                    setLockScreenOrWallpaper { wallpaperManager, inputStream ->
                        wallpaperManager.setStream(inputStream)
                        toast("Image set as wallpaper")
                    }
                    true
                }
                R.id.lock_screen -> {
                    setLockScreenOrWallpaper { wallpaperManager, inputStream ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                           wallpaperManager.setBitmap(
                                BitmapFactory.decodeStream(inputStream),
                                null,
                                true,
                                WallpaperManager.FLAG_LOCK
                            )
                            toast("Image set as lock screen")
                        } else toast("Error occurred!")
                    }
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun setLockScreenOrWallpaper(action: (WallpaperManager, InputStream) -> Unit) {
        val wallpaperManager = WallpaperManager.getInstance(this) ?: let {
            toast("Error occurred!")
            return
        }
        viewModel.getImageName()?.let { imageName ->
            File(cacheDir, imageName).inputStream().use { stream ->
                action(wallpaperManager, stream)
            }
        }
    }
}