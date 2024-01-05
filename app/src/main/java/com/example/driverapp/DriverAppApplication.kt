package com.example.driverapp

import android.app.Application
import com.example.driverapp.module.appModule
import org.koin.core.context.GlobalContext.startKoin

class DriverAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule)
        }
    }
}