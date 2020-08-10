package me.alberto.pdfreader.framework

import android.app.Application

class MajesticReaderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MajesticViewModelFactory.inject(this, Interactors())
    }
}