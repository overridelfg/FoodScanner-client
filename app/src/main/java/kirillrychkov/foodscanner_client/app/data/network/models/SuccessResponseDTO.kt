package kirillrychkov.foodscanner_client.app.data.network.models

import kirillrychkov.foodscanner_client.app.domain.entity.SuccessResponse

data class SuccessResponseDTO(
    val message: String
)

fun SuccessResponseDTO.toSuccessResponse() : SuccessResponse{
    return SuccessResponse(message)
}