package kirillrychkov.foodscanner_client.presentation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kirillrychkov.foodscanner_client.presentation.presentation.auth.AuthViewModel

class ViewModelFactory(): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel() as T
            else -> throw IllegalArgumentException("ViewModelClass not found")
        }
    }
}