package kirillrychkov.foodscanner_client.app.presentation.restrictions

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.entity.Ingredient
import kirillrychkov.foodscanner_client.app.domain.usecase.profile.UpdateRestrictionsUseCase
import kirillrychkov.foodscanner_client.app.domain.usecase.restrictions.*
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChooseRestrictionsViewModel @Inject constructor(
    private val getDietsListUseCase: GetDietsListUseCase,
    private val getAllergensListUseCase: GetAllergensListUseCase,
    private val getIngredientsListUseCase: GetIngredientsListUseCase,
    private val postSelectedDietsUseCase: PostSelectedDietsUseCase,
    private val postSelectedAllergensUseCase: PostSelectedAllergensUseCase,
    private val getSelectedDietsUseCase: GetSelectedDietsUseCase,
    private val getSelectedAllergensUseCase: GetSelectedAllergensUseCase,
    private val removeSelectedRestrictionsUseCase: RemoveSelectedRestrictionsUseCase,
    val updateRestrictionsUseCase: UpdateRestrictionsUseCase
) : ViewModel() {

    private val _dietsList = MutableLiveData<ViewState<List<Diet>, String?>>()
    val dietsList : LiveData<ViewState<List<Diet>, String?>>
        get() = _dietsList

    private val _allergensList = MutableLiveData<ViewState<List<Allergen>, String?>>()
    val allergensList : LiveData<ViewState<List<Allergen>, String?>>
        get() = _allergensList

    private val _ingredientsList = MutableLiveData<ViewState<List<Ingredient>, String?>>()
    val ingredientsList : LiveData<ViewState<List<Ingredient>, String?>>
        get() = _ingredientsList

    private val _selectedDietsList = MutableLiveData<MutableList<Diet>>()
    val selectedDietsList : LiveData<MutableList<Diet>>
        get() = _selectedDietsList

    private val _selectedAllergensList = MutableLiveData<MutableList<Allergen>>()
    val selectedAllergensList : LiveData<MutableList<Allergen>>
        get() = _selectedAllergensList

    private val _updateRestrictionsResult = MutableLiveData<ViewState<String, String?>>()
    val updateRestrictionsResult : LiveData<ViewState<String, String?>>
        get() = _updateRestrictionsResult

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

    fun getIngredientsList(){
        viewModelScope.launch {
            _ingredientsList.value = ViewState.loading()
            val result = getIngredientsListUseCase.invoke()
            _ingredientsList.value = when (result) {
                is OperationResult.Error -> ViewState.error(result.data)
                is OperationResult.Success -> ViewState.success(result.data)
            }
        }
    }

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

    fun getSelectedDiets(){
        val selectedDiets = getSelectedDietsUseCase.invoke()
        _selectedDietsList.value = selectedDiets!!
    }

    fun getSelectedAllergens(){
        val selectedAllergens = getSelectedAllergensUseCase.invoke()
        _selectedAllergensList.value = selectedAllergens!!
    }

    fun postSelectedDiets(listOfDiets: List<Diet>){
        postSelectedDietsUseCase.invoke(listOfDiets)
    }

    fun postSelectedAllergens(listOfAllergens: List<Allergen>){
        postSelectedAllergensUseCase.invoke(listOfAllergens)
    }

    fun removeSelectedRestrictions() = removeSelectedRestrictionsUseCase.invoke()
}