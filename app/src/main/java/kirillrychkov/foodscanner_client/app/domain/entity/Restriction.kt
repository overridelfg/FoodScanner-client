package kirillrychkov.foodscanner_client.app.domain.entity

abstract class Restriction {
    abstract val id: Int
    abstract val title: String
    abstract val restrictedIngredients: List<String>
}