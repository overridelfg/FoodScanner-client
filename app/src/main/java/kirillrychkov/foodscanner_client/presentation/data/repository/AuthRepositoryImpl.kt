package kirillrychkov.foodscanner_client.presentation.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import kirillrychkov.foodscanner_client.presentation.data.PrefsStorage
import kirillrychkov.foodscanner_client.presentation.data.network.ServerAPI
import kirillrychkov.foodscanner_client.presentation.domain.OperationResult
import kirillrychkov.foodscanner_client.presentation.domain.entity.Allergen
import kirillrychkov.foodscanner_client.presentation.domain.entity.Diet
import kirillrychkov.foodscanner_client.presentation.domain.entity.User
import kirillrychkov.foodscanner_client.presentation.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val prefsStorage: PrefsStorage
) : AuthRepository {

    override fun login(email: String, password: String): OperationResult<Unit, String?> {
        val diets = mutableListOf<Diet>()
        val allergens = mutableListOf<Allergen>()

        diets.add(Diet(2, "asdada"))

        allergens.add(Allergen(2, "asdada"))
        prefsStorage.saveToSharedPreferences(
            User(
                id = 1,
                username = "username",
                email = email,
                password = password,
                token = "11231",
                diets = diets,
                allergens = allergens
            )
        )
        return OperationResult.Success(Unit)
    }

    override fun register(
        username: String,
        email: String,
        password: String,
        diets: List<Diet>,
        allergens: List<Allergen>
    ): OperationResult<Unit, String?> {
        prefsStorage.saveToSharedPreferences(
            User(
                id = 1,
                username = "username",
                email = email,
                password = password,
                token = "11231",
                diets = diets,
                allergens = allergens
            )
        )
        return OperationResult.Success(Unit)
    }

    override fun logout() {
        prefsStorage.saveToSharedPreferences(null)
    }

    override fun getUser(): User? {
        Log.d("AA", prefsStorage.getUser().toString())
        return null
    }
}