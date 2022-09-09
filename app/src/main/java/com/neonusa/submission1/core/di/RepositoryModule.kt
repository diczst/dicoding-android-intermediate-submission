package com.neonusa.submission1.core.di

import com.neonusa.submission1.core.data.repository.AppRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { AppRepository(get()) }
}