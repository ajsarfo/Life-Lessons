package com.sarftec.lifelessons.application.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.sarftec.lifelessons.application.binding.AboutDialogBinding
import com.sarftec.lifelessons.databinding.LayoutAboutDialogBinding

class AboutDialog(
    aboutBinding: AboutDialogBinding,
    parent: ViewGroup
) : AlertDialog(parent.context) {

    init {
        with(
            LayoutAboutDialogBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        ) {
            setView(root)
            binding = aboutBinding
            executePendingBindings()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismiss()
    }
}