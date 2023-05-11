package kirillrychkov.foodscanner_client.app.presentation.mainpage.barcodescan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.entity.ProductRestriction
import kirillrychkov.foodscanner_client.app.domain.entity.SuccessResponse
import kirillrychkov.foodscanner_client.app.domain.usecase.products.AddToFavoriteUseCase
import kirillrychkov.foodscanner_client.app.domain.usecase.products.GetProductDetailsUseCase
import kirillrychkov.foodscanner_client.app.domain.usecase.products.GetProductRestrictionsDetailsUseCase
import kirillrychkov.foodscanner_client.app.domain.usecase.scan.GetBarcodeScanHistoryUseCase
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

class BarcodeScannerViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    val getProductRestrictionsDetailsUseCase: GetProductRestrictionsDetailsUseCase,
    val getBarcodeScanHistoryUseCase: GetBarcodeScanHistoryUseCase,
    val addToFavoriteUseCase: AddToFavoriteUseCase,
): ViewModel() {
    private val _productDetails = MutableLiveData<ViewState<Product, String?>>()
    val productDetails : LiveData<ViewState<Product, String?>>
        get() = _productDetails

    private val _productRestrictionDetails = MutableLiveData<ViewState<ProductRestriction, String?>>()
    val productRestrictionDetails : LiveData<ViewState<ProductRestriction, String?>>
        get() = _productRestrictionDetails

    private val _barcodeScanHistoryResult = MutableLiveData<ViewState<List<Product>, String?>>()
    val barcodeScanHistoryResult : LiveData<ViewState<List<Product>, String?>>
        get() = _barcodeScanHistoryResult

    private val _addToFavoriteResult = MutableLiveData<ViewState<SuccessResponse, String?>>()
    val addToFavoriteResult : LiveData<ViewState<SuccessResponse, String?>>
        get() = _addToFavoriteResult


    fun getProductDetails(barcode: Long){
        viewModelScope.launch {
            _productDetails.value = ViewState.loading()
            val result = getProductDetailsUseCase.invoke(barcode)
            _productDetails.value = when (result) {
                is OperationResult.Error -> ViewState.error(result.data)
                is OperationResult.Success -> ViewState.success(result.data)
            }
        }
    }

    fun getBarcodeScanHistory(){
        viewModelScope.launch {
            _barcodeScanHistoryResult.value = ViewState.loading()
            val result = getBarcodeScanHistoryUseCase.invoke()
            _barcodeScanHistoryResult.value = when (result) {
                is OperationResult.Error -> ViewState.error(result.data)
                is OperationResult.Success -> ViewState.success(result.data)
            }
        }
    }

    fun getProductRestrictionsDetails(productId: Long){
        viewModelScope.launch {
            _productRestrictionDetails.value = ViewState.loading()
            val result = getProductRestrictionsDetailsUseCase.invoke(productId)
            _productRestrictionDetails.value = when (result) {
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
}