package kirillrychkov.foodscanner_client.app.data

import android.content.Context
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
            val listOfAllergensString = allergens.split(":")

            val listOfDiets = decodeDietsList(listOfDietsString)
            val listOfAllergens = decodeAllergensList(listOfAllergensString)

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
        val diets = getListOfDiets()
        val allergens = getListOfAllergens()
        if(user != null && diets != null && allergens != null){
            sharedPreferences.edit()
                .putLong(ID_KEY, user.id)
                .putString(PASSWORD_KEY, user.password)
                .putString(USERNAME_KEY, user.username)
                .putString(EMAIL_KEY, user.email)
                .putString(TOKEN_KEY, user.token)
                .putString(DIETS_KEY, encodeDietsList(diets))
                .putString(ALLERGENS_KEY, encodeAllergensList(allergens))
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

    fun saveListOfDiets(listOfDiets : List<Diet>?){
        if(listOfDiets != null){
            var diets = ""
            for (i in 0 until listOfDiets.size){
                if(i == listOfDiets.size - 1){
                    diets += listOfDiets[i].id.toString() + "," + listOfDiets[i].title
                } else{
                    diets += listOfDiets[i].id.toString() + "," + listOfDiets[i].title + ":"
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
                if (i == listOfAllergens.size - 1) {
                    allergens += listOfAllergens[i].id.toString() + "," + listOfAllergens[i].title
                } else {
                    allergens += listOfAllergens[i].id.toString() + "," + listOfAllergens[i].title + ":"
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
            if(i == listOfDiets.size - 1){
                diets += diet.id.toString() + "," + diet.title
            } else{
                diets += diet.id.toString() + "," + diet.title + ":"
            }
        }
        return diets
    }

    private fun encodeAllergensList(listOfAllergens: List<Allergen>) : String{
        var allergens = ""
        for (i in 0 until listOfAllergens.size){
            val allergen = listOfAllergens[i]
            if(i == listOfAllergens.size - 1){
                allergens += allergen.id.toString() + "," + allergen.title
            } else{
                allergens += allergen.id.toString() + "," + allergen.title + ":"
            }
        }
        return allergens
    }

    private fun decodeDietsList(listOfDietsString : List<String>) : MutableList<Diet>{
        val listOfDiets = mutableListOf<Diet>()

        for (diet in listOfDietsString){
            val dietId = diet.split(",")[0].toInt()
            val title = diet.split(",")[1]
            listOfDiets.add(Diet(dietId, title))
        }
        return listOfDiets
    }

    private fun decodeAllergensList(listOfAllergensString : List<String>) : MutableList<Allergen>{
        val listOfAllergens = mutableListOf<Allergen>()

        for (allergen in listOfAllergensString){
            val allergenId = allergen.split(",")[0].toInt()
            val title = allergen.split(",")[1]
            listOfAllergens.add(Allergen(allergenId, title))
        }
        return listOfAllergens
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