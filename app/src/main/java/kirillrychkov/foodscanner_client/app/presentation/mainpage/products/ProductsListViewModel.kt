package kirillrychkov.foodscanner_client.app.presentation.mainpage.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.entity.ProductRestriction
import kirillrychkov.foodscanner_client.app.domain.entity.SuccessResponse
import kirillrychkov.foodscanner_client.app.domain.usecase.products.*
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductsListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    val getProductsBySearchUseCase: GetProductsBySearchUseCase,
    val getProductRestrictionsDetailsUseCase: GetProductRestrictionsDetailsUseCase,
    val addToFavoriteUseCase: AddToFavoriteUseCase,
    val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {

    private val _productsList = MutableLiveData<ViewState<List<Product>, String?>>()
    val productsList : LiveData<ViewState<List<Product>, String?>>
        get() = _productsList

    private val _favoriteList = MutableLiveData<ViewState<List<Product>, String?>>()
    val favoriteList : LiveData<ViewState<List<Product>, String?>>
        get() = _favoriteList

    private val _productDetails = MutableLiveData<ViewState<ProductRestriction, String?>>()
    val productDetails : LiveData<ViewState<ProductRestriction, String?>>
        get() = _productDetails

    private val _productsSearchList = MutableLiveData<ViewState<List<Product>, String?>>()
    val productsSearchList : LiveData<ViewState<List<Product>, String?>>
        get() = _productsSearchList

    private val _addToFavoriteResult = MutableLiveData<ViewState<SuccessResponse, String?>>()
    val addToFavoriteResult : LiveData<ViewState<SuccessResponse, String?>>
        get() = _addToFavoriteResult

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

    fun getProductRestrictionsDetails(productId: Long){
        viewModelScope.launch {
            _productDetails.value = ViewState.loading()
            val result = getProductRestrictionsDetailsUseCase.invoke(productId)
            _productDetails.value = when (result) {
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

    fun addToFavorite(productId: Long){
        viewModelScope.launch {
            _addToFavoriteResult.value = ViewState.loading()
            val result = addToFavoriteUseCase.invoke(productId)
            _addToFavoriteResult.value = when (result) {
                is OperationResult.Error -> ViewState.error(result.data)
                is OperationResult.Success -> ViewState.success(result.data)
            }

        }
    }

    fun getFavorites(){
        viewModelScope.launch {
            _favoriteList.value = ViewState.loading()
            val result = getFavoritesUseCase.invoke()
            _favoriteList.value = when (result) {
                is OperationResult.Error -> ViewState.error(result.data)
                is OperationResult.Success -> ViewState.success(result.data)
            }
        }
    }
}