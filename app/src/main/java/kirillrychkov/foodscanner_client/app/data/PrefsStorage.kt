package kirillrychkov.foodscanner_client.app.data

import android.content.Context
import android.util.Log
import kirillrychkov.foodscanner_client.app.data.network.models.TokensResponseDTO
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.entity.User
import javax.inject.Inject

class PrefsStorage @Inject constructor(
    context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(
        "User data preferences",
        Context.MODE_PRIVATE
    )

    fun getUser(): User? {
        val id = sharedPreferences.getString(ID_KEY, null)
        val email = sharedPreferences.getString(EMAIL_KEY, null)
        val name = sharedPreferences.getString(USERNAME_KEY, null)
        val accessToken = sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
        val refreshToken = sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
        if (!id.isNullOrBlank() && !email.isNullOrBlank() && !name.isNullOrBlank()
            && !accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()
        ) {
            return User(
                id = id,
                email = email,
                name = name,
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }
        return null
    }

    fun refreshTokens(tokens: TokensResponseDTO) {
        sharedPreferences.edit()
            .putString(ACCESS_TOKEN_KEY, tokens.accessToken)
            .putString(REFRESH_TOKEN_KEY, tokens.refreshToken)
            .apply()
    }

    fun saveToSharedPreferences(user: User?) {
        if (user != null) {
            sharedPreferences.edit()
                .putString(ID_KEY, user.id)
                .putString(USERNAME_KEY, user.name)
                .putString(EMAIL_KEY, user.email)
                .putString(ACCESS_TOKEN_KEY, user.accessToken)
                .putString(REFRESH_TOKEN_KEY, user.refreshToken)
                .apply()
        } else {
            sharedPreferences.edit()
                .remove(ID_KEY)
                .remove(USERNAME_KEY)
                .remove(EMAIL_KEY)
                .remove(ACCESS_TOKEN_KEY)
                .remove(REFRESH_TOKEN_KEY)
                .apply()
        }
    }



    companion object {
        private const val ID_KEY = "id"
        private const val USERNAME_KEY = "username"
        private const val EMAIL_KEY = "email"
        private const val REFRESH_TOKEN_KEY = "access_token"
        private const val ACCESS_TOKEN_KEY = "refresh_token"
    }
}