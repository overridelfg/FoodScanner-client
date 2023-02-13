package kirillrychkov.foodscanner_client.app.domain.usecase.auth

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor (
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String) : OperationResult<Unit, String?>{
        return authRepository.login(email, password)
    }
}