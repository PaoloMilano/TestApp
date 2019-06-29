package com.magicbluepenguin.testapp.base

import android.app.Application
import com.magicbluepenguin.testapp.dagger.AppComponent
import com.magicbluepenguin.testapp.dagger.DaggerAppComponent

class BaseApplication : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        this.appComponent = DaggerAppComponent.builder().withAssetManager(assets).build()
    }
}