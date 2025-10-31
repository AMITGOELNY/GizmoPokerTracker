package com.ghn.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.ghn.service")
class ServiceModule

@Module
@ComponentScan("com.ghn.repository")
class RepositoryModule

@Module
@ComponentScan("com.ghn.client")
class ClientModule
