package kirillrychkov.foodscanner_client.app.data.network.models

data class UserRestrictionsDTO(
    val diets: List<DietDTO>,
    val allergies: List<AllergenDTO>,
) {
}