package com.ghn.poker.tracker.di

import co.touchlab.kermit.Logger
import com.ghn.poker.core.database.di.DatabaseModule
import com.ghn.poker.core.network.di.NetworkModule
import com.ghn.poker.core.preferences.di.PreferencesModule
import com.ghn.poker.feature.auth.AuthModule
import com.ghn.poker.feature.auth.domain.usecase.impl.MainViewModel
import com.ghn.poker.feature.cards.CardsModule
import com.ghn.poker.feature.feed.FeedModule
import com.ghn.poker.feature.tracker.TrackerModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
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
            DatabaseModule().module,
            NetworkModule().module,
            PreferencesModule().module,
            // Feature modules
            AuthModule().module,
            TrackerModule().module,
            FeedModule().module,
            CardsModule().module,
            module {
                viewModel { MainViewModel(get(), get()) }
            }
        ).also {
            Logger.d("KMM Koin init Complete")
        }
    }

internal expect val platformModule: Module
