package kirillrychkov.foodscanner_client.app.data.repository

import kirillrychkov.foodscanner_client.app.data.PrefsStorage
import kirillrychkov.foodscanner_client.app.data.network.ServerAPI
import kirillrychkov.foodscanner_client.app.data.network.models.*
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.entity.User
import kirillrychkov.foodscanner_client.app.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val prefsStorage: PrefsStorage,
    private val apiService: ServerAPI
) : AuthRepository {

    override suspend fun login(email: String, password: String): OperationResult<User, String?> {
        return withContext(Dispatchers.IO) {
            try{
                val response = apiService.login(LoginRequestDTO(email, password))
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!.toUser()
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
                } else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    return@withContext OperationResult.Error(errorObj.getString("error"))
                } else {
                    return@withContext OperationResult.Error("Что-то пошло не так!")
                }
            }catch (e: HttpException){
                return@withContext OperationResult.Error(e.message)
            } catch (e : IOException){
                return@withContext OperationResult.Error(e.message)
            } catch (e: Exception){
                return@withContext OperationResult.Error("Неизвестная ошибка")
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
        return withContext(Dispatchers.IO) {
            try {
                val dietsList = diets.map {
                    DietDTO(it.id, it.title)
                }
                val allergensList = allergens.map {
                    AllergenDTO(it.id, it.title)
                }
                val response = apiService.register(
                    RegisterRequestDTO(
                        email = email,
                        password = password,
                        name = name,
                        diets = dietsList,
                        allergens = allergensList
                    )
                )
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!.toUser()
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
                } else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    return@withContext OperationResult.Error(errorObj.getString("error"))
                } else {
                    return@withContext OperationResult.Error("Что-то пошло не так!")
                }
            }catch (e: HttpException){
                return@withContext OperationResult.Error(e.message)
            } catch (e : IOException){
                return@withContext OperationResult.Error(e.message)
            } catch (e: Exception){
                return@withContext OperationResult.Error("Неизвестная ошибка")
            }
        }
    }

    override fun logout() {
        prefsStorage.saveToSharedPreferences(null)
    }

    override fun getUser(): User? {
        return prefsStorage.getUser()
    }
}