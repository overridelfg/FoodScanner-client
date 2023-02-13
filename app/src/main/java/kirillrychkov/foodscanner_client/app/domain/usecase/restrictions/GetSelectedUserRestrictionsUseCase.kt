package kirillrychkov.foodscanner_client.app.domain.usecase.restrictions

import androidx.lifecycle.LiveData
import kirillrychkov.foodscanner_client.app.domain.entity.UserRestrictions
import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import javax.inject.Inject

class GetSelectedUserRestrictionsUseCase @Inject constructor(
    private val repository: ChooseRestrictionsRepository
){
    operator fun invoke(userId: Int) : LiveData<List<UserRestrictions>>{
       TODO()
    }
}