package kirillrychkov.foodscanner_client.presentation.domain.usecase.auth

import kirillrychkov.foodscanner_client.presentation.domain.OperationResult
import kirillrychkov.foodscanner_client.presentation.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase (
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String, username: String) : OperationResult<Unit, String?> {
        return authRepository.register(email, password, username)
    }
}