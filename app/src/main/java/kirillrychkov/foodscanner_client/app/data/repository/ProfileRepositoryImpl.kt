package kirillrychkov.foodscanner_client.app.data.repository

import kirillrychkov.foodscanner_client.app.data.PrefsStorage
import kirillrychkov.foodscanner_client.app.data.network.ServerAPI
import kirillrychkov.foodscanner_client.app.data.network.models.*
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import org.json.JSONObject
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    val apiService: ServerAPI,
    val prefsStorage: PrefsStorage
) : ProfileRepository {
    override suspend fun getUserDiets(): OperationResult<List<Diet>, String?> {
        return withContext(Dispatchers.IO){
            try{
                val token = "Bearer " + prefsStorage.getUser()!!.accessToken
                val response = apiService.getUserDiets(token)
                if (response.isSuccessful && response.body() != null){
                    val result = response.body()!!.map {
                        it.toDiet()
                    }
                    return@withContext OperationResult.Success(result)
                }else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    if(errorObj.getString("error") == "Token expired!"){
                        refreshToken()
                        getUserDiets()
                    }else{
                        return@withContext OperationResult.Error(errorObj.getString("error"))
                    }
                } else {
                    return@withContext OperationResult.Error("Что-то пошло не так!")
                }
            }
            catch (e: IOException){
                return@withContext OperationResult.Error(e.cause?.message.toString())
            }
            catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    override suspend fun getUserAllergens(): OperationResult<List<Allergen>, String?> {
        return withContext(Dispatchers.IO){
            try{
                val token = "Bearer " + prefsStorage.getUser()!!.accessToken
                val response = apiService.getUserAllergens(token)
                if (response.isSuccessful && response.body() != null){
                    val result = response.body()!!.map {
                        it.toAllergen()
                    }
                    return@withContext OperationResult.Success(result)
                }else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    if(errorObj.getString("error") == "Token expired!"){
                        refreshToken()
                        getUserAllergens()
                    }else{
                        return@withContext OperationResult.Error(errorObj.getString("error"))
                    }
                } else {
                    return@withContext OperationResult.Error("Что-то пошло не так!")
                }
            }
            catch (e: IOException){
                return@withContext OperationResult.Error(e.cause?.message.toString())
            }
            catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    override suspend fun updateRestrictions(
        diets: List<Diet>,
        allergens: List<Allergen>
    ): OperationResult<String, String?> {
        return withContext(Dispatchers.IO){
            try{
                val token = "Bearer " + prefsStorage.getUser()!!.accessToken
                val dietsList = diets.map {
                    DietDTO(it.id, it.title, it.description)
                }
                val allergensList = allergens.map {
                    AllergenDTO(it.id, it.title, it.description)
                }

                val response = apiService.updateRestrictions(token, UserRestrictionsDTO(dietsList, allergensList))
                if (response.isSuccessful && response.body() != null){
                    val result = response.body()!!.message
                    return@withContext OperationResult.Success(result)
                }else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    if(errorObj.getString("error") == "Token expired!"){
                        refreshToken()
                        updateRestrictions(diets, allergens)
                    }else{
                        return@withContext OperationResult.Error(errorObj.getString("error"))
                    }
                } else {
                    return@withContext OperationResult.Error("Что-то пошло не так!")
                }
            }
            catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    private suspend fun refreshToken(){
        withContext(Dispatchers.IO) {
            var refreshToken = ""
            synchronized(this){
                refreshToken = prefsStorage.getUser()!!.refreshToken
            }
            val result = apiService.refreshAccessToken(refreshToken).body()!!
            prefsStorage.refreshTokens(result)
        }
    }
}