package kirillrychkov.foodscanner_client.app.data.network.models

import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.entity.User

data class LoginResponseDTO(
    val user : UserDTO,
    val token : String
)
fun LoginResponseDTO.toUser() : User{
    return User(
        id = user.id,
        email = user.email,
        name = user.name,
        token = token,
        diets = user.diets.map {
            it.toDiet()
        },
        allergens = user.allergens.map {
            it.toAllergen()
        }
    )
}