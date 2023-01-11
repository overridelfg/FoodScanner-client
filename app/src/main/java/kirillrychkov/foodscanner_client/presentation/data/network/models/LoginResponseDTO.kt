package kirillrychkov.foodscanner_client.presentation.data.network.models

data class LoginResponseDTO(
    val user : UserDTO,
    val token: String
) {

}