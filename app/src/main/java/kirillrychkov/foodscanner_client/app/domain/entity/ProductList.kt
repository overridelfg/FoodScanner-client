package kirillrychkov.foodscanner_client.app.domain.entity

data class ProductList(
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
){
}