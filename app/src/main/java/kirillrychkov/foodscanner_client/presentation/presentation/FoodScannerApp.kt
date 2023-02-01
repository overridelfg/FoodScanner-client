package kirillrychkov.foodscanner_client.presentation.presentation

import android.app.Application
import kirillrychkov.foodscanner_client.presentation.di.AppComponent
import kirillrychkov.foodscanner_client.presentation.di.DaggerAppComponent

class FoodScannerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appContext(this)
            .build()
        appComponent.inject(this)
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}