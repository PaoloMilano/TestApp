package com.magicbluepenguin.testapp.customerrepository

import com.magicbluepenguin.testapp.customer.Customer
import com.magicbluepenguin.testapp.data.DataResponse

interface CustomerRepository<T> {
    fun getCustomersResponse(fromSource: String? = null): DataResponse<List<Customer>>
}