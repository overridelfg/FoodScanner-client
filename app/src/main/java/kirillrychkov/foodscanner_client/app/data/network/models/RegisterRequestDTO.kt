package kirillrychkov.foodscanner_client.app.data.network.models

data class RegisterRequestDTO(
    val email: String,
    val password: String,
    val name: String,
    val diets: List<DietDTO>,
    val allergens: List<AllergenDTO>
) {
}