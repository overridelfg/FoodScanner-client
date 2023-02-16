package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kirillrychkov.foodscanner_client.app.domain.entity.User
import kirillrychkov.foodscanner_client.app.domain.repository.AuthRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    authRepository: AuthRepository
) : ViewModel() {
    private val _getUser = MutableLiveData<User>(authRepository.getUser())
    val getUser : LiveData<User>
        get() = _getUser

}