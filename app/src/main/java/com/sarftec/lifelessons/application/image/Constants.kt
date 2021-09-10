package com.sarftec.lifelessons.application.image

import android.net.Uri

const val GRADIENT_IMAGE_FOLDER = "gradients"
const val PICTURE_IMAGE_FOLDER = "images"
const val PLACEHOLDER_IMAGE_FOLDER = "placeholders"
const val APP_FOLDER = "app"

fun String.toAssetUri(folder: String) : Uri {
    return Uri.parse("file:///android_asset/$folder/$this")
}