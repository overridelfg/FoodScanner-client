package kirillrychkov.foodscanner_client.app.domain.usecase.restrictions

import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import javax.inject.Inject

class PostSelectedAllergensUseCase @Inject constructor(
    val repository: ChooseRestrictionsRepository
) {
    operator fun invoke(listOfAllergens : List<Allergen>){
        repository.postSelectedAllergens(listOfAllergens)
    }
}