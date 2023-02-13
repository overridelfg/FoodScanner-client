package kirillrychkov.foodscanner_client.app.domain.usecase.restrictions

import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import javax.inject.Inject

class PostSelectedDietsUseCase @Inject constructor(
    val repository: ChooseRestrictionsRepository
) {
    operator fun invoke(listOfDiet : List<Diet>){
        repository.postSelectedDiets(listOfDiet)
    }
}