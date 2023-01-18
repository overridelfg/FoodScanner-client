package kirillrychkov.foodscanner_client.presentation.domain.entity

data class Diet(
    override val id: Int,
    override val title: String
) : Restriction(){
}