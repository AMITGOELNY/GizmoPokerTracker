package com.ghn.poker.tracker.di

import com.ghn.poker.tracker.data.database.DataBaseDriver
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    single { DataBaseDriver().createDriver() }

    single {
        OkHttp.create {
            config { retryOnConnectionFailure(true) }
        }
    }
}
