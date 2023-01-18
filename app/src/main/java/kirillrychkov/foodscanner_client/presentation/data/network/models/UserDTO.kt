package kirillrychkov.foodscanner_client.presentation.data.network.models

data class UserDTO(
    val id: Long,
    val username: String,
    val email: String,
    val password: String,
) {
}