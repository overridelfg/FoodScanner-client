package kirillrychkov.foodscanner_client.app.domain.repository

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.entity.Ingredient

interface ChooseRestrictionsRepository {
    suspend fun getDiets(): OperationResult<List<Diet>, String?>

    suspend fun getAllergens(): OperationResult<List<Allergen>, String?>

    fun getIngredients(): OperationResult<List<Ingredient>, String?>
}