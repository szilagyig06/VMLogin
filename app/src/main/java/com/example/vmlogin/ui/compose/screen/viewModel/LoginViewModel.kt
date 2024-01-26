package com.example.vmlogin.ui.compose.screen.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vmlogin.Constant
import com.example.vmlogin.auth.store.token.TokenManager
import com.example.vmlogin.auth.store.user.UserManager
import com.example.vmlogin.model.UserModel
import com.example.vmlogin.retrofit.request.LoginRequest
import com.example.vmlogin.retrofit.request.RefreshRequest
import com.example.vmlogin.retrofit.service.login.LoginService
import com.example.vmlogin.retrofit.service.refresh.RefreshService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val loginService: LoginService,
    private val refreshService: RefreshService,
    private val tokenManager: TokenManager,
    private val userManager: UserManager
) : ViewModel() {
    private val _userName = MutableLiveData("")
    val userName: LiveData<String> get() = _userName

    fun setUserName(newUserName: String) {
        _userName.value = newUserName
    }

    private val _password = MutableLiveData("")
    val password: LiveData<String> get() = _password

    fun setPassword(newPassword: String) {
        _password.value = newPassword
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    init {
        refreshToken()
    }

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object UnexpectedError : LoginState()
        data class Error(val errorMessage: String) : LoginState()
        data class Success(val accessToken: String) : LoginState()
    }

    fun login() {
        viewModelScope.launch {
            try {
                loadingState()

                    val response = loginService.login(
                        LoginRequest(
                            username = _userName.value ?: "",
                            password = _password.value ?: "",
                            grantType = Constant.App.GRANT_TYPE_PW,
                            clientId = Constant.App.CLIENT_ID
                        )
                    )
                    if (response.isSuccessful) {
                        userManager.saveUser(UserModel(_userName.value ?: "", "User Role"))

                        response.body()?.accessToken?.run { successState(this) }
                            ?: run { unexpectedState() }
                    }

            } catch (e: Throwable) {
                errorState(e.message ?: "")
            }
        }
    }

    private fun refreshToken() {
        viewModelScope.launch {
            val expired = tokenManager.hasTokenExpired()
            val refreshToken = tokenManager.getRefreshToken()

            if (expired && refreshToken?.isNotBlank() == true) {
                viewModelScope.launch {
                    try {
                        loadingState()
                        val response = refreshService.refreshToken(
                            RefreshRequest(
                                refreshToken = refreshToken,
                                grantType = Constant.App.GRANT_TYPE_PW,
                                clientId = Constant.App.CLIENT_ID
                            )
                        )

                        if (response.isSuccessful) {
                            response.body()?.accessToken?.run { successState(this) }
                                ?: run { unexpectedState() }
                        }
                    } catch (e: Throwable) {
                        errorState(e.message ?: "")
                    }
                }
            }
        }
    }

    fun resetState() {
        setState(LoginState.Idle)
    }

    private fun loadingState() {
        setState(LoginState.Loading)
    }

    private fun successState(token: String) {
        setState(LoginState.Success(token))
    }

    private fun errorState(message: String) {
        setState(LoginState.Error(message))
    }

    private fun unexpectedState() {
        setState(LoginState.UnexpectedError)
    }

    private fun setState(state: LoginState) {
        _loginState.value = state
    }
}