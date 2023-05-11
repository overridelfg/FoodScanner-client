package kirillrychkov.foodscanner_client.app.data.network.models

import kirillrychkov.foodscanner_client.app.domain.entity.Product

data class ProductDTO(
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
    val isValid: Boolean,
    val isFavorite: Boolean,
)

fun ProductDTO.toProduct(): Product {
    return Product(
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
        isValid,
        isFavorite
    )
}