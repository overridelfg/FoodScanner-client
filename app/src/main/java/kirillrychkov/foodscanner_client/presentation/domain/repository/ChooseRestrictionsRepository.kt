package kirillrychkov.foodscanner_client.presentation.domain.repository

import androidx.lifecycle.LiveData
import kirillrychkov.foodscanner_client.presentation.domain.entity.Allergen
import kirillrychkov.foodscanner_client.presentation.domain.entity.Diet
import kirillrychkov.foodscanner_client.presentation.domain.entity.Ingredient

interface ChooseRestrictionsRepository {
    fun getDiets(): LiveData<List<Diet>>

    fun getAllergens(): LiveData<List<Allergen>>

    fun getIngredients(): LiveData<List<Ingredient>>
}