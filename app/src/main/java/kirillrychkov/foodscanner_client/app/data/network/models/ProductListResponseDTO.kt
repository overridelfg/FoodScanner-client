package kirillrychkov.foodscanner_client.app.data.network.models

import kirillrychkov.foodscanner_client.app.domain.entity.ProductList

data class ProductListResponseDTO(
    val id: Long,
    val Name: String,
    val Barcode: Long,
    val Description: String,
    val Proteins: String,
    val Fats: String,
    val Carbohydrates: String,
    val Kcal: String,
    val Kj: Int,
    val Weight: String,
    val Jpg: String,
    val isValid: Boolean
)

fun ProductListResponseDTO.toProductList(): ProductList {
    return ProductList(
        id,
        Name,
        Barcode,
        Description,
        Proteins,
        Fats,
        Carbohydrates,
        Kcal,
        Kj,
        Weight,
        Jpg,
        isValid
    )
}