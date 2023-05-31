package kirillrychkov.foodscanner_client.app.presentation.mainpage.feedbacks.nonexistent

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.FeedbackImages
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.entity.SuccessResponse
import kirillrychkov.foodscanner_client.app.domain.usecase.products.PostNonexistentFeedbackUseCase
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class FeedbackViewModel @Inject constructor(
    val postNonexistentFeedbackUseCase: PostNonexistentFeedbackUseCase
) : ViewModel() {

    private val _getFirstImage = MutableLiveData<Bitmap>()
    val getFirstImage : LiveData<Bitmap>
        get() = _getFirstImage

    private val _getSecondImage = MutableLiveData<Bitmap>()
    val getSecondImage : LiveData<Bitmap>
        get() = _getSecondImage

    private val _feedbackNonexistent = MutableLiveData<kirillrychkov.foodscanner_client.app.presentation.ViewState<SuccessResponse, String?>>()
    val feedbackNonexistent : LiveData<kirillrychkov.foodscanner_client.app.presentation.ViewState<SuccessResponse, String?>>
        get() = _feedbackNonexistent


    fun setSecondImage(bitmap: Bitmap){
        _getSecondImage.postValue(bitmap)
    }

    fun setFirstImage(bitmap: Bitmap){
        _getFirstImage.postValue(bitmap)
    }

    private val _currentBarcode = MutableLiveData<Long>()
    val currentBarcode : LiveData<Long>
        get() = _currentBarcode

    fun setCurrentBarcode(barcode: Long){
        _currentBarcode.value = barcode
    }

    fun provideImagesFeedback(firstImage: Bitmap, secondImage: Bitmap, barcode: Long){

        val multipartBodyFirst = convertToMultiPart(firstImage)
        val multipartBodySecond = convertToMultiPart(secondImage)

        viewModelScope.launch {
            _feedbackNonexistent.value = kirillrychkov.foodscanner_client.app.presentation.ViewState.loading()
            val result = postNonexistentFeedbackUseCase.invoke(FeedbackImages(multipartBodyFirst, multipartBodySecond, barcode))
            _feedbackNonexistent.value = when (result) {
                is OperationResult.Error -> kirillrychkov.foodscanner_client.app.presentation.ViewState.error(result.data)
                is OperationResult.Success -> kirillrychkov.foodscanner_client.app.presentation.ViewState.success(result.data)
            }
        }
    }

    private fun convertToMultiPart(image: Bitmap): MultipartBody.Part{
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArray = baos.toByteArray()
        return MultipartBody.Part.createFormData(
            "photo[content]", "photo",
            byteArray.toRequestBody("image/*".toMediaTypeOrNull(), 0, byteArray.size)
        )
    }

}