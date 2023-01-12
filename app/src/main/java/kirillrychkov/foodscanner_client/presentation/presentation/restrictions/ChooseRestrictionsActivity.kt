package kirillrychkov.foodscanner_client.presentation.presentation.restrictions

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kirillrychkov.foodscanner_client.R

class ChooseRestrictionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_restrictions)
    }

    companion object{
        fun newIntentChooseRestrictions(context: Context): Intent {
            return Intent(context, ChooseRestrictionsActivity::class.java)
        }
    }
}