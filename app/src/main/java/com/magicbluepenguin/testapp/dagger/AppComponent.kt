package com.magicbluepenguin.testapp.dagger

import android.content.res.AssetManager
import com.magicbluepenguin.testapp.base.BaseActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, CsvCustomerDataFetcherModule::class, CsvCustomerRepositoryModule::class])
interface AppComponent {
    fun inject(baseActivity: BaseActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withAssetManager(assetManager: AssetManager): AppComponent.Builder

        fun build(): AppComponent
    }
}