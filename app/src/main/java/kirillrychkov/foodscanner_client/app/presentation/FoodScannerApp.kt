package kirillrychkov.foodscanner_client.app.presentation

import android.app.Application
import kirillrychkov.foodscanner_client.app.di.AppComponent
import kirillrychkov.foodscanner_client.app.di.DaggerAppComponent

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