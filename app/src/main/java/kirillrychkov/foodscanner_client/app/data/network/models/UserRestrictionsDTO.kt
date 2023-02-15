package kirillrychkov.foodscanner_client.app.data.network.models

data class UserRestrictionsDTO(
    val diets: List<String>,
    val allergies: List<String>,
) {
}