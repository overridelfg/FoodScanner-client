package kirillrychkov.foodscanner_client.app.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kirillrychkov.foodscanner_client.app.presentation.auth.AuthViewModel
import kirillrychkov.foodscanner_client.app.presentation.mainpage.barcodescan.BarcodeScannerViewModel
import kirillrychkov.foodscanner_client.app.presentation.mainpage.products.ProductsListViewModel
import kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.ProfileViewModel
import kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.update.UpdateRestrictionsViewModel
import kirillrychkov.foodscanner_client.app.presentation.restrictions.ChooseRestrictionsViewModel

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    fun bindAuthViewModel(authViewModel : AuthViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChooseRestrictionsViewModel::class)
    fun bindChooseRestrictionsViewModel(chooseRestrictionsViewModel : ChooseRestrictionsViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun bindProfileViewModel(profileViewModel: ProfileViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BarcodeScannerViewModel::class)
    fun bindBarcodeScannerViewModel(barcodeScannerViewModel: BarcodeScannerViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductsListViewModel::class)
    fun bindProductsListViewModel(productsListViewModel: ProductsListViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpdateRestrictionsViewModel::class)
    fun bindUpdateRestrictionsViewModel(updateRestrictionsViewModel: UpdateRestrictionsViewModel) : ViewModel
}