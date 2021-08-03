package com.sarftec.lifelessons.application.splash

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView

class Font(
    private val context: Context,
    private val fontName: String,
    private val fontStyle: Int? = Typeface.BOLD,
    private val fontSize: Int = 32
) {
    fun setTypefaceAndShadows(
        textView: TextView,
        shadowRadius: Float = 5f,
        shadowDx: Float = 2f,
        shadowDy: Float = 2f,
        shadowColor: Int = android.graphics.Color.BLACK
    ) {
        val viewTypeface = Typeface.createFromAsset(context.assets, "fonts/$fontName")
        textView.apply {
            fontStyle?.let { setTypeface(viewTypeface, it) } ?: setTypeface(viewTypeface)
            setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor)
            textSize = fontSize.toFloat()
        }
    }
}