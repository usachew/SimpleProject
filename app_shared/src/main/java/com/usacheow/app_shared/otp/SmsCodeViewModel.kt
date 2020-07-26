package com.usacheow.app_shared.otp

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.usacheow.app_shared.R
import com.usacheow.coredata.network.setRequestThreads
import com.usacheow.coreui.livedata.ActionLiveData
import com.usacheow.coreui.livedata.SimpleAction
import com.usacheow.coreui.resources.ResourcesWrapper
import com.usacheow.coreui.viewmodels.SimpleViewModel
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import java.util.concurrent.TimeUnit

private const val EMPTY_MESSAGE = ""
private const val EMPTY_CODE = ""
private const val SMS_CODE_TIMEOUT_SECONDS = 60L

class SmsCodeViewModel
@ViewModelInject constructor(
    private val resources: ResourcesWrapper,
    @Assisted private val savedStateHandle: SavedStateHandle
) : SimpleViewModel() {

    private var currentCode = ""
    private var maxCodeLength = 0

    private val _inputtedCodeLiveData by lazy { MutableLiveData<String>() }
    val inputtedCode: LiveData<String> get() = _inputtedCodeLiveData

    private val _isLoadingStateLiveData by lazy { MutableLiveData<Boolean>() }
    val isLoadingState: LiveData<Boolean> get() = _isLoadingStateLiveData

    private val _closeScreenLiveData by lazy { ActionLiveData<SimpleAction>() }
    val closeScreen: LiveData<SimpleAction> get() = _closeScreenLiveData

    private val _showMessageLiveData by lazy { ActionLiveData<String>() }
    val showMessage: LiveData<String> get() = _showMessageLiveData

    private val _isResendButtonEnabledLiveData by lazy { ActionLiveData<Boolean>() }
    val isResendButtonEnabled: LiveData<Boolean> get() = _isResendButtonEnabledLiveData

    private val _resendButtonTextLiveData by lazy { ActionLiveData<String>() }
    val resendButtonText: LiveData<String> get() = _resendButtonTextLiveData

    private val _codeLiveData by lazy { ActionLiveData<String>() }
    val code: LiveData<String> get() = _codeLiveData

    private val _resendCodeLiveData by lazy { ActionLiveData<SimpleAction>() }
    val resendCode: LiveData<SimpleAction> get() = _resendCodeLiveData

    fun setupInitState(codeLength: Int) {
        currentCode = ""
        maxCodeLength = codeLength

        _inputtedCodeLiveData.value = EMPTY_CODE
        _isLoadingStateLiveData.value = false

        startTimer()
    }

    private fun startTimer() {
        _isResendButtonEnabledLiveData.value = false
        _resendButtonTextLiveData.value = resources.getString(R.string.sms_code_resend_timer, SMS_CODE_TIMEOUT_SECONDS)

        disposables += Observable.interval(1L, TimeUnit.SECONDS)
            .setRequestThreads()
            .take(SMS_CODE_TIMEOUT_SECONDS)
            .map { SMS_CODE_TIMEOUT_SECONDS - it }
            .subscribe(
                { _resendButtonTextLiveData.value = resources.getString(R.string.sms_code_resend_timer, it) },
                {},
                {
                    _resendButtonTextLiveData.value = resources.getString(R.string.sms_code_resend)
                    _isResendButtonEnabledLiveData.value = true
                }
            )
    }

    fun onResendClicked() {
        startTimer()
        _resendCodeLiveData.value = SimpleAction()
    }

    fun onDigitAdded(digit: String) {
        if (currentCode.length >= maxCodeLength) return

        currentCode += digit
        _inputtedCodeLiveData.value = currentCode
        if (currentCode.length == maxCodeLength) {
            onCodeInputted()
        }
    }

    fun onDigitRemoved() {
        if (currentCode.isEmpty()) return

        currentCode = currentCode.dropLast(1)
        _inputtedCodeLiveData.value = currentCode
    }

    private fun onCodeInputted() {
        _showMessageLiveData.value = EMPTY_MESSAGE
        _isLoadingStateLiveData.value = true
        _codeLiveData.value = currentCode
    }

    fun showMessage(message: String) {
        _isLoadingStateLiveData.value = false
        _showMessageLiveData.value = message
    }

    fun closeScreen() {
        _closeScreenLiveData.value = SimpleAction()
    }
}