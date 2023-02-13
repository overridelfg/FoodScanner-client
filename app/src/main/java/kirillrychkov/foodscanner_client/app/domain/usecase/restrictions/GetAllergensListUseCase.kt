package kirillrychkov.foodscanner_client.app.domain.usecase.restrictions

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import javax.inject.Inject

class GetAllergensListUseCase @Inject constructor(
    private val repository: ChooseRestrictionsRepository
) {
    operator fun invoke(): OperationResult<List<Allergen>, String?> {
        return repository.getAllergens()
    }
}