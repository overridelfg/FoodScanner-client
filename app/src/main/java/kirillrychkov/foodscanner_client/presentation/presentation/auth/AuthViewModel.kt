package kirillrychkov.foodscanner_client.presentation.presentation.auth

import android.app.Application
import androidx.lifecycle.*
import kirillrychkov.foodscanner_client.presentation.data.repository.AuthRepositoryImpl
import kirillrychkov.foodscanner_client.presentation.domain.OperationResult
import kirillrychkov.foodscanner_client.presentation.domain.usecase.auth.LoginUseCase
import kirillrychkov.foodscanner_client.presentation.domain.usecase.auth.RegisterUseCase
import kirillrychkov.foodscanner_client.presentation.presentation.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUserCase: RegisterUseCase
) : ViewModel() {

    private val _loginResult = MutableLiveData<ViewState<Unit, String?>>()
    val loginResult: LiveData<ViewState<Unit, String?>>
    get() = _loginResult

    private val _registerResult = MutableLiveData<ViewState<Unit, String?>>()
    val registerResult : LiveData<ViewState<Unit, String?>>
        get() = _registerResult

    private val _errorInputEmail = MutableLiveData<AuthFormErrorState>()
    val errorInputEmail: LiveData<AuthFormErrorState>
        get() = _errorInputEmail

    private val _errorInputPassword = MutableLiveData<AuthFormErrorState>()
    val errorInputPassword: LiveData<AuthFormErrorState>
        get() = _errorInputPassword

    private val _errorInputUsername = MutableLiveData<AuthFormErrorState>()
    val errorInputUsername: LiveData<AuthFormErrorState>
        get() = _errorInputUsername

    fun login(email: String, password: String) {
        val fieldValid = validateLoginInput(email, password)
        if (fieldValid) {
            viewModelScope.launch {
                _loginResult.value = ViewState.loading()
                val result = loginUseCase.invoke(email, password)
                _loginResult.value = when (result) {
                    is OperationResult.Error -> ViewState.error(result.data)
                    is OperationResult.Success -> ViewState.success(result.data)
                }
            }
        }
    }

    fun register(email: String, password: String, username: String) {
        val fieldValid = validateRegisterInput(email, password, username)
        if (fieldValid) {
            viewModelScope.launch {
                _loginResult.value = ViewState.loading()
                val result = registerUserCase.invoke(email, password, username)
                _loginResult.value = when (result) {
                    is OperationResult.Error -> ViewState.error(result.data)
                    is OperationResult.Success -> ViewState.success(result.data)
                }
            }
        }
    }

    private fun validateLoginInput(email: String, password: String): Boolean {
        val emailValid = validateEmailInput(email)
        val passwordValid = validatePasswordInput(password)
        return emailValid && passwordValid
    }

    private fun validateRegisterInput(email: String, password: String, username: String): Boolean {
        val loginValid = validateLoginInput(email, password)
        val usernameValid = validateUsernameInput(username)
        return loginValid && usernameValid
    }

    private fun validateEmailInput(email: String): Boolean{
        if (email.isBlank()) {
            _errorInputEmail.value = AuthFormErrorState.EMPTY_EMAIL
            return false
        }
        return true
    }

    private fun validatePasswordInput(password: String) : Boolean{
        if (password.length < 4 || password.length > 32) {
            if (password.isBlank()) {
                _errorInputPassword.value = AuthFormErrorState.EMPTY_PASSWORD
            } else {
                _errorInputPassword.value = AuthFormErrorState.INVALID_PASSWORD
            }
            return false
        }
        return true
    }

    private fun validateUsernameInput(username: String) : Boolean{
        if (username.length < 2 || username.length > 18) {
            if (username.isBlank()) {
                _errorInputUsername.value = AuthFormErrorState.EMPTY_USERNAME
            } else {
                _errorInputUsername.value = AuthFormErrorState.INVALID_USERNAME
            }
            return false
        }
        return true
    }

    fun resetErrorInputEmail() {
        _errorInputEmail.value = AuthFormErrorState.VALID_EMAIL
    }

    fun resetErrorInputPassword() {
        _errorInputPassword.value = AuthFormErrorState.VALID_PASSWORD
    }

    fun resetErrorInputUsername() {
        _errorInputUsername.value = AuthFormErrorState.VALID_USERNAME
    }
}