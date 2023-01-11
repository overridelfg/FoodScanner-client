package kirillrychkov.foodscanner_client.presentation.data.network.models

data class IngredientDTO(
    val id: Long,
    val title: String,
    val isAllowed: Boolean,
    val description: String
) {
}