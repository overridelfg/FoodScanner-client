package kirillrychkov.foodscanner_client.app.presentation

sealed class ViewState<out S : Any?, out E : Any?> {
    object Loading : ViewState<Nothing, Nothing>()

    data class Success<out S : Any?>(val result: S) : ViewState<S, Nothing>()

    data class Error<out E : Any?>(val result: E) : ViewState<Nothing, E>()

    companion object {
        fun <S> success(data: S) = Success(data)

        fun loading() = Loading

        fun <E> error(message: E) = Error(message)
    }
}