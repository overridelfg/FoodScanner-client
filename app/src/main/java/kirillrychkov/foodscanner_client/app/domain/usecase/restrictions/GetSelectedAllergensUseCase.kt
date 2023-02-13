package kirillrychkov.foodscanner_client.app.domain.usecase.restrictions

import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import javax.inject.Inject

class GetSelectedAllergensUseCase @Inject constructor(
    val repository: ChooseRestrictionsRepository
) {
    operator fun invoke() : MutableList<Allergen>?{
        return repository.getSelectedAllergens()
    }
}