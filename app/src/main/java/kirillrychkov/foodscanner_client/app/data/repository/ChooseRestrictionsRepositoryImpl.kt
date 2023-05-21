package kirillrychkov.foodscanner_client.app.data.repository

import android.util.Log
import kirillrychkov.foodscanner_client.app.data.PrefsStorage
import kirillrychkov.foodscanner_client.app.data.network.ServerAPI
import kirillrychkov.foodscanner_client.app.data.network.models.toAllergen
import kirillrychkov.foodscanner_client.app.data.network.models.toDiet
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.entity.Ingredient
import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

class ChooseRestrictionsRepositoryImpl @Inject constructor(
    private val prefsStorage: PrefsStorage,
    private val apiService: ServerAPI
): ChooseRestrictionsRepository {


    override suspend fun getDiets(): OperationResult<List<Diet>, String?> {
        return withContext(Dispatchers.IO){
            try{
                val result = apiService.getDiets().map {
                    it.toDiet()
                }
                return@withContext OperationResult.Success(result)
            }
            catch (e: IOException){
                return@withContext OperationResult.Error(e.cause?.message.toString())
            }
            catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    override suspend fun getAllergens(): OperationResult<List<Allergen>, String?> {
        return withContext(Dispatchers.IO){
            try{
                val result = apiService.getAllergens().map {
                    it.toAllergen()
                }
                return@withContext OperationResult.Success(result)
            }
            catch (e: IOException){
                return@withContext OperationResult.Error(e.cause?.message.toString())
            }
            catch (e: Exception){
                Log.d("AA", e.message.toString())
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    override fun getIngredients(): OperationResult<List<Ingredient>, String?> {
        TODO("Not yet implemented")
    }

//    override fun getIngredients(): OperationResult<List<Ingredient>, String?> {
//        try{
//            return OperationResult.Success(ingredientsList)
//        }catch (e: Exception){
//            return OperationResult.Error(e.message)
//        }
//    }

}