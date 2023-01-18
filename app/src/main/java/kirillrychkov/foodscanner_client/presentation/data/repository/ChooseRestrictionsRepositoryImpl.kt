package kirillrychkov.foodscanner_client.presentation.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kirillrychkov.foodscanner_client.presentation.domain.entity.Allergen
import kirillrychkov.foodscanner_client.presentation.domain.entity.Diet
import kirillrychkov.foodscanner_client.presentation.domain.entity.Ingredient
import kirillrychkov.foodscanner_client.presentation.domain.repository.ChooseRestrictionsRepository

class ChooseRestrictionsRepositoryImpl: ChooseRestrictionsRepository {

    private val dietsListLD = MutableLiveData<List<Diet>>()
    private val dietsList = mutableListOf<Diet>()

    private val allergensListLD = MutableLiveData<List<Allergen>>()
    private val allergensList = mutableListOf<Allergen>()

    private val ingredientsListLD = MutableLiveData<List<Ingredient>>()
    private val ingredientsList = mutableListOf<Ingredient>()

    init {
        for (i in 0 until 99){
            dietsList.add(Diet("Диета + $i"))
        }

        for (i in 0 until 99){
            allergensList.add(Allergen("Аллерген + $i"))
        }

        for (i in 0 until 99){
            ingredientsList.add(Ingredient("Ингредиент + $i"))
        }
    }

    override fun getDiets(): LiveData<List<Diet>> {
        updateDietsList()
        return dietsListLD
    }

    override fun getAllergens(): LiveData<List<Allergen>> {
        updateAllergensList()
        return allergensListLD
    }

    override fun getIngredients(): LiveData<List<Ingredient>> {
        updateIngredientsList()
        return ingredientsListLD
    }

    private fun updateDietsList(){
        dietsListLD.value = dietsList.toList()
    }

    private fun updateAllergensList(){
        allergensListLD.value = allergensList.toList()
    }

    private fun updateIngredientsList(){
        ingredientsListLD.value = ingredientsList.toList()
    }
}