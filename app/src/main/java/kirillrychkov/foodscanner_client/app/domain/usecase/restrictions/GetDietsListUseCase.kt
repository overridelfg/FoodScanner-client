package kirillrychkov.foodscanner_client.app.domain.usecase.restrictions

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import javax.inject.Inject

class GetDietsListUseCase @Inject constructor(
    private val repository: ChooseRestrictionsRepository
) {
    suspend operator fun invoke(): OperationResult<List<Diet>, String?> {
        return repository.getDiets()
    }
}