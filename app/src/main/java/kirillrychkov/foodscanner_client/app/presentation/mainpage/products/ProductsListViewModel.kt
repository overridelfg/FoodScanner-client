package kirillrychkov.foodscanner_client.app.presentation.mainpage.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.usecase.products.GetProductsUseCase
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductsListViewModel @Inject constructor(
    val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _productsList = MutableLiveData<ViewState<List<Product>, String?>>()
    val productsList : LiveData<ViewState<List<Product>, String?>>
        get() = _productsList

    fun getProducts(){
        viewModelScope.launch {
            _productsList.value = ViewState.loading()
            val result = getProductsUseCase.invoke()
            _productsList.value = when (result) {
                is OperationResult.Error -> ViewState.error(result.data)
                is OperationResult.Success -> ViewState.success(result.data)
            }
        }
    }
}