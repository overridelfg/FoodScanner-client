package kirillrychkov.foodscanner_client.presentation.domain.auth

interface AuthRepository {
    fun login(email: String, password: String)

    fun registration(username: String, email: String, password: String)
}