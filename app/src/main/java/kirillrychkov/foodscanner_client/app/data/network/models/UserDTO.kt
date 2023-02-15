package kirillrychkov.foodscanner_client.app.data.network.models

data class UserDTO(
    val id: String,
    val email: String,
    val name: String,
    val diets: List<DietDTO>,
    val allergens: List<AllergenDTO>
) {
}