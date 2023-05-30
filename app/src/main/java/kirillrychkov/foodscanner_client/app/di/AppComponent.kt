package kirillrychkov.foodscanner_client.app.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.SplashActivity
import kirillrychkov.foodscanner_client.app.presentation.auth.LoginFragment
import kirillrychkov.foodscanner_client.app.presentation.auth.RegisterFragment
import kirillrychkov.foodscanner_client.app.presentation.mainpage.barcodescan.BarcodeScannerFragment
import kirillrychkov.foodscanner_client.app.presentation.mainpage.barcodescan.history.BarcodeScannerHistoryActivity
import kirillrychkov.foodscanner_client.app.presentation.mainpage.favorites.FavoritesFragment
import kirillrychkov.foodscanner_client.app.presentation.mainpage.feedbacks.nonexistent.FirstProductPhotoFragment
import kirillrychkov.foodscanner_client.app.presentation.mainpage.feedbacks.nonexistent.FirstProductPhotoImageFragment
import kirillrychkov.foodscanner_client.app.presentation.mainpage.feedbacks.nonexistent.SecondPhotoProductFragment
import kirillrychkov.foodscanner_client.app.presentation.mainpage.feedbacks.nonexistent.SecondPhotoProductImageFragment
import kirillrychkov.foodscanner_client.app.presentation.mainpage.products.ProductsListFragment
import kirillrychkov.foodscanner_client.app.presentation.mainpage.products.details.ProductDetailsFragment
import kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.restrictionslist.AllergensListFragment
import kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.restrictionslist.DietsListFragment
import kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.ProfileFragment
import kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.update.UpdateAllergensFragment
import kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.update.UpdateDietsFragment
import kirillrychkov.foodscanner_client.app.presentation.restrictions.ChooseAllergensFragment
import kirillrychkov.foodscanner_client.app.presentation.restrictions.ChooseDietsFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DataModule::class, NetworkModule::class, ViewModelModule::class]
)
interface AppComponent{

    fun inject(app: FoodScannerApp)

    fun inject(splashActivity: SplashActivity)

    fun inject(loginFragment: LoginFragment)

    fun inject(registerFragment: RegisterFragment)

    fun inject(chooseDietsFragment: ChooseDietsFragment)

    fun inject(chooseAllergensFragment: ChooseAllergensFragment)

    fun inject(productsListFragment: ProductsListFragment)

    fun inject(barcodeScannerFragment: BarcodeScannerFragment)

    fun inject(profileFragment: ProfileFragment)

    fun inject(dietsListFragment: DietsListFragment)

    fun inject(allergensListFragment: AllergensListFragment)

    fun inject(favoritesFragment: FavoritesFragment)

    fun inject(barcodeScannerHistoryActivity: BarcodeScannerHistoryActivity)

    fun inject(productDetailsFragment: ProductDetailsFragment)

    fun inject(updateDietsFragment: UpdateDietsFragment)

    fun inject(updateAllergensFragment: UpdateAllergensFragment)

    fun inject(secondPhotoProductImageFragment: SecondPhotoProductImageFragment)

    fun inject(firstProductPhotoFragment : FirstProductPhotoFragment)

    fun inject(firstProductPhotoImageFragment: FirstProductPhotoImageFragment)

    fun inject(secondPhotoProductFragment: SecondPhotoProductFragment)
    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun appContext(context: Context): Builder

    }

}