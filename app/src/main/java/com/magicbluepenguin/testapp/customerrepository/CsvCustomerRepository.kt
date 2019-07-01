package com.magicbluepenguin.testapp.customerrepository

import com.magicbluepenguin.testapp.data.customerfetcher.CustomerDataFetcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvCustomerRepository @Inject constructor(val customerDataFetcher: CustomerDataFetcher<String>) : CustomerRepository <String> {
    override fun getCustomersResponse(fromSource: String?) = customerDataFetcher.fetchAndParseCustomers(fromSource)
}