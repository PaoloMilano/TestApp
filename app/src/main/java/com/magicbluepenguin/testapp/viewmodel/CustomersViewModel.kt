package com.magicbluepenguin.testapp.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.magicbluepenguin.testapp.customer.Customer
import com.magicbluepenguin.testapp.customerrepository.CustomerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CustomersViewModel @Inject constructor(val customerRepository: CustomerRepository<String>) : ViewModel(), CoroutineScope {

    val customersLiveList = ObservableField<List<Customer>>()
    val isLoading = ObservableBoolean()
    val noResourcesProvided = ObservableBoolean()

    var previousQuery: String? = null

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun fetchAndUpdateResults(fromSource: String? = previousQuery) {
        isLoading.set(true)
        if(fromSource == null){
            noResourcesProvided.set(true)
            isLoading.set(false)
        }else {
            launch(Dispatchers.Default) {
                customersLiveList.set(customerRepository.getCustomers(fromSource))
                isLoading.set(false)
            }
        }
        noResourcesProvided.set(false)
    }

    fun refreshResults(){
        fetchAndUpdateResults()
    }
}