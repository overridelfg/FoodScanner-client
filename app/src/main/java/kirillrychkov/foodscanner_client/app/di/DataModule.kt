package kirillrychkov.foodscanner_client.app.di

import dagger.Binds
import dagger.Module
import kirillrychkov.foodscanner_client.app.data.repository.AuthRepositoryImpl
import kirillrychkov.foodscanner_client.app.data.repository.ChooseRestrictionsRepositoryImpl
import kirillrychkov.foodscanner_client.app.domain.repository.AuthRepository
import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl) : AuthRepository

    @Binds
    @Singleton
    abstract fun bindChooseRestrictionsRepository(impl: ChooseRestrictionsRepositoryImpl) : ChooseRestrictionsRepository
}