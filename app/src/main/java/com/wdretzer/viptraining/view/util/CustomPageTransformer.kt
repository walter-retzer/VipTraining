package com.wdretzer.viptraining.view.util

import android.content.Context
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.wdretzer.viptraining.R
import kotlin.math.abs


class CustomPageTransformer(context: Context) : ViewPager2.PageTransformer {

    private val nextItemVisiblePx =
        context.resources.getDimension(R.dimen.viewpager_next_item_visible)
    private val currentItemHorizontalMarginPx =
        context.resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
    private val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx

    override fun transformPage(view: View, position: Float) {
        view.apply {
            translationX = -pageTranslationX * position
            scaleY = 1 - (0.15f * abs(position))
            alpha = 0.75f + (1 - abs(position))
        }
    }
}
