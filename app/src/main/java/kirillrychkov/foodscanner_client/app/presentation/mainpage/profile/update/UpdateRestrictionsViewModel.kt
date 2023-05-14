package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.usecase.profile.UpdateRestrictionsUseCase
import kirillrychkov.foodscanner_client.app.domain.usecase.restrictions.GetAllergensListUseCase
import kirillrychkov.foodscanner_client.app.domain.usecase.restrictions.GetDietsListUseCase
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdateRestrictionsViewModel @Inject constructor(
    val updateRestrictionsUseCase: UpdateRestrictionsUseCase,
    val getDietsListUseCase: GetDietsListUseCase,
    val getAllergensListUseCase: GetAllergensListUseCase,
): ViewModel() {

    private val _dietsList = MutableLiveData<ViewState<List<Diet>, String?>>()
    val dietsList : LiveData<ViewState<List<Diet>, String?>>
        get() = _dietsList

    private val _allergensList = MutableLiveData<ViewState<List<Allergen>, String?>>()
    val allergensList : LiveData<ViewState<List<Allergen>, String?>>
        get() = _allergensList

    private val _updateRestrictionsResult = MutableLiveData<ViewState<String, String?>>()
    val updateRestrictionsResult : LiveData<ViewState<String, String?>>
        get() = _updateRestrictionsResult

    private val _selectedDietsList = MutableLiveData<MutableList<Diet>>()
    val selectedDietsList : LiveData<MutableList<Diet>>
        get() = _selectedDietsList

    private val _selectedAllergensList = MutableLiveData<MutableList<Allergen>>()
    val selectedAllergensList : LiveData<MutableList<Allergen>>
        get() = _selectedAllergensList

    fun updateRestrictions(diets: List<Diet>, allergens : List<Allergen>){
        viewModelScope.launch {
            _updateRestrictionsResult.value = ViewState.loading()
            val result = updateRestrictionsUseCase.invoke(diets, allergens)
            _updateRestrictionsResult.value = when (result) {
                is OperationResult.Error -> ViewState.error(result.data)
                is OperationResult.Success -> ViewState.success(result.data)
            }
        }
    }

    fun getDietsList(){
        viewModelScope.launch {
            _dietsList.value = ViewState.loading()
            val result = getDietsListUseCase.invoke()
            _dietsList.value = when (result) {
                is OperationResult.Error -> ViewState.error(result.data)
                is OperationResult.Success -> ViewState.success(result.data)
            }
        }
    }

    fun getAllergensList(){
        viewModelScope.launch {
            _allergensList.value = ViewState.loading()
            val result = getAllergensListUseCase.invoke()
            _allergensList.value = when (result) {
                is OperationResult.Error -> ViewState.error(result.data)
                is OperationResult.Success -> ViewState.success(result.data)
            }
        }
    }

    fun setSelectedDiets(selectedDiets: MutableList<Diet>){
        _selectedDietsList.value = selectedDiets
    }

    fun getSelectedDiets() : MutableList<Diet>?{
        return selectedDietsList.value
    }

    fun getSelectedAllergens() : MutableList<Allergen>?{
        return selectedAllergensList.value
    }

    fun setSelectedAllergens(selectedAllergens: MutableList<Allergen>){
        _selectedAllergensList.value = selectedAllergens
    }
}