package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.entity.User
import kirillrychkov.foodscanner_client.app.domain.repository.AuthRepository
import kirillrychkov.foodscanner_client.app.domain.usecase.profile.GetUserAllergensUseCase
import kirillrychkov.foodscanner_client.app.domain.usecase.profile.GetUserDietsUseCase
import kirillrychkov.foodscanner_client.app.domain.usecase.profile.UpdateRestrictionsUseCase
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    authRepository: AuthRepository,
    val getUserDietsUseCase: GetUserDietsUseCase,
    val getUserAllergensUseCase: GetUserAllergensUseCase
) : ViewModel() {
    private val _getUser = MutableLiveData<User>(authRepository.getUser())
    val getUser : LiveData<User>
        get() = _getUser

    private val _userDiets = MutableLiveData<ViewState<List<Diet>, String?>>()
    val userDiets : LiveData<ViewState<List<Diet>, String?>>
        get() = _userDiets

    private val _userAllergens = MutableLiveData<ViewState<List<Allergen>, String?>>()
    val userAllergens : LiveData<ViewState<List<Allergen>, String?>>
        get() = _userAllergens



    fun getUserDiets(){
        viewModelScope.launch {
            _userDiets.value = ViewState.loading()
            val result = getUserDietsUseCase.invoke()
            _userDiets.value = when (result) {
                is OperationResult.Error -> ViewState.error(result.data)
                is OperationResult.Success -> ViewState.success(result.data)
            }
        }
    }

    fun getUserAllergens(){
        viewModelScope.launch {
            _userAllergens.value = ViewState.loading()
            val result = getUserAllergensUseCase.invoke()
            _userAllergens.value = when (result) {
                is OperationResult.Error -> ViewState.error(result.data)
                is OperationResult.Success -> ViewState.success(result.data)
            }
        }
    }



}