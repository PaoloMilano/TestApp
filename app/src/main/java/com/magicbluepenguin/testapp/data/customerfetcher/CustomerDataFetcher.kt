package com.magicbluepenguin.testapp.data.customerfetcher

import com.magicbluepenguin.testapp.data.DataResponse
import com.magicbluepenguin.testapp.data.customer.Customer

interface CustomerDataFetcher <T> {
    fun fetchAndParseCustomers(fromSource: T? = null): DataResponse<List<Customer>>
}
