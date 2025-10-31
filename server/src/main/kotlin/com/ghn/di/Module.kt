package com.ghn.di

import com.ghn.client.GizmoRSSClient
import com.ghn.repository.RefreshTokenRepository
import com.ghn.repository.RefreshTokenRepositoryImpl
import com.ghn.repository.SessionRepository
import com.ghn.repository.SessionRepositoryImpl
import com.ghn.repository.UserRepository
import com.ghn.repository.UserRepositoryImpl
import com.ghn.service.EvaluatorService
import com.ghn.service.EvaluatorServiceImpl
import com.ghn.service.RefreshTokenService
import com.ghn.service.RefreshTokenServiceImpl
import com.ghn.service.SessionService
import com.ghn.service.SessionServiceImpl
import com.ghn.service.UserService
import com.ghn.service.UserServiceImpl
import com.prof18.rssparser.RssParser
import org.koin.dsl.module
import org.slf4j.LoggerFactory

val appModule = module {
    single<SessionService> { SessionServiceImpl(get()) }
    single<SessionRepository> { SessionRepositoryImpl(get()) }

    single<RefreshTokenRepository> { RefreshTokenRepositoryImpl(get()) }
    single<RefreshTokenService> { RefreshTokenServiceImpl(get()) }

    single<UserService> { UserServiceImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    single<EvaluatorService> { EvaluatorServiceImpl(LoggerFactory.getLogger("EvaluatorService")) }

    single { GizmoRSSClient(RssParser()) }
}
