package kirillrychkov.foodscanner_client.app.domain.repository

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.entity.User

interface AuthRepository {
    suspend fun login(email: String, password: String): OperationResult<User, String?>

    suspend fun register(
        email: String,
        password: String,
        name: String,
        diets: List<Diet>,
        allergens: List<Allergen>
    ): OperationResult<User, String?>

    fun logout()

    fun getUser(): User?
}