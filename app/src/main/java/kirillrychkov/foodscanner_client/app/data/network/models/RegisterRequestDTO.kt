package kirillrychkov.foodscanner_client.app.data.network.models

data class RegisterRequestDTO(
    val username: String,
    val email: String,
    val password: String,
    val diets: List<String>,
    val allergies: List<String>
) {
}