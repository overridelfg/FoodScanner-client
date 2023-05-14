package kirillrychkov.foodscanner_client.app.domain.entity

data class User(
    val id: String,
    val email: String,
    val name: String,
    val accessToken: String,
    val refreshToken: String
) {
}