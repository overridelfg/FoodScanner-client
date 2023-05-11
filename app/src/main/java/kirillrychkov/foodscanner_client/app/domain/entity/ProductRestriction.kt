package kirillrychkov.foodscanner_client.app.domain.entity

data class ProductRestriction(
    val answerDiets: List<String>,
    val answerAllergens: List<String>,
    val status: Boolean
) {
}