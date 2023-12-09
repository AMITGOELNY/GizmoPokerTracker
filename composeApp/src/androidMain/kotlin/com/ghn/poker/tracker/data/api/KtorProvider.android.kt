package com.ghn.poker.tracker.data.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.annotation.Single

@Single([KtorProvider::class])
internal actual class KtorProviderImpl : KtorProvider {
    override val client: HttpClient = HttpClient(OkHttp) {
        engine {
//            preconfigured = okHttpClient
            config { retryOnConnectionFailure(true) }
        }
        getCommonConfig()
    }
}
