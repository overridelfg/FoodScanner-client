package kirillrychkov.foodscanner_client.app.data.network.models

data class TokensResponseDTO(
    val accessToken: String,
    val refreshToken: String
) {
}