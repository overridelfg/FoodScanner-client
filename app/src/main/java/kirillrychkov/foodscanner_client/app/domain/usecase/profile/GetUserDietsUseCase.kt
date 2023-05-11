package kirillrychkov.foodscanner_client.app.domain.usecase.profile

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.repository.ProfileRepository
import javax.inject.Inject

class GetUserDietsUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): OperationResult<List<Diet>, String?> {
        return repository.getUserDiets()
    }
}