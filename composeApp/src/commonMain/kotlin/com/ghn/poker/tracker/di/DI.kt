package com.ghn.poker.tracker.di

import co.touchlab.kermit.Logger
import com.ghn.poker.tracker.data.preferences.PreferenceManager
import com.ghn.poker.tracker.data.preferences.PrefsManager
import com.ghn.poker.tracker.domain.usecase.impl.AppState
import com.ghn.poker.tracker.domain.usecase.impl.Store
import com.ghn.poker.tracker.domain.usecase.impl.UserStore
import io.ktor.client.HttpClient
import org.koin.core.KoinApplication
import org.koin.core.annotation.ComponentScan
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ksp.generated.module

const val DEFAULT_SETTINGS_NAME = "DEFAULT_SETTINGS"
const val ENCRYPTED_SETTINGS_NAME = "ENCRYPTED_SETTINGS"

fun initKoin(appModule: () -> Module): KoinApplication =
    startKoin {
        modules(
            appModule(),
            module { single { HttpClient(engine = get()) } },
            platformModule,
            RepositoryModule().module,
            UseCaseModule().module,
            DatabaseModule().module,
            SourcesModule().module,
            APIModule().module,
            ViewModelModule().module,
            storageModule,
            sharedViewModelModule
        ).also {
            Logger.d("KMM Koin init Complete")
        }
    }

@org.koin.core.annotation.Module
@ComponentScan("com.ghn.poker.tracker.data.repository")
class RepositoryModule

@org.koin.core.annotation.Module
@ComponentScan("com.ghn.poker.tracker.domain.usecase")
class UseCaseModule

@org.koin.core.annotation.Module
@ComponentScan("com.ghn.poker.tracker.data.database")
class DatabaseModule

@org.koin.core.annotation.Module
@ComponentScan("com.ghn.poker.tracker.data.sources")
class SourcesModule

@org.koin.core.annotation.Module
@ComponentScan("com.ghn.poker.tracker.data.api")
class APIModule

@org.koin.core.annotation.Module
@ComponentScan(
    "com.ghn.poker.tracker.presentation.session",
    "com.ghn.poker.tracker.presentation.login",
    "com.ghn.poker.tracker.presentation.feed",
    "com.ghn.poker.tracker.presentation.cards"
)
class ViewModelModule

internal expect val platformModule: Module

val sharedViewModelModule = module {

    single<Store<AppState>> { UserStore(get()) }
}

internal val storageModule = module {
    single<PreferenceManager> {
        PrefsManager(get(named(ENCRYPTED_SETTINGS_NAME)), get(named(DEFAULT_SETTINGS_NAME)))
    }
}
