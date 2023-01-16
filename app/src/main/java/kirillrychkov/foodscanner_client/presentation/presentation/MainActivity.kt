package kirillrychkov.foodscanner_client.presentation.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.presentation.presentation.auth.AuthActivity
import kirillrychkov.foodscanner_client.presentation.presentation.restrictions.ChooseRestrictionsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
    companion object{
        fun newIntentMainActivity(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}