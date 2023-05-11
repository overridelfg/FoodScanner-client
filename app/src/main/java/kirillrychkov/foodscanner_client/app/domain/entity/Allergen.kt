package kirillrychkov.foodscanner_client.app.domain.entity

data class Allergen(
    override val id: Int,
    override val title: String,
    override val description: String,
    override val restrictedIngredients: List<String>
) : Restriction(){
}