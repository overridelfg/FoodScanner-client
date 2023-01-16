package kirillrychkov.foodscanner_client.presentation.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kirillrychkov.foodscanner_client.presentation.presentation.auth.AuthViewModel

//@Module
//interface ViewModelModule {
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(AuthViewModel::class)
//    fun bindAuthViewModel(authViewModel : AuthViewModel) : ViewModel
//}