package com.magicbluepenguin.testapp.customerrepository

import com.magicbluepenguin.testapp.customer.Customer

interface CustomerRepository<T> {
    fun getCustomers(fromSource: String? = null): List<Customer>
}