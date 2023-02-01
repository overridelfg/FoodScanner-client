package kirillrychkov.foodscanner_client.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kirillrychkov.foodscanner_client.presentation.data.repository.AuthRepositoryImpl
import kirillrychkov.foodscanner_client.presentation.data.repository.ChooseRestrictionsRepositoryImpl
import kirillrychkov.foodscanner_client.presentation.domain.repository.AuthRepository
import kirillrychkov.foodscanner_client.presentation.domain.repository.ChooseRestrictionsRepository
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