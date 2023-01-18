package kirillrychkov.foodscanner_client.presentation.domain.entity

data class UserRestrictions(
    val userId: Int,
    val diets: List<Diet>,
    val allergies: List<Diet>,
    val ingredients: List<Diet>,
) {
}