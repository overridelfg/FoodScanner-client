package kirillrychkov.foodscanner_client.app.di

import dagger.Binds
import dagger.Module
import kirillrychkov.foodscanner_client.app.data.repository.AuthRepositoryImpl
import kirillrychkov.foodscanner_client.app.data.repository.ChooseRestrictionsRepositoryImpl
import kirillrychkov.foodscanner_client.app.data.repository.ProductsRepositoryImpl
import kirillrychkov.foodscanner_client.app.data.repository.ProfileRepositoryImpl
import kirillrychkov.foodscanner_client.app.domain.repository.AuthRepository
import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import kirillrychkov.foodscanner_client.app.domain.repository.ProductsRepository
import kirillrychkov.foodscanner_client.app.domain.repository.ProfileRepository
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl) : AuthRepository

    @Binds
    @Singleton
    abstract fun bindChooseRestrictionsRepository(impl: ChooseRestrictionsRepositoryImpl) : ChooseRestrictionsRepository

    @Binds
    @Singleton
    abstract fun bindProductsRepository(impl: ProductsRepositoryImpl): ProductsRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}