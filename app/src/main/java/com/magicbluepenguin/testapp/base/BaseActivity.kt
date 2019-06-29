package com.magicbluepenguin.testapp.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    fun <T : ViewModel> getViewModel(viewModelType: Class<T>): T {
        if (!::viewModelFactory.isInitialized) {
            (this.application as BaseApplication).appComponent.inject(this)
        }
        return ViewModelProviders.of(this, viewModelFactory)[viewModelType]
    }
}