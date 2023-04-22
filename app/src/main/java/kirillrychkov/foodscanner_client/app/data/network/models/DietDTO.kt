package kirillrychkov.foodscanner_client.app.data.network.models

import kirillrychkov.foodscanner_client.app.domain.entity.Diet

data class DietDTO(
    val id: Int,
    val title: String,
    val restricted_ingredients: List<String>
)

fun DietDTO.toDiet(): Diet {
    return Diet(
        id,
        title,
        restricted_ingredients
    )
}