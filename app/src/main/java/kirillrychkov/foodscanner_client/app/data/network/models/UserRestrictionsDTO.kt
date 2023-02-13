package kirillrychkov.foodscanner_client.app.data.network.models

data class UserRestrictionsDTO(
    val userId: Int,
    val diets: List<String>,
    val allergies: List<String>,
    val avoidance: List<String>,
    val ingredients: List<String>,
) {
}