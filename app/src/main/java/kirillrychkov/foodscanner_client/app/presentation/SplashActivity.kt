package kirillrychkov.foodscanner_client.app.presentation

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.repository.AuthRepository
import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import kirillrychkov.foodscanner_client.app.presentation.auth.AuthActivity
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var chooseRestrictionsRepository: ChooseRestrictionsRepository

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
//        val intent = MainActivity.newIntentMainActivity(this)
//        startActivity(intent)
//        finish()
        if(savedInstanceState == null){
            if(authRepository.getUser() == null){
                chooseRestrictionsRepository.removeSelectedRestrictions()
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

