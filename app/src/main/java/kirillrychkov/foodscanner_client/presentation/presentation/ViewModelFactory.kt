package kirillrychkov.foodscanner_client.presentation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kirillrychkov.foodscanner_client.presentation.presentation.auth.AuthViewModel
import kirillrychkov.foodscanner_client.presentation.presentation.restrictions.ChooseRestrictionsViewModel

class ViewModelFactory(): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel() as T
            modelClass.isAssignableFrom(ChooseRestrictionsViewModel::class.java) -> ChooseRestrictionsViewModel() as T
            else -> throw IllegalArgumentException("ViewModelClass not found")
        }
    }
}