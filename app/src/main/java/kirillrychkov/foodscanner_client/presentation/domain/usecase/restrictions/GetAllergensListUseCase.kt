package kirillrychkov.foodscanner_client.presentation.domain.usecase.restrictions

import androidx.lifecycle.LiveData
import kirillrychkov.foodscanner_client.presentation.domain.OperationResult
import kirillrychkov.foodscanner_client.presentation.domain.entity.Allergen
import kirillrychkov.foodscanner_client.presentation.domain.repository.ChooseRestrictionsRepository

class GetAllergensListUseCase(
    private val repository: ChooseRestrictionsRepository
) {
    operator fun invoke(): OperationResult<List<Allergen>, String?> {
        return repository.getAllergens()
    }
}