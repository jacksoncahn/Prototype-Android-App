package com.jetbrains.kmpapp

import android.app.Application
import di.initKoin
import screens.DetailViewModel
import screens.ListViewModel
import org.koin.dsl.module

class MapnookInit : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            listOf(
                module {
                    factory { ListViewModel(get()) }
                    factory { DetailViewModel(get()) }
                }
            )
        )
    }
}
