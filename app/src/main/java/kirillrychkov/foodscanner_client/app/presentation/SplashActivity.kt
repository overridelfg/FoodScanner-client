package kirillrychkov.foodscanner_client.app.presentation

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.repository.AuthRepository
import kirillrychkov.foodscanner_client.app.presentation.auth.AuthActivity
import okhttp3.internal.notify
import okhttp3.internal.wait
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit
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
//        val intent = MainActivity.newIntentMainActivity(this)
//        startActivity(intent)
//        finish()
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
