package com.magicbluepenguin.testapp.dagger

import android.content.res.AssetManager
import com.magicbluepenguin.testapp.data.customerfetcher.CsvCustomerDataFetcher
import com.magicbluepenguin.testapp.data.customerfetcher.CustomerDataFetcher
import dagger.Binds
import dagger.Module

@Module
abstract class CsvCustomerDataFetcherModule(val assetManager: AssetManager) {

    @Binds
    abstract fun bindCsvCustomerDataFetcher(csvCustomerFetcher: CsvCustomerDataFetcher): CustomerDataFetcher<String>
}