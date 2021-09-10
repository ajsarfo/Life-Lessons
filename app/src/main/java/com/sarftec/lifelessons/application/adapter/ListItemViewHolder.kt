package com.sarftec.lifelessons.application.adapter

import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lifelessons.application.binding.ListItemBinding
import com.sarftec.lifelessons.application.file.saveBitmapToGallery
import com.sarftec.lifelessons.application.file.toBitmap
import com.sarftec.lifelessons.application.file.toast
import com.sarftec.lifelessons.application.file.viewInGallery
import com.sarftec.lifelessons.application.tools.PermissionHandler
import com.sarftec.lifelessons.data.database.entity.Quote
import com.sarftec.lifelessons.databinding.LayoutListItemBinding

class ListItemViewHolder(
    private val binding: LayoutListItemBinding,
    private val permissionHandler: PermissionHandler
) : RecyclerView.ViewHolder(binding.root) {

    val onCapture : () -> Unit = {
        val context = binding.root.context
        permissionHandler.requestReadWrite {
            binding.captureFrame.toBitmap { bitmap ->
                context.saveBitmapToGallery(bitmap)?.let { imageUri ->
                    context.toast("Saved to gallery")
                    context.viewInGallery(imageUri)
                }
            }
        }
    }

    fun bind(quote: Quote, position: Int, capsule: Capsule) {
        binding.binding = ListItemBinding(quote, position, capsule, onCapture).also {
            it.init()
        }
    }
}