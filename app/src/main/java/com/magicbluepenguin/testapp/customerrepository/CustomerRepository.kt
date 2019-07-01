package com.magicbluepenguin.testapp.customerrepository

import com.magicbluepenguin.testapp.data.DataResponse
import com.magicbluepenguin.testapp.data.customer.Customer

interface CustomerRepository<T> {
    fun getCustomersResponse(fromSource: String? = null): DataResponse<List<Customer>>
}