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

    fun getUser() : User?{
        val id = sharedPreferences.getString(ID_KEY, null)
        val email = sharedPreferences.getString(EMAIL_KEY, null)
        val name = sharedPreferences.getString(USERNAME_KEY, null)
        val accessToken = sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
        val refreshToken = sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
        val diets = sharedPreferences.getString(DIETS_KEY, null)
        val allergens = sharedPreferences.getString(ALLERGENS_KEY, null)
        if(!id.isNullOrBlank() && !email.isNullOrBlank() && !name.isNullOrBlank()
            && !accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank() && !diets.isNullOrBlank()
            && !allergens.isNullOrBlank()){

            val listOfDietsString = diets.split(":")
            val listOfAllergensString = allergens.split(":")

            val listOfDiets = decodeDietsList(listOfDietsString)
            val listOfAllergens = decodeAllergensList(listOfAllergensString)

            return User(
                id = id,
                email = email,
                name = name,
                accessToken = accessToken,
                refreshToken = refreshToken,
                diets = listOfDiets,
                allergens = listOfAllergens
            )
        }
        return null
    }

    fun refreshTokens(tokens: TokensResponseDTO){
        sharedPreferences.edit()
            .putString(ACCESS_TOKEN_KEY, tokens.accessToken)
            .putString(REFRESH_TOKEN_KEY, tokens.refreshToken)
            .apply()
    }

    fun saveToSharedPreferences(user: User?){
        if(user != null){
            sharedPreferences.edit()
                .putString(ID_KEY, user.id)
                .putString(USERNAME_KEY, user.name)
                .putString(EMAIL_KEY, user.email)
                .putString(ACCESS_TOKEN_KEY, user.accessToken)
                .putString(REFRESH_TOKEN_KEY, user.refreshToken)
                .putString(DIETS_KEY, encodeDietsList(user.diets))
                .putString(ALLERGENS_KEY, encodeAllergensList(user.allergens))
                .apply()
        }else{
            sharedPreferences.edit()
                .remove(ID_KEY)
                .remove(USERNAME_KEY)
                .remove(EMAIL_KEY)
                .remove(ACCESS_TOKEN_KEY)
                .remove(REFRESH_TOKEN_KEY)
                .remove(DIETS_KEY)
                .remove(ALLERGENS_KEY)
                .apply()
        }
    }

    fun saveListOfDiets(listOfDiets : List<Diet>?){
        if(listOfDiets != null){
            var diets = ""
            for (i in 0 until listOfDiets.size){
                val restrictedIngredientsList = listOfDiets[i].restrictedIngredients
                var restrictedIngredients = ""
                for(j in 0 until restrictedIngredientsList.size){
                    if(j == restrictedIngredientsList.size - 1){
                        restrictedIngredients += restrictedIngredientsList[j]
                    }else{
                        restrictedIngredients += restrictedIngredientsList[j] + "-"
                    }
                }
                if(i == listOfDiets.size - 1){
                    diets += listOfDiets[i].id.toString() + "," +
                            listOfDiets[i].title  + "," +
                            restrictedIngredients
                } else{
                    diets += listOfDiets[i].id.toString() + "," +
                            listOfDiets[i].title +
                            restrictedIngredients + ":"
                }
            }
            sharedPreferences.edit()
                .putString(DIETS_KEY, diets)
                .apply()
        }else{
            sharedPreferences.edit()
                .remove(DIETS_KEY)
                .apply()
        }

    }

    fun saveListOfAllergens(listOfAllergens : List<Allergen>?){
        if(listOfAllergens != null) {
            var allergens = ""
            for (i in 0 until listOfAllergens.size) {
                val restrictedIngredientsList = listOfAllergens[i].restrictedIngredients
                var restrictedIngredients = ""
                for(j in 0 until restrictedIngredientsList.size){
                    if(j == restrictedIngredientsList.size - 1){
                        restrictedIngredients += restrictedIngredientsList[j]
                    }else{
                        restrictedIngredients += restrictedIngredientsList[j] + "-"
                    }
                }
                if(i == listOfAllergens.size - 1){
                    allergens += listOfAllergens[i].id.toString() + "," +
                            listOfAllergens[i].title  + "," +
                            restrictedIngredients
                } else{
                    allergens += listOfAllergens[i].id.toString() + "," +
                            listOfAllergens[i].title +
                            restrictedIngredients + ":"
                }
            }
            sharedPreferences.edit()
                .putString(ALLERGENS_KEY, allergens)
                .apply()
        }else{
            sharedPreferences.edit()
                .remove(ALLERGENS_KEY)
                .apply()
        }
    }

    fun getListOfDiets() : MutableList<Diet>?{
        val diets = sharedPreferences.getString(DIETS_KEY, "")
        if(!diets.isNullOrBlank()) {
            val listOfDietsString = diets.split(":")
            return decodeDietsList(listOfDietsString)
        }
        return null
    }

    fun getListOfAllergens() : MutableList<Allergen>?{
        val allergens = sharedPreferences.getString(ALLERGENS_KEY, "")
        if(!allergens.isNullOrBlank()) {
            val listOfAllergensString = allergens.split(":")
            return decodeAllergensList(listOfAllergensString)
        }
        return null
    }

    private fun encodeDietsList(listOfDiets: List<Diet>) : String{
        var diets = ""
        for (i in 0 until listOfDiets.size){
            val diet = listOfDiets[i]
            val restrictedIngredientsList = listOfDiets[i].restrictedIngredients
            var restrictedIngredients = ""
            for(j in 0 until restrictedIngredientsList.size){
                if(j == restrictedIngredientsList.size - 1){
                    restrictedIngredients += restrictedIngredientsList[j]
                }else{
                    restrictedIngredients += restrictedIngredientsList[j] + "-"
                }
            }
            if(i == listOfDiets.size - 1){
                diets += listOfDiets[i].id.toString() + "," +
                        listOfDiets[i].title  + "," +
                        restrictedIngredients
            } else{
                diets += listOfDiets[i].id.toString() + "," +
                        listOfDiets[i].title +
                        restrictedIngredients + ":"
            }
        }
        return diets
    }

    private fun encodeAllergensList(listOfAllergens: List<Allergen>) : String{
        var allergens = ""
        for (i in 0 until listOfAllergens.size){
            val restrictedIngredientsList = listOfAllergens[i].restrictedIngredients
            var restrictedIngredients = ""
            for(j in 0 until restrictedIngredientsList.size){
                if(j == restrictedIngredientsList.size - 1){
                    restrictedIngredients += restrictedIngredientsList[j]
                }else{
                    restrictedIngredients += restrictedIngredientsList[j] + "-"
                }
            }
            if(i == listOfAllergens.size - 1){
                allergens += listOfAllergens[i].id.toString() + "," +
                        listOfAllergens[i].title  + "," +
                        restrictedIngredients
            } else{
                allergens += listOfAllergens[i].id.toString() + "," +
                        listOfAllergens[i].title +
                        restrictedIngredients + ":"
            }
        }
        return allergens
    }

    private fun decodeDietsList(listOfDietsString : List<String>) : MutableList<Diet>{
        val listOfDiets = mutableListOf<Diet>()

        for (diet in listOfDietsString){
            val dietId = diet.split(",")[0].toInt()
            val title = diet.split(",")[1]
            val restrictedIngredients = diet.split(",")[2]
            val restrictedIngredientsList = restrictedIngredients.split('-')
            listOfDiets.add(Diet(dietId, title, restrictedIngredientsList))
        }
        return listOfDiets
    }

    private fun decodeAllergensList(listOfAllergensString : List<String>) : MutableList<Allergen>{
        val listOfAllergens = mutableListOf<Allergen>()

        for (allergen in listOfAllergensString){
            val allergenId = allergen.split(",")[0].toInt()
            val title = allergen.split(",")[1]
            val restrictedIngredients = allergen.split(",")[2]
            val restrictedIngredientsList = restrictedIngredients.split('-')
            listOfAllergens.add(Allergen(allergenId, title, restrictedIngredientsList))
        }
        return listOfAllergens
    }

    companion object {
        private const val ID_KEY = "id"
        private const val USERNAME_KEY = "username"
        private const val EMAIL_KEY = "email"
        private const val REFRESH_TOKEN_KEY = "access_token"
        private const val ACCESS_TOKEN_KEY = "refresh_token"
        private const val DIETS_KEY = "diets"
        private const val ALLERGENS_KEY = "allergens"
    }
}