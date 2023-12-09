package com.ghn.poker.tracker.di

import co.touchlab.kermit.Logger
import org.koin.core.KoinApplication
import org.koin.core.annotation.ComponentScan
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.ksp.generated.module

fun initKoin(appModule: () -> Module): KoinApplication =
    startKoin {
        modules(
            appModule(),
            platformModule,
            RepositoryModule().module,
            UseCaseModule().module,
            DatabaseModule().module,
            SourcesModule().module,
            APIModule().module,
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

internal expect val platformModule: Module
