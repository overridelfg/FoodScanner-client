package kirillrychkov.foodscanner_client.presentation.data.auth

import kirillrychkov.foodscanner_client.presentation.domain.auth.AuthRepository

object AuthRepositoryImpl : AuthRepository{
    override fun login(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun registration(username: String, email: String, password: String) {
        TODO("Not yet implemented")
    }
}