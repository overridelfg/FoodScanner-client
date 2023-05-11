package kirillrychkov.foodscanner_client.app.domain.usecase.profile

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import kirillrychkov.foodscanner_client.app.domain.repository.ProfileRepository
import javax.inject.Inject

class GetUserAllergensUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): OperationResult<List<Allergen>, String?> {
        return repository.getUserAllergens()
    }
}