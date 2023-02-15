package kirillrychkov.foodscanner_client.app.domain.entity

data class User(
    val id: String,
    val email: String,
    val name: String,
    val token: String,
    val diets: List<Diet>,
    val allergens: List<Allergen>
) {
}