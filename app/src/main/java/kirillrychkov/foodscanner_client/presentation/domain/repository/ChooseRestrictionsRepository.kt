package kirillrychkov.foodscanner_client.presentation.domain.repository

import androidx.lifecycle.LiveData
import kirillrychkov.foodscanner_client.presentation.domain.OperationResult
import kirillrychkov.foodscanner_client.presentation.domain.entity.Allergen
import kirillrychkov.foodscanner_client.presentation.domain.entity.Diet
import kirillrychkov.foodscanner_client.presentation.domain.entity.Ingredient
import kirillrychkov.foodscanner_client.presentation.domain.entity.UserRestrictions

interface ChooseRestrictionsRepository {
    fun getDiets(): OperationResult<List<Diet>, String?>

    fun getAllergens(): OperationResult<List<Allergen>, String?>

    fun getIngredients(): OperationResult<List<Ingredient>, String?>
}