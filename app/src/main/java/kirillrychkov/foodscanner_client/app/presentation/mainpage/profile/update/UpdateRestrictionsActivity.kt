package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.update

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import java.util.ArrayList

class UpdateRestrictionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_restrictions)

        val selectedDiets : ArrayList<Diet> = intent.extras?.getParcelableArrayList("UPDATE_DIETS")
            ?: throw RuntimeException("Selected diets could not be a null")

        val selectedAllergens : ArrayList<Allergen> = intent.extras?.getParcelableArrayList("UPDATE_ALLERGENS")
            ?: throw RuntimeException("Selected allergens could not be a null")

        val updateDietsFragment = UpdateDietsFragment.newInstance(selectedDiets, selectedAllergens)

        supportFragmentManager.beginTransaction()
            .addToBackStack("update_restrictions")
            .add(R.id.update_restrictions_container, updateDietsFragment)
            .commit()
    }
}