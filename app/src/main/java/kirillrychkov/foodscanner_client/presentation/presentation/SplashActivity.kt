package kirillrychkov.foodscanner_client.presentation.presentation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.presentation.domain.repository.AuthRepository
import kirillrychkov.foodscanner_client.presentation.presentation.auth.AuthActivity
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    private val component by lazy{
        FoodScannerApp.appComponent
    }

    override fun attachBaseContext(newBase: Context?) {
        component.inject(this)
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if(savedInstanceState == null){
            if(authRepository.getUser() == null){
                val intent = AuthActivity.newIntentAuthActivity(this)
                startActivity(intent)
                finish()
            }else{
                val intent = MainActivity.newIntentMainActivity(this)
                startActivity(intent)
                finish()
            }
        }
    }
}