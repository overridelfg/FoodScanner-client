package kirillrychkov.foodscanner_client.app.domain.usecase.profile

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateRestrictionsUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(diets: List<Diet>, allergens: List<Allergen>): OperationResult<String, String?> {
        return repository.updateRestrictions(diets, allergens)
    }
}