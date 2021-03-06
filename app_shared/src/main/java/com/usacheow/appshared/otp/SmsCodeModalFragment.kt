package com.usacheow.appshared.otp

import android.os.Bundle
import android.text.InputFilter
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.usacheow.appshared.R
import com.usacheow.appshared.databinding.FragmentSmsCodeBinding
import com.usacheow.coreui.fragments.SimpleModalFragment
import com.usacheow.coreui.utils.view.doOnClick
import dagger.hilt.android.AndroidEntryPoint

private const val CODE_LENGTH_KEY = "CODE_LENGTH_KEY"
private const val CODE_LENGTH_DEFAULT_VALUE = 4

@AndroidEntryPoint
class SmsCodeModalFragment : SimpleModalFragment<FragmentSmsCodeBinding>() {

    override val params = Params(
        viewBindingProvider = FragmentSmsCodeBinding::inflate,
    )

    private val viewModel by viewModels<SmsCodeViewModel>({ requireParentFragment() })

    companion object {
        fun newInstance(codeLength: Int) = SmsCodeModalFragment().apply {
            arguments = bundleOf(CODE_LENGTH_KEY to codeLength)
        }
    }

    override fun processArguments(bundle: Bundle?) {
        val maxCodeLength = arguments?.getInt(CODE_LENGTH_KEY) ?: CODE_LENGTH_DEFAULT_VALUE
        binding.smsCodeInput.filters = arrayOf(InputFilter.LengthFilter(maxCodeLength))
        viewModel.setupInitState(maxCodeLength)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        binding.smsCodeNumPadView.apply {
            isFingerprintEnabled = false
            onBackspaceClickedAction = { viewModel.onDigitRemoved() }
            onNumberClickedAction = { viewModel.onDigitAdded(it) }
        }
        binding.smsCodeCloseButton.doOnClick { dismiss() }
        binding.smsCodeResendButton.doOnClick { viewModel.onResendClicked() }
    }

    override fun subscribe() {
        viewModel.isLoadingState.observe(viewLifecycleOwner) { binding.loaderView.root.isVisible = it }
        viewModel.inputtedCode.observe(viewLifecycleOwner) {
            binding.smsCodeInput.setText(it)
            binding.smsCodeNumPadView.setBackspaceButtonsVisibility(it.isNotEmpty())
        }
        viewModel.resendButtonText.observe(viewLifecycleOwner) { binding.smsCodeResendButton.text = it }
        viewModel.isResendButtonEnabled.observe(viewLifecycleOwner) { binding.smsCodeResendButton.setEnabledTextButton(it) }
        viewModel.showMessage.observe(viewLifecycleOwner) { binding.smsCodeMessageView.text = it }
        viewModel.closeScreen.observe(viewLifecycleOwner) { dismiss() }
    }

    private fun TextView.setEnabledTextButton(isEnabled: Boolean) {
        this.isEnabled = isEnabled
        setTextColor(when (isEnabled) {
            true -> R.color.colorPrimary
            false -> R.color.disabled
        })
    }
}