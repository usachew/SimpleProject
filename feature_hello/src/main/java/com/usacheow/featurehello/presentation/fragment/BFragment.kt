package com.usacheow.featurehello.presentation.fragment

import android.os.Bundle
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.usacheow.coreui.adapters.ViewTypesAdapter
import com.usacheow.coreui.fragments.SimpleFragment
import com.usacheow.coreui.uikit.molecule.ListTileItem
import com.usacheow.coreui.utils.TextString
import com.usacheow.coreui.utils.view.PaddingValue
import com.usacheow.featurehello.R
import com.usacheow.featurehello.databinding.FragmentBBinding
import com.usacheow.featurehello.presentation.viewmodels.AViewModel
import com.usacheow.featurehello.presentation.viewmodels.BViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BFragment : SimpleFragment<FragmentBBinding>() {

    override val params = Params(
        viewBindingProvider = FragmentBBinding::inflate,
    )

    private val aViewModel by viewModels<AViewModel>({ requireParentFragment() })
    private val bViewModel by viewModels<BViewModel>()

    override fun onApplyWindowInsets(insets: WindowInsetsCompat, padding: PaddingValue) {
//        binding.listView.updatePadding(
//            bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
//        )
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        bViewModel.x++
        binding.header.root.apply {
            title = "B Fragment ${aViewModel.x} ${bViewModel.x}"
            setBackground(R.color.surfaceSecondary)
            setNavigationAction(R.drawable.ic_back) { activity?.onBackPressed() }
        }

        binding.listView.layoutManager = LinearLayoutManager(context)
        binding.listView.adapter = ViewTypesAdapter(listOf(
            ListTileItem(value = TextString("1 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("2 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("3 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("4 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("5 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("6 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("7 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("8 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("9 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("10 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("11 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("12 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("13 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("14 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("15 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("16 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("17 Go to next screen"), clickListener = ::openNextScreen),
            ListTileItem(value = TextString("18 Go to next screen"), clickListener = ::openNextScreen)
        ))
    }

    private fun openNextScreen() {
//        getContainer { show() }
    }
}