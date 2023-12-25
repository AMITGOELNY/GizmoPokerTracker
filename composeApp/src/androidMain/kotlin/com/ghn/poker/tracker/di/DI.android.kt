package com.ghn.poker.tracker.di

import com.ghn.poker.tracker.data.database.DataBaseDriver
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    single { DataBaseDriver(get()).createDriver() }
    single {
        OkHttp.create {
//            preconfigured = okHttpClient
            config { retryOnConnectionFailure(true) }
        }
    }
}
