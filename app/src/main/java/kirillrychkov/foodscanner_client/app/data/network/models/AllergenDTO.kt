package kirillrychkov.foodscanner_client.app.data.network.models

import kirillrychkov.foodscanner_client.app.domain.entity.Allergen

data class AllergenDTO(
    val id: Int,
    val title: String,
    val description: String,
    val restricted_ingredients: List<String>
)
fun AllergenDTO.toAllergen() : Allergen{
    return Allergen(
        id,
        title,
        description,
        restricted_ingredients
    )
}