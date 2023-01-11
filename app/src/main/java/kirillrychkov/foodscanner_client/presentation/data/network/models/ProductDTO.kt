package kirillrychkov.foodscanner_client.presentation.data.network.models

data class ProductDTO(
    val id: Long,
    val title: String,
    val ingredients: List<String>,
    val image: String,
    val barcode: String,
    val isProductAllowed: Boolean,
    val allergenStatements: List<String>
) {
}