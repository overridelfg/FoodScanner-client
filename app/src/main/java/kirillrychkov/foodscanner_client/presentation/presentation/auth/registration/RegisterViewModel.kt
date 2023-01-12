package kirillrychkov.foodscanner_client.presentation.presentation.auth.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    private val _errorInputEmail = MutableLiveData<RegisterFormErrors>()
    val errorInputEmail: LiveData<RegisterFormErrors>
        get() = _errorInputEmail

    private val _errorInputPassword = MutableLiveData<RegisterFormErrors>()
    val errorInputPassword: LiveData<RegisterFormErrors>
        get() = _errorInputPassword

    private val _errorInputUsername = MutableLiveData<RegisterFormErrors>()
    val errorInputUsername: LiveData<RegisterFormErrors>
        get() = _errorInputUsername

    private val _shouldFinishRegister = MutableLiveData<Boolean>()
    val shouldFinishRegister: LiveData<Boolean>
        get() = _shouldFinishRegister

    fun register(email: String, password: String, username: String) {
        val fieldValid = validateInput(email, password, username)
        if (fieldValid) {
            _shouldFinishRegister.value = true
        }else{
            _shouldFinishRegister.value = false
        }
    }

    private fun validateInput(email: String, password: String, username: String): Boolean {
        var result = true
        if (email.isBlank()) {
            _errorInputEmail.value = RegisterFormErrors.EMPTY_EMAIL
            result = false
        }
        if (password.length < 4 || password.length > 32) {
            if (password.isBlank()) {
                _errorInputPassword.value = RegisterFormErrors.EMPTY_PASSWORD
            } else {
                _errorInputPassword.value = RegisterFormErrors.INVALID_PASSWORD
            }
            result = false
        }
        if (username.length < 2 || username.length > 18) {
            if (username.isBlank()) {
                _errorInputUsername.value = RegisterFormErrors.EMPTY_USERNAME
            } else {
                _errorInputUsername.value = RegisterFormErrors.INVALID_USERNAME
            }
            result = false
        }
        return result
    }

    fun resetErrorInputEmail() {
        _errorInputEmail.value = RegisterFormErrors.VALID_EMAIL
    }

    fun resetErrorInputPassword() {
        _errorInputPassword.value = RegisterFormErrors.VALID_PASSWORD
    }

    fun resetErrorInputUsername() {
        _errorInputUsername.value = RegisterFormErrors.VALID_USERNAME
    }

    enum class RegisterFormErrors {
        EMPTY_EMAIL,
        INVALID_EMAIL,
        EMPTY_PASSWORD,
        INVALID_PASSWORD,
        EMPTY_USERNAME,
        INVALID_USERNAME,
        VALID_EMAIL,
        VALID_PASSWORD,
        VALID_USERNAME
    }
}