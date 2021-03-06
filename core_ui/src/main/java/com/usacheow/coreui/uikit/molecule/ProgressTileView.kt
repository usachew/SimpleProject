package com.usacheow.coreui.uikit.molecule

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.usacheow.coreui.R
import com.usacheow.coreui.adapters.base.Populatable
import com.usacheow.coreui.databinding.ViewProgressTileBinding
import com.usacheow.coreui.utils.view.color

class ProgressTileView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr), Populatable<ProgressTileItem> {

    private val binding by lazy { ViewProgressTileBinding.inflate(LayoutInflater.from(context), this, true) }

    override fun populate(model: ProgressTileItem) {
        val layoutParams = binding.progressView.layoutParams as LinearLayoutCompat.LayoutParams
        binding.progressView.layoutParams = layoutParams.apply { weight = model.currentValue.toFloat() }
        binding.progressView.backgroundTintList = ColorStateList.valueOf(color(model.colorResId))
        binding.progressLayout.weightSum = model.maxValue.toFloat()
    }
}

data class ProgressTileItem(
    val maxValue: Int,
    val currentValue: Int,
    @ColorRes val colorResId: Int = R.color.success,
)