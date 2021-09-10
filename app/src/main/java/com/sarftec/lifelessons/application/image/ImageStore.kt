package com.sarftec.lifelessons.application.image

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageStore @Inject constructor(
    @ApplicationContext context: Context
) {

    private val defaultImage = "default.jpg"

    private val gradientImages: Set<String> = context
        .assets
        .list(GRADIENT_IMAGE_FOLDER)!!
        .map { it.toLowerCase(Locale.ENGLISH) }
        .toHashSet()

    private val pictureImages: Set<String> = context
        .assets
        .list(PICTURE_IMAGE_FOLDER)!!
        .map { it.toLowerCase(Locale.ENGLISH) }
        .toHashSet()

    fun appImage(): Uri = "icon.jpg".toAssetUri(APP_FOLDER)

    fun placeholderImages(context: Context): List<Uri> {
        return context
            .assets
            .list(PLACEHOLDER_IMAGE_FOLDER)!!
            .map {
                it.toAssetUri(PLACEHOLDER_IMAGE_FOLDER)
            }
    }

    fun gradientImages(): List<Uri> = gradientImages.map { it.toAssetUri(GRADIENT_IMAGE_FOLDER) }

    fun randomGradientImage(): Uri {
        return gradientImages.random().toAssetUri(GRADIENT_IMAGE_FOLDER)
    }

    fun pictureImage(name: String): Uri {
        val image = "${name.replace(" ", "_").toLowerCase(Locale.ENGLISH)}.jpg"
        val picture = if (!pictureImages.contains(image)) defaultImage else image
        return picture.toAssetUri(PICTURE_IMAGE_FOLDER)
    }
}
