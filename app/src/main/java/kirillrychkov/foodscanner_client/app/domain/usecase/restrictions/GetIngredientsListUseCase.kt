package kirillrychkov.foodscanner_client.app.domain.usecase.restrictions

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Ingredient
import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import javax.inject.Inject

class GetIngredientsListUseCase  @Inject constructor(
    private val repository: ChooseRestrictionsRepository
){
    operator fun invoke(): OperationResult<List<Ingredient>, String?> {
        return repository.getIngredients()
    }
}