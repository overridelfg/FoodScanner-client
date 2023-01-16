package kirillrychkov.foodscanner_client.presentation.presentation

sealed class ViewState<out S : Any?, out E : Any?> {
    object Loading : ViewState<Nothing, Nothing>()

    data class Success<out S : Any?>(val result: S) : ViewState<S, Nothing>()

    data class Error<out S : Any?, out E : Any?>(val oldvalue: S, val result: E) : ViewState<S, E>()

    companion object {
        fun <S> success(data: S) = Success(data)

        fun loading() = Loading

        fun <S, E> error(oldValue: S, message: E) = Error(oldValue, message)
    }
}