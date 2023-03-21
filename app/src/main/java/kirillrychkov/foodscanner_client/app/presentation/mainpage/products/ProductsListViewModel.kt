package kirillrychkov.foodscanner_client.app.presentation.mainpage.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.usecase.products.GetProductsBySearchUseCase
import kirillrychkov.foodscanner_client.app.domain.usecase.products.GetProductsUseCase
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductsListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    val getProductsBySearchUseCase: GetProductsBySearchUseCase
) : ViewModel() {

    private val _productsList = MutableLiveData<ViewState<List<Product>, String?>>()
    val productsList : LiveData<ViewState<List<Product>, String?>>
        get() = _productsList

    private val _productsSearchList = MutableLiveData<ViewState<List<Product>, String?>>()
    val productsSearchList : LiveData<ViewState<List<Product>, String?>>
        get() = _productsSearchList

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

    fun getProductsBySearch(name: String){
        viewModelScope.launch {
            _productsSearchList.value = ViewState.loading()
            val result = getProductsBySearchUseCase.invoke(name)
            _productsSearchList.value = when (result) {
                is OperationResult.Error -> ViewState.error(result.data)
                is OperationResult.Success -> ViewState.success(result.data)
            }
        }
    }
}