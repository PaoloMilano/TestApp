package com.magicbluepenguin.testapp.dagger

import android.content.res.AssetManager
import com.magicbluepenguin.testapp.data.CsvCustomerDataFetcher
import com.magicbluepenguin.testapp.data.CustomerDataFetcher
import dagger.Binds
import dagger.Module

@Module
abstract class CsvCustomerDataFetcherModule(val assetManager: AssetManager) {

    @Binds
    abstract fun bindCsvCustomerDataFetcher(csvCustomerFetcher: CsvCustomerDataFetcher): CustomerDataFetcher<String>
}