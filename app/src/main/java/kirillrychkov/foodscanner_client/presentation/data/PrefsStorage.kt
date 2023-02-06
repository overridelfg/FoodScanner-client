package kirillrychkov.foodscanner_client.presentation.data

import android.content.Context
import android.util.Log
import kirillrychkov.foodscanner_client.presentation.domain.entity.Allergen
import kirillrychkov.foodscanner_client.presentation.domain.entity.Diet
import kirillrychkov.foodscanner_client.presentation.domain.entity.User
import javax.inject.Inject

class PrefsStorage @Inject constructor(
    context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(
        "User data preferences",
        Context.MODE_PRIVATE
    )

    fun getUser() : User?{
        val id = sharedPreferences.getLong(ID_KEY, -1)
        val email = sharedPreferences.getString(EMAIL_KEY, null)
        val username = sharedPreferences.getString(USERNAME_KEY, null)
        val password = sharedPreferences.getString(PASSWORD_KEY, null)
        val token = sharedPreferences.getString(TOKEN_KEY, null)
        val diets = sharedPreferences.getString(DIETS_KEY, null)
        val allergens = sharedPreferences.getString(ALLERGENS_KEY, null)
        if(id != -1L && !email.isNullOrBlank() && !username.isNullOrBlank()
            && !token.isNullOrBlank() && !password.isNullOrBlank()
            && !diets.isNullOrBlank() && !allergens.isNullOrBlank()){
            val listOfDietsString = diets.split(":")
            val listOfAllergensString = diets.split(":")
            val listOfDiets = mutableListOf<Diet>()
            val listOfAllergens = mutableListOf<Allergen>()

            for (diet in listOfDietsString){
                val dietId = diet.split(",")[0].toInt()
                val title = diet.split(",")[1]
                listOfDiets.add(Diet(dietId, title))
            }

            for (allergen in listOfAllergensString){
                val allergenId = allergen.split(",")[0].toInt()
                val title = allergen.split(",")[1]
                listOfAllergens.add(Allergen(allergenId, title))
            }
            return User(
                id = id,
                email = email,
                username = username,
                password = password,
                token = token,
                diets = listOfDiets,
                allergens = listOfAllergens
            )
        }
        return null
    }

    fun saveToSharedPreferences(user: User?){
        if(user != null){
            var diets = ""
            for (i in 0 until user.diets.size){
                val diet = user.diets[i]
                if(i == user.diets.size - 1){
                    diets += diet.id.toString() + "," + diet.title
                } else{
                    diets += diet.id.toString() + "," + diet.title + ":"
                }
            }
            var allergens = ""
            for (i in 0 until user.allergens.size){
                val allergen = user.allergens[i]
                if(i == user.allergens.size - 1){
                    allergens += allergen.id.toString() + "," + allergen.title
                } else{
                    allergens += allergen.id.toString() + "," + allergen.title + ":"
                }
            }

            sharedPreferences.edit()
                .putLong(ID_KEY, user.id)
                .putString(PASSWORD_KEY, user.password)
                .putString(USERNAME_KEY, user.username)
                .putString(EMAIL_KEY, user.email)
                .putString(TOKEN_KEY, user.token)
                .putString(DIETS_KEY, diets)
                .putString(ALLERGENS_KEY, allergens)
                .apply()
        }else{
            sharedPreferences.edit()
                .remove(ID_KEY)
                .remove(PASSWORD_KEY)
                .remove(USERNAME_KEY)
                .remove(EMAIL_KEY)
                .remove(TOKEN_KEY)
                .remove(DIETS_KEY)
                .remove(ALLERGENS_KEY)
                .apply()
        }
    }

    companion object {
        private const val ID_KEY = "id"
        private const val USERNAME_KEY = "username"
        private const val EMAIL_KEY = "email"
        private const val PASSWORD_KEY = "password"
        private const val TOKEN_KEY = "token"
        private const val DIETS_KEY = "diets"
        private const val ALLERGENS_KEY = "allergens"
    }
}