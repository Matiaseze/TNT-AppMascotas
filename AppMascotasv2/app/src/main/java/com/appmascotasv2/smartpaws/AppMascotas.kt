package com.appmascotasv2.smartpaws

import android.app.Application
import com.appmascotasv2.smartpaws.di.AppContainer

class AppMascotas : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}