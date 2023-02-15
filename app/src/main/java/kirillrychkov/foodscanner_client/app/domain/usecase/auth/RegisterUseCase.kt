package kirillrychkov.foodscanner_client.app.domain.usecase.auth

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.entity.User
import kirillrychkov.foodscanner_client.app.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor (
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String,
                        password: String,
                        name: String,
                        diets: List<Diet>,
                        allergens: List<Allergen>) : OperationResult<User, String?> {
        return authRepository.register(email, password, name, diets, allergens)
    }
}