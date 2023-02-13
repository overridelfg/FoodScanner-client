package kirillrychkov.foodscanner_client.app.data.network.models

data class LoginResponseDTO(
    val user : UserDTO,
    val token: String
) {

}