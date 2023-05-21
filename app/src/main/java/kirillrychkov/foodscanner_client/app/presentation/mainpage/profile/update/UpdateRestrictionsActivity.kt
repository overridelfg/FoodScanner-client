package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.update

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import java.util.ArrayList

class UpdateRestrictionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_restrictions)

        findNavController(R.id.update_restrictions_container)
            .setGraph(R.navigation.update_restrictions_navigation, intent.extras)
    }
}