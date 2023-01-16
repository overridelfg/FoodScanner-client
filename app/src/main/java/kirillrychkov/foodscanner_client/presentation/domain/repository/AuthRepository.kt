package kirillrychkov.foodscanner_client.presentation.domain.repository

import kirillrychkov.foodscanner_client.presentation.domain.OperationResult

interface AuthRepository {
    fun login(email: String, password: String) : OperationResult<Unit, String?>

    fun register(username: String, email: String, password: String) : OperationResult<Unit, String?>
}