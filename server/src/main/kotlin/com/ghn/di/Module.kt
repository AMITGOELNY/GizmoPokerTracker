package com.ghn.di

import com.ghn.client.GizmoRSSClient
import com.ghn.repository.SessionRepository
import com.ghn.repository.SessionRepositoryImpl
import com.ghn.repository.UserRepository
import com.ghn.repository.UserRepositoryImpl
import com.ghn.service.SessionService
import com.ghn.service.SessionServiceImpl
import com.ghn.service.UserService
import com.ghn.service.UserServiceImpl
import com.prof18.rssparser.RssParser
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val appModule = module {
    single<SessionService> { SessionServiceImpl(get()) }
    single<SessionRepository> { SessionRepositoryImpl(get()) }

    single<UserService> { UserServiceImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single { GizmoRSSClient(RssParser()) }
//    single<DataSource> { DatabaseConfig(get()).dataSource() }
}

val jsonModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
}
