package kirillrychkov.foodscanner_client.presentation.domain.entity

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val password: String,
    val token: String,
    val diets: List<Diet>,
    val allergens: List<Allergen>
) {
}