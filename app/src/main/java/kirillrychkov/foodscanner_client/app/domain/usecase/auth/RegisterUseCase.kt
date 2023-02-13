package kirillrychkov.foodscanner_client.app.domain.usecase.auth

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor (
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String,
                        password: String,
                        username: String,
                        diets: List<Diet>,
                        allergens: List<Allergen>) : OperationResult<Unit, String?> {
        return authRepository.register(email, password, username, diets, allergens)
    }
}