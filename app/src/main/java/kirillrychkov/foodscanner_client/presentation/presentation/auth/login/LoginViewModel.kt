package kirillrychkov.foodscanner_client.presentation.presentation.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _errorInputEmail = MutableLiveData<LoginFormErrors>()
    val errorInputEmail: LiveData<LoginFormErrors>
        get() = _errorInputEmail

    private val _errorInputPassword = MutableLiveData<LoginFormErrors>()
    val errorInputPassword: LiveData<LoginFormErrors>
        get() = _errorInputPassword

    private val _shouldFinishLogin = MutableLiveData<Boolean>()
    val shouldFinishLogin: LiveData<Boolean>
        get() = _shouldFinishLogin

    fun login(email: String, password: String) {
        val fieldValid = validateInput(email, password)
        if (fieldValid) {
            _shouldFinishLogin.value = true
        }else{
            _shouldFinishLogin.value = false
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        var result = true
        if (email.isBlank()) {
            _errorInputEmail.value = LoginFormErrors.EMPTY_EMAIL
            result = false
        }
        if (password.length < 4 || password.length > 32) {
            if (password.isBlank()) {
                _errorInputPassword.value = LoginFormErrors.EMPTY_PASSWORD
            } else {
                _errorInputPassword.value = LoginFormErrors.INVALID_PASSWORD
            }
            result = false
        }
        return result
    }

    fun resetErrorInputEmail() {
        _errorInputEmail.value = LoginFormErrors.VALID_EMAIL
    }

    fun resetErrorInputPassword() {
        _errorInputPassword.value = LoginFormErrors.VALID_PASSWORD
    }

    enum class LoginFormErrors {
        EMPTY_EMAIL,
        INVALID_EMAIL,
        EMPTY_PASSWORD,
        INVALID_PASSWORD,
        VALID_EMAIL,
        VALID_PASSWORD
    }

}