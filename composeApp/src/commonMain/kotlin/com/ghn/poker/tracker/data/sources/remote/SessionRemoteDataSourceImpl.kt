package com.ghn.poker.tracker.data.sources.remote

import com.ghn.gizmodb.common.models.SessionDTO
import com.ghn.poker.tracker.data.api.KtorProvider
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import org.koin.core.annotation.Single

@Single([SessionRemoteDataSource::class])
internal class SessionRemoteDataSourceImpl(
    private val provider: KtorProvider
) : SessionRemoteDataSource {
    override suspend fun getSessions(): List<SessionDTO> {
        return provider.client
            .get {
                url {
                    protocol = URLProtocol.HTTP
                    host = "138.197.84.104"
                    path("sessions")
                }
            }.body<List<SessionDTO>>()
    }
}
