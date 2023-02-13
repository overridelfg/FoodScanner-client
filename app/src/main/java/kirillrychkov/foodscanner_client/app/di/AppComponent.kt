package kirillrychkov.foodscanner_client.app.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.SplashActivity
import kirillrychkov.foodscanner_client.app.presentation.auth.LoginFragment
import kirillrychkov.foodscanner_client.app.presentation.auth.RegisterFragment
import kirillrychkov.foodscanner_client.app.presentation.restrictions.ChooseAllergensFragment
import kirillrychkov.foodscanner_client.app.presentation.restrictions.ChooseDietsFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DataModule::class, ViewModelModule::class]
)
interface AppComponent{

    fun inject(app: FoodScannerApp)

    fun inject(splashActivity: SplashActivity)

    fun inject(loginFragment: LoginFragment)

    fun inject(registerFragment: RegisterFragment)

    fun inject(chooseDietsFragment: ChooseDietsFragment)

    fun inject(chooseAllergensFragment: ChooseAllergensFragment)


    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun appContext(context: Context): Builder
    }

}