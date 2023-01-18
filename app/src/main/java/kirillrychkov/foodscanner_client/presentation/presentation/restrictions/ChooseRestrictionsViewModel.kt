package kirillrychkov.foodscanner_client.presentation.presentation.restrictions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import kirillrychkov.foodscanner_client.presentation.data.repository.ChooseRestrictionsRepositoryImpl
import kirillrychkov.foodscanner_client.presentation.domain.usecase.restrictions.GetAllergensListUseCase
import kirillrychkov.foodscanner_client.presentation.domain.usecase.restrictions.GetDietsListUseCase
import kirillrychkov.foodscanner_client.presentation.domain.usecase.restrictions.GetIngredientsListUseCase
import kirillrychkov.foodscanner_client.presentation.presentation.ViewState

class ChooseRestrictionsViewModel : ViewModel() {
    private val repository = ChooseRestrictionsRepositoryImpl()
    private val getDietsListUseCase: GetDietsListUseCase = GetDietsListUseCase(repository)
    private val getAllergensListUseCase: GetAllergensListUseCase =
        GetAllergensListUseCase(repository)
    private val getIngredientsListUseCase: GetIngredientsListUseCase =
        GetIngredientsListUseCase(repository)

    val dietsList = getDietsListUseCase.invoke()

    val allergensList = getAllergensListUseCase.invoke()

    val ingredientsListUseCase = getIngredientsListUseCase.invoke()
}