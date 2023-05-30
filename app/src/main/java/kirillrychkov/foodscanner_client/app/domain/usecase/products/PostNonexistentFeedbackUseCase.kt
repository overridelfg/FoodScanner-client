package kirillrychkov.foodscanner_client.app.domain.usecase.products

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.FeedbackImages
import kirillrychkov.foodscanner_client.app.domain.entity.SuccessResponse
import kirillrychkov.foodscanner_client.app.domain.repository.ProductsRepository
import javax.inject.Inject

class PostNonexistentFeedbackUseCase  @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(feedbackImages: FeedbackImages): OperationResult<SuccessResponse, String?> {
        return productsRepository.feedbackNonexistentProducts(feedbackImages)
    }
}