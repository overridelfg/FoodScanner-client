package kirillrychkov.foodscanner_client.app.domain.usecase.restrictions

import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import javax.inject.Inject

class GetSelectedDietsUseCase @Inject constructor(
    val repository: ChooseRestrictionsRepository
) {
    operator fun invoke() : MutableList<Diet>?{
        return repository.getSelectedDiets()
    }
}