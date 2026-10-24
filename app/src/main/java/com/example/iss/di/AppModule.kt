package com.example.iss.di

import com.example.iss.data.AppDatabase
import com.example.iss.data.IssDao
import com.example.iss.viewmodel.AstroViewModel
import com.example.iss.viewmodel.IssViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Database
    single<AppDatabase> { AppDatabase.getDatabase(get()) }
    single<IssDao> { get<AppDatabase>().issDao() }

    // ViewModels
    viewModel { IssViewModel(get()) }
    viewModel { AstroViewModel() }
}
