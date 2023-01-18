package kirillrychkov.foodscanner_client.presentation.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kirillrychkov.foodscanner_client.presentation.domain.OperationResult
import kirillrychkov.foodscanner_client.presentation.domain.entity.Allergen
import kirillrychkov.foodscanner_client.presentation.domain.entity.Diet
import kirillrychkov.foodscanner_client.presentation.domain.entity.Ingredient
import kirillrychkov.foodscanner_client.presentation.domain.entity.UserRestrictions
import kirillrychkov.foodscanner_client.presentation.domain.repository.ChooseRestrictionsRepository

class ChooseRestrictionsRepositoryImpl: ChooseRestrictionsRepository {

    private val dietsList = mutableListOf<Diet>()

    private val allergensList = mutableListOf<Allergen>()

    private val ingredientsList = mutableListOf<Ingredient>()

    init {
        for (i in 0 until 99){
            dietsList.add(Diet(i, "Диета + $i"))
        }

        for (i in 0 until 99){
            allergensList.add(Allergen(i, "Аллерген + $i"))
        }

        for (i in 0 until 99){
            ingredientsList.add(Ingredient(i, "Ингредиент + $i"))
        }
    }

    override fun getDiets(): OperationResult<List<Diet>, String?> {
        try{
            return OperationResult.Success(dietsList)
        }catch (e: Exception){
            return OperationResult.Error(e.message)
        }
    }

    override fun getAllergens(): OperationResult<List<Allergen>, String?> {
        try{
            return OperationResult.Success(allergensList)
        }catch (e: Exception){
            return OperationResult.Error(e.message)
        }
    }

    override fun getIngredients(): OperationResult<List<Ingredient>, String?> {
        try{
            return OperationResult.Success(ingredientsList)
        }catch (e: Exception){
            return OperationResult.Error(e.message)
        }
    }
}