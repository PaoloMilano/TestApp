package com.magicbluepenguin.testapp.data

import com.magicbluepenguin.testapp.customer.Customer

interface CustomerDataFetcher <T>{
    fun fetchAndParseCustomers(fromSource: T? = null): List<Customer>
}
