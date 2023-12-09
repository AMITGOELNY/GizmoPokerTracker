package com.ghn.poker.tracker.data.api

import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single([KtorProvider::class])
internal actual class KtorProviderImpl : KtorProvider {
    override val client = HttpClient { getCommonConfig() }
}