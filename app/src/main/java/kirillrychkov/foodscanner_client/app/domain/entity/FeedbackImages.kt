package kirillrychkov.foodscanner_client.app.domain.entity

import android.graphics.Bitmap
import okhttp3.MultipartBody
import java.io.File

class FeedbackImages(
    val firstImage: MultipartBody.Part,
    val secondImage: MultipartBody.Part,
    val barcode: Long
) {
}