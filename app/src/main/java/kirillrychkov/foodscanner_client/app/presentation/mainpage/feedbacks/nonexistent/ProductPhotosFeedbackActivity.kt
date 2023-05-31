package kirillrychkov.foodscanner_client.app.presentation.mainpage.feedbacks.nonexistent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import kirillrychkov.foodscanner_client.R

class ProductPhotosFeedbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_photos_feedback)

        findNavController(R.id.feedback_products_photos_container)
            .setGraph(R.navigation.feedback_products_photos_navigation)
    }
}