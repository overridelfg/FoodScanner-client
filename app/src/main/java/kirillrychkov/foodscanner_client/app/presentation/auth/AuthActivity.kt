package kirillrychkov.foodscanner_client.app.presentation.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kirillrychkov.foodscanner_client.R

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

    companion object{
        fun newIntentAuthActivity(context: Context): Intent {
            return Intent(context, AuthActivity::class.java)
        }
    }
}