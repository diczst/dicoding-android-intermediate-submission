package com.neonusa.submission1.core.di

import com.neonusa.submission1.ui.login.LoginViewModel
import com.neonusa.submission1.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{ RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}