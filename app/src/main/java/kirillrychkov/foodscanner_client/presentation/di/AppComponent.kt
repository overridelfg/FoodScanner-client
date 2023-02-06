package kirillrychkov.foodscanner_client.presentation.di

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Provides
import kirillrychkov.foodscanner_client.presentation.domain.repository.AuthRepository
import kirillrychkov.foodscanner_client.presentation.domain.repository.ChooseRestrictionsRepository
import kirillrychkov.foodscanner_client.presentation.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.presentation.presentation.MainActivity
import kirillrychkov.foodscanner_client.presentation.presentation.SplashActivity
import kirillrychkov.foodscanner_client.presentation.presentation.auth.AuthActivity
import kirillrychkov.foodscanner_client.presentation.presentation.auth.LoginFragment
import kirillrychkov.foodscanner_client.presentation.presentation.auth.RegisterFragment
import kirillrychkov.foodscanner_client.presentation.presentation.restrictions.ChooseAllergensFragment
import kirillrychkov.foodscanner_client.presentation.presentation.restrictions.ChooseDietsFragment
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