package kirillrychkov.foodscanner_client.app.data.network.models

import kirillrychkov.foodscanner_client.app.domain.entity.ProductRestriction

data class ProductRestrictionResponseDTO(
    val answerDiets: List<String>,
    val answerAllergens: List<String>,
    val status: Boolean
)

fun ProductRestrictionResponseDTO.toProductRestriction() : ProductRestriction{
    return ProductRestriction(answerDiets, answerAllergens, status)
}