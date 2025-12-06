package com.ghn.poker.tracker.di

import co.touchlab.kermit.Logger
import com.ghn.poker.core.network.di.NetworkModule
import com.ghn.poker.core.preferences.di.PreferencesModule
import com.ghn.poker.feature.auth.AuthModule
import com.ghn.poker.feature.auth.domain.usecase.impl.AppState
import com.ghn.poker.feature.auth.domain.usecase.impl.Store
import com.ghn.poker.feature.auth.domain.usecase.impl.UserStore
import com.ghn.poker.feature.cards.CardsModule
import com.ghn.poker.feature.feed.FeedModule
import com.ghn.poker.feature.tracker.TrackerModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import org.koin.core.KoinApplication
import org.koin.core.annotation.ComponentScan
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module

fun initKoin(appModule: () -> Module): KoinApplication =
    startKoin {
        modules(
            appModule(),
            module {
                single<HttpClient> {
                    val engine: HttpClientEngine? = getOrNull()
                    if (engine != null) HttpClient(engine) else HttpClient()
                }
            },
            platformModule,
            // Core modules
            NetworkModule().module,
            PreferencesModule().module,
            // Feature modules
            AuthModule().module,
            TrackerModule().module,
            FeedModule().module,
            CardsModule().module,
            // Legacy composeApp modules (to be migrated)
            RepositoryModule().module,
            UseCaseModule().module,
            DatabaseModule().module,
            SourcesModule().module,
            APIModule().module,
            ViewModelModule().module,
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
    single<Store<AppState>> { UserStore(get(), get()) }
}
