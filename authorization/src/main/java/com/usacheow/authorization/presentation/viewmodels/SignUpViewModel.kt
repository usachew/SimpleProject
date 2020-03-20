package com.usacheow.authorization.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.usacheow.authorization.domain.commands.RegisterByLoginAndPasswordCommand
import com.usacheow.coredata.error.ErrorProcessorImpl
import com.usacheow.coredata.setRequestThreads
import com.usacheow.coreuikit.viewmodels.SimpleRxViewModel
import com.usacheow.coreuikit.viewmodels.livedata.ActionLiveData
import com.usacheow.coreuikit.viewmodels.livedata.SimpleAction
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class SignUpViewModel
@Inject constructor(
    errorProcessor: ErrorProcessorImpl,
    private val registerCommand: RegisterByLoginAndPasswordCommand
) : SimpleRxViewModel(errorProcessor) {

    val isLoadingState: LiveData<Boolean> get() = _isLoadingStateLiveData
    private val _isLoadingStateLiveData by lazy { MutableLiveData<Boolean>() }

    val submitButtonEnabled: LiveData<Boolean> get() = _submitButtonEnabledLiveData
    private val _submitButtonEnabledLiveData by lazy { MutableLiveData<Boolean>() }

    val openMainScreen: LiveData<SimpleAction> get() = _openMainScreenLiveData
    private val _openMainScreenLiveData by lazy { ActionLiveData<SimpleAction>() }

    init {
        _isLoadingStateLiveData.value = false
        _submitButtonEnabledLiveData.value = false
    }

    fun onDataChanged(login: String, password: String) {
        _submitButtonEnabledLiveData.value = isLoginValid(login) && isPasswordValid(password)
    }

    private fun isLoginValid(login: String) = login.length >= 6

    private fun isPasswordValid(password: String) = password.length >= 6

    fun onSignInClicked(login: String, password: String) {
        if (!isLoginValid(login) || !isPasswordValid(password)) return

        disposables.clear()
        disposables += registerCommand.execute(login, password)
            .doOnSubscribe { _isLoadingStateLiveData.postValue(true) }
            .setRequestThreads()
            .defaultSubscribe {
                _isLoadingStateLiveData.value = false
                _openMainScreenLiveData.value = SimpleAction()
            }
    }
}