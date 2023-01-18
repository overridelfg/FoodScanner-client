package kirillrychkov.foodscanner_client.presentation.domain.entity

data class Allergen(
    override val title: String
) : Restriction(){
}