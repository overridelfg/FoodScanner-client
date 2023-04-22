package kirillrychkov.foodscanner_client.app.data.network.models

import kirillrychkov.foodscanner_client.app.domain.entity.ProductRestriction

data class ProductRestrictionResponseDTO(
    val answer: List<String>,
    val status: Boolean
)

fun ProductRestrictionResponseDTO.toProductRestriction() : ProductRestriction{
    return ProductRestriction(answer, status)
}