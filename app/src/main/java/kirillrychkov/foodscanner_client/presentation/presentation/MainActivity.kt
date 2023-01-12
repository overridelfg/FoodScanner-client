package kirillrychkov.foodscanner_client.presentation.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.presentation.presentation.auth.AuthActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }
}