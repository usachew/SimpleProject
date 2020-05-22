package com.usacheow.demo

import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.usacheow.coreuikit.fragments.SimpleFragment
import com.usacheow.coreuikit.utils.ext.PaddingValue
import com.usacheow.diprovider.DiProvider
import kotlinx.android.synthetic.main.fragment_fonts.fontsListView

class FontsFragment : SimpleFragment() {

    override val layoutId = R.layout.fragment_fonts

    companion object {
        fun newInstance() = FontsFragment()
    }

    override fun inject(diProvider: DiProvider) {
    }

    override fun onApplyWindowInsets(insets: WindowInsetsCompat, padding: PaddingValue) {
        fontsListView.updatePadding(
            top = insets.systemWindowInsetTop,
            bottom = insets.systemWindowInsetBottom
        )
    }
}