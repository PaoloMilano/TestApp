package com.magicbluepenguin.testapp.customerlistactivity

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.magicbluepenguin.testapp.customer.Customer
import com.magicbluepenguin.testapp.customerrepository.CustomerRepository
import com.magicbluepenguin.testapp.data.DataResponse
import com.magicbluepenguin.testapp.data.ResponseErrorAll
import com.magicbluepenguin.testapp.data.ResponseErrorNone
import com.magicbluepenguin.testapp.data.ResponseErrorSome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

enum class DataFetchError { NONE, SOME, ALL }

class CustomersViewModel @Inject constructor(val customerRepository: CustomerRepository<String>) : ViewModel(),
    CoroutineScope {

    val customersLiveList = ObservableField<List<Customer>>()
    val isLoading = ObservableBoolean()

    // These 2 error values will only temporarily be set to error when an error occurs it is expected
    // that clients will observe value changes and react to these events approrpiately
    val dataFetchError = ObservableField<DataFetchError>()
    val noResourcesProvided = ObservableBoolean()

    var previousResource: String? = null

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    init {
        dataFetchError.set(DataFetchError.NONE)
    }

    fun fetchAndUpdateResults(fromSource: String? = previousResource) = launch(Dispatchers.Default) {
        isLoading.set(true)
        if (fromSource == null) {
            noResourcesProvided.set(true)
            isLoading.set(false)
        } else {
            previousResource = fromSource
            customerRepository.getCustomersResponse(fromSource).also {
                customersLiveList.set(handleCustomersResponseAndGetData(it))
            }
            isLoading.set(false)
        }
        noResourcesProvided.set(false)
    }

    private fun handleCustomersResponseAndGetData(dataResponse: DataResponse<List<Customer>>): List<Customer> {
        return when (dataResponse) {
            is ResponseErrorNone -> dataResponse.data
            is ResponseErrorSome -> {
                dataFetchError.set(DataFetchError.SOME)
                dataFetchError.set(DataFetchError.NONE)
                dataResponse.data
            }
            is ResponseErrorAll -> {
                dataFetchError.set(DataFetchError.ALL)
                dataFetchError.set(DataFetchError.NONE)
                emptyList()
            }
        }
    }

    fun refreshResults() = launch(Dispatchers.Default) {
        fetchAndUpdateResults()
    }
}