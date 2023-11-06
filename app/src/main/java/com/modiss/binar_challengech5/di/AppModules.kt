package com.modiss.binar_challengech5.di

import com.modiss.binar_challengech5.database.AppDatabase
import com.modiss.binar_challengech5.repository.CartRepository
import com.modiss.binar_challengech5.utils.AssetWrapper
import com.modiss.binar_challengech5.utils.PreferenceDataStoreHelper
import com.modiss.binar_challengech5.utils.PreferenceDataStoreHelperImpl
import com.chuckerteam.chucker.api.ChuckerInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    private val localModule = module {
        single { AppDatabase.getInstance(androidContext()) }
        single { get<AppDatabase>().cartDao() }
        single { androidContext().appDataStore }
        single<PreferenceDataStoreHelper> { PreferenceDataStoreHelperImpl(get()) }
    }

    private val networkModule = module {
        single { ChuckerInterceptor(androidContext()) }
        single { challengech5ApiService.invoke(get()) }
    }

    private val dataSourceModule = module {
        single<CartDataSource> { CartDatabaseDataSource(get()) }
        single<UserPreferenceDataSource> { UserPreferenceDataSourceImpl(get()) }
        single<challengech5DataSource> { challengech5ApiDataSource(get()) }
    }

    private val repositoryModule = module {
        single<CartRepository> { CartRepositoryImpl(get(), get()) }
    }

    private val viewModelModule = module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::CartViewModel)
    }

    private val utilsModule = module {
        single { AssetWrapper(androidContext()) }
    }

    val modules: List<Module> = listOf(
        localModule,
        networkModule,
        dataSourceModule,
        repositoryModule,
        viewModelModule,
        utilsModule
    )
}

