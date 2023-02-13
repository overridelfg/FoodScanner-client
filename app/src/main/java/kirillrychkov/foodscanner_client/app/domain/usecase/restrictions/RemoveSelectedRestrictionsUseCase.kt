package kirillrychkov.foodscanner_client.app.domain.usecase.restrictions

import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import javax.inject.Inject

class RemoveSelectedRestrictionsUseCase @Inject constructor(
    val repository: ChooseRestrictionsRepository
) {
    operator fun invoke(){
        repository.removeSelectedRestrictions()
    }
}