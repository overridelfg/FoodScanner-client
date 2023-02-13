package kirillrychkov.foodscanner_client.app.data.repository

import android.util.Log
import kirillrychkov.foodscanner_client.app.data.PrefsStorage
import kirillrychkov.foodscanner_client.app.data.network.ApiProvider
import kirillrychkov.foodscanner_client.app.data.network.models.toDiet
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.entity.Ingredient
import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChooseRestrictionsRepositoryImpl @Inject constructor(
    private val prefsStorage: PrefsStorage
): ChooseRestrictionsRepository {

    private val apiService =  ApiProvider().apiService

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

    override suspend fun getDiets(): OperationResult<List<Diet>, String?> {
        return withContext(Dispatchers.IO){
            try{
                val result = apiService.getDiets().map {
                    it.toDiet()
                }

                Log.d("ADA", result.toString())
                return@withContext OperationResult.Success(result)
            }catch (e: Exception){
                Log.d("ADA", e.message.toString())
                return@withContext OperationResult.Error(e.message)
            }
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

    override fun postSelectedDiets(listOfDiets : List<Diet>) {
        prefsStorage.saveListOfDiets(listOfDiets)
    }

    override fun postSelectedAllergens(listOfAllergens : List<Allergen>) {
        prefsStorage.saveListOfAllergens(listOfAllergens)
    }

    override fun getSelectedDiets(): MutableList<Diet>? {
        if (prefsStorage.getListOfDiets() == null){
            return mutableListOf()
        } else{
            return prefsStorage.getListOfDiets()
        }
    }

    override fun getSelectedAllergens(): MutableList<Allergen>? {
        if (prefsStorage.getListOfAllergens() == null){
            return mutableListOf()
        } else{
            return prefsStorage.getListOfAllergens()
        }
    }

    override fun removeSelectedRestrictions() {
        prefsStorage.saveListOfDiets(null)
        prefsStorage.saveListOfAllergens(null)
    }
}