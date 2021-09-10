package com.sarftec.lifelessons.application.panel

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.databinding.LayoutTextPanelBinding
import kotlinx.coroutines.CoroutineScope

class TextPanelManager(
    coroutineScope: CoroutineScope,
    private val listener: PanelListener,
    private val layout: LayoutTextPanelBinding
) : PanelListener by listener {

    private val headers = listOf(
        layout.readerHeaderSize to layout.readerTextsizeSublayoutId,
        layout.readerHeaderColor to layout.readerTextcolorSublayoutId,
        layout.readerHeaderFont to layout.readerTextfontSublayoutId,
        layout.readerHeaderAlignment to layout.readerTextAlignemntSublayoutId,
        layout.readerHeaderBgOpacity to layout.readerBgopacitySublayoutId
    )

    private var current = headers.first()

    private val selectedTextColor by lazy {
        ContextCompat.getColor(
            layout.root.context,
            R.color.reader_text_highlightcolor
        )
    }

    private val unselectedTextColor by lazy {
        ContextCompat.getColor(
            layout.root.context,
            R.color.reader_text_color
        )
    }

    init {
        headers.forEach { pair ->
            pair.first.setOnClickListener {
                (it as TextView).setTextColor(selectedTextColor)
                current.first.setTextColor(unselectedTextColor)
                switchLayout(pair)
                current = pair
            }
        }

        layout.readerCloseButton.setOnClickListener {
            layout.layoutExtraPanel.visibility = View.GONE
        }

        ColorManager(coroutineScope, this, layout)
        AlignmentManager(this, layout)
        FontManager(coroutineScope, this, layout)
        OpacityManager(this, layout)
        SizeManager(this, layout)
    }

    private fun switchLayout(pair: Pair<TextView, LinearLayout>) {
        current.second.visibility = View.GONE
        pair.second.visibility = View.VISIBLE
    }

    fun show() {
        layout.layoutExtraPanel.visibility = View.VISIBLE
    }
}