package kirillrychkov.foodscanner_client.app.data.repository

import android.util.Log
import kirillrychkov.foodscanner_client.app.data.PrefsStorage
import kirillrychkov.foodscanner_client.app.data.network.ApiProvider
import kirillrychkov.foodscanner_client.app.data.network.models.*
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.entity.User
import kirillrychkov.foodscanner_client.app.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val prefsStorage: PrefsStorage
) : AuthRepository {

    private val apiService =  ApiProvider().apiService

    override suspend fun login(email: String, password: String): OperationResult<User, String?> {
        return withContext(Dispatchers.IO) {
            try{
                val result = apiService.login(LoginRequestDTO(email, password)).toUser()
                prefsStorage.saveToSharedPreferences(
                    User(
                        id = result.id,
                        name = result.name,
                        email = result.email,
                        token = result.token,
                        diets = result.diets,
                        allergens = result.allergens
                    )
                )
                return@withContext OperationResult.Success(result)
            }catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String,
        diets: List<Diet>,
        allergens: List<Allergen>
    ): OperationResult<User, String?> {
        return withContext(Dispatchers.IO){
            try{
                val dietsList = diets.map {
                    DietDTO(it.id, it.title)
                }
                val allergensList = allergens.map {
                    AllergenDTO(it.id, it.title)
                }
                val result = apiService.register(
                    RegisterRequestDTO(
                        email = email,
                        password = password,
                        name = name,
                        diets = dietsList,
                        allergens = allergensList
                    )
                ).toUser()

                prefsStorage.saveToSharedPreferences(
                    User(
                        id = result.id,
                        name = result.name,
                        email = result.email,
                        token = result.token,
                        diets = result.diets,
                        allergens = result.allergens
                    )
                )

                return@withContext OperationResult.Success(result)
            }catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    override fun logout() {
        prefsStorage.saveToSharedPreferences(null)
    }

    override fun getUser(): User? {
        Log.d("AA", prefsStorage.getUser().toString())
        return null
    }
}