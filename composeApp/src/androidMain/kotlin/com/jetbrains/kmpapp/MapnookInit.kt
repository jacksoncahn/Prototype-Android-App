package com.jetbrains.kmpapp//package com.jetbrains.kmpapp

import android.app.Application
//import di.initKoin
//import screens.DetailViewModel
//import screens.ListViewModel
//import org.koin.dsl.module

class MapnookInit : Application() {
    override fun onCreate() {
        super.onCreate()
        //commented out all of this initKoin stuff because I accidentally deleted the files it needs
        //but it doesn't seem like this functionality was necessary for Mapnook Mobile anyway
//        initKoin(
//            listOf(
//                module {
//                    factory { ListViewModel(get()) }
//                    factory { DetailViewModel(get()) }
//                }
//            )
//        )
    }
}
