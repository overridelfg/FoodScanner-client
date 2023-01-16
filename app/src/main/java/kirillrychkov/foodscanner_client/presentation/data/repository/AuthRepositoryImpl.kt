package kirillrychkov.foodscanner_client.presentation.data.repository

import android.app.Application
import android.util.Log
import kirillrychkov.foodscanner_client.presentation.domain.OperationResult
import kirillrychkov.foodscanner_client.presentation.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl(
) : AuthRepository {
    override fun login(email: String, password: String): OperationResult<Unit, String?> {
        Log.d("LOGIN", "Logined")
        return OperationResult.Success(Unit)
    }

    override fun register(
        username: String,
        email: String,
        password: String
    ): OperationResult<Unit, String?> {
        Log.d("LOGIN", "Logined")
        return OperationResult.Success(Unit)
    }

}