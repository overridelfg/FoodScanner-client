package kirillrychkov.foodscanner_client.app.domain.usecase.auth

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.User
import kirillrychkov.foodscanner_client.app.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor (
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) : OperationResult<User, String?>{
        return authRepository.login(email, password)
    }
}