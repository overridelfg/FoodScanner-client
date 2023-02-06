package kirillrychkov.foodscanner_client.presentation.domain.repository

import kirillrychkov.foodscanner_client.presentation.domain.OperationResult
import kirillrychkov.foodscanner_client.presentation.domain.entity.Allergen
import kirillrychkov.foodscanner_client.presentation.domain.entity.Diet
import kirillrychkov.foodscanner_client.presentation.domain.entity.User

interface AuthRepository {
    fun login(email: String, password: String): OperationResult<Unit, String?>

    fun register(
        username: String,
        email: String,
        password: String,
        diets: List<Diet>,
        allergens: List<Allergen>
    ): OperationResult<Unit, String?>

    fun logout()

    fun getUser(): User?
}