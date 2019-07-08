package com.magicbluepenguin.testapp.customerlistactivity

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicbluepenguin.testapp.customerrepository.CustomerRepository
import com.magicbluepenguin.testapp.data.DataFetchError
import com.magicbluepenguin.testapp.data.DataResponse
import com.magicbluepenguin.testapp.data.ResponseNoValue
import com.magicbluepenguin.testapp.data.ResponseWithValue
import com.magicbluepenguin.testapp.data.customer.Customer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class CustomersViewModel @Inject constructor(val customerRepository: CustomerRepository<String>) : ViewModel() {

    val customersLiveList = ObservableField<List<Customer>>()
    val isLoading = ObservableBoolean()

    // These 2 error values will only temporarily be set to error when an error occurs it is expected
    // that clients will observe value changes and react to these events approrpiately
    val dataFetchError = ObservableField<DataFetchError>()
    val noResourcesProvided = ObservableBoolean()

    private var currentResource: String? = null
    private var currentFetchingJob: Job? = null

    init {
        dataFetchError.set(DataFetchError.NONE)
    }

    fun fetchAndUpdateResults(fromSource: String? = currentResource): Job? {
        currentFetchingJob?.cancel()
        currentFetchingJob = viewModelScope.launch(Dispatchers.Default) {
            isLoading.set(true)
            if (fromSource == null) {
                noResourcesProvided.set(true)
                isLoading.set(false)
            } else {
                currentResource = fromSource
                customerRepository.getCustomersResponse(fromSource).also {
                    customersLiveList.set(handleCustomersResponseAndGetData(it))
                }
                isLoading.set(false)
            }
            noResourcesProvided.set(false)
        }
        return currentFetchingJob
    }

    private fun handleCustomersResponseAndGetData(dataResponse: DataResponse<List<Customer>>): List<Customer> {
        dataFetchError.set(dataResponse.error)
        dataFetchError.set(DataFetchError.NONE)
        return when (dataResponse) {
            is ResponseWithValue -> {
                dataResponse.data
            }
            is ResponseNoValue -> {
                emptyList()
            }
        }
    }

    fun refreshResults() = fetchAndUpdateResults()
}