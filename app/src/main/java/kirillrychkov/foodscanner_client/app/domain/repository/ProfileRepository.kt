package kirillrychkov.foodscanner_client.app.domain.repository

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet

interface ProfileRepository {
    suspend fun getUserDiets() : OperationResult<List<Diet>, String?>

    suspend fun getUserAllergens() : OperationResult<List<Allergen>, String?>

    suspend fun updateRestrictions(diets: List<Diet>, allergens: List<Allergen>) : OperationResult<String, String?>
}