package com.magicbluepenguin.testapp

import androidx.databinding.Observable
import com.magicbluepenguin.testapp.customer.Customer
import com.magicbluepenguin.testapp.customerlistactivity.CustomersViewModel
import com.magicbluepenguin.testapp.customerlistactivity.DataFetchError
import com.magicbluepenguin.testapp.customerrepository.CsvCustomerRepository
import com.magicbluepenguin.testapp.data.ResponseErrorAll
import com.magicbluepenguin.testapp.data.ResponseErrorNone
import com.magicbluepenguin.testapp.data.ResponseErrorSome
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CustomerViewModelTest {

    @Test
    fun `assert that customer list observable is updated correctly`() {
        val testListOfCustomers = listOf(
            Customer("Pinco", "Pallino", "12-17-1998", 3),
            Customer("Porco", "Spino", "12-13-1980", 4),
            Customer("Mamma", "Maria", "09-11-1980", 7)
        )

        val mockRepository = mockk<CsvCustomerRepository>()
        every { mockRepository.getCustomersResponse(any()) } answers { ResponseErrorNone(testListOfCustomers) }

        val customersViewModel = CustomersViewModel(mockRepository)

        runBlocking {
            // Pass an empty string as a fake resource name
            customersViewModel.fetchAndUpdateResults("").join()
        }
        assertEquals(testListOfCustomers, customersViewModel.customersLiveList.get())
    }

    @Test
    fun `assert that no-resource observable is updated correctly when a resource is provided`() {
        val mockRepository = mockk<CsvCustomerRepository>()
        every { mockRepository.getCustomersResponse(any()) } answers { ResponseErrorNone(emptyList()) }

        val customersViewModel = CustomersViewModel(mockRepository)
        // We expect this to be set to false, so we need to start from true in order to observe the call
        customersViewModel.noResourcesProvided.set(true)

        val actualCalls = mutableListOf<Boolean>()
        customersViewModel.noResourcesProvided.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                actualCalls.add(customersViewModel.noResourcesProvided.get())
            }
        })
        runBlocking {
            // Pass an empty string as a fake resource name
            customersViewModel.fetchAndUpdateResults("").join()
        }
        assertEquals(listOf(false), actualCalls)
    }

    @Test
    fun `assert that no-resource observable is updated correctly when no resource is provided`() {
        val customersViewModel = CustomersViewModel(mockk<CsvCustomerRepository>())
        val actualCalls = mutableListOf<Boolean>()
        customersViewModel.noResourcesProvided.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                actualCalls.add(customersViewModel.noResourcesProvided.get())
            }
        })
        runBlocking {
            customersViewModel.fetchAndUpdateResults().join()
        }
        assertEquals(listOf(true, false), actualCalls)
    }

    @Test
    fun `assert that loading observable is updated correctly when a resource is provided`() {

        val mockRepository = mockk<CsvCustomerRepository>()
        every { mockRepository.getCustomersResponse(any()) } answers { ResponseErrorNone(emptyList()) }

        val customersViewModel = CustomersViewModel(mockRepository)

        val actualCalls = mutableListOf<Boolean>()
        customersViewModel.isLoading.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                actualCalls.add(customersViewModel.isLoading.get())
            }
        })
        runBlocking {
            // Pass an empty string as a fake resource name
            customersViewModel.fetchAndUpdateResults("").join()
        }
        assertEquals(listOf(true, false), actualCalls)
    }

    @Test
    fun `assert that loading observable is updated correctly when no resource is provided`() {
        val customersViewModel = CustomersViewModel(mockk<CsvCustomerRepository>())
        val actualCalls = mutableListOf<Boolean>()
        customersViewModel.isLoading.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                actualCalls.add(customersViewModel.isLoading.get())
            }
        })
        runBlocking {
            customersViewModel.fetchAndUpdateResults().join()
        }
        assertEquals(listOf(true, false), actualCalls)
    }

    @Test
    fun `assert that the last rsource name is cached`() {

        val resourceName = "fake_res_name"
        val mockRepository = mockk<CsvCustomerRepository>()
        every { mockRepository.getCustomersResponse(any()) } answers { ResponseErrorNone(emptyList()) }

        val customersViewModel = CustomersViewModel(mockRepository)

        runBlocking {
            customersViewModel.fetchAndUpdateResults(resourceName).join()
            customersViewModel.refreshResults().join()
        }
        verify(exactly = 2) {
            mockRepository.getCustomersResponse(resourceName)
        }
    }

    @Test
    fun `assert that error level is initialised to NONE`() {
        val customersViewModel = CustomersViewModel(mockk())
        assertEquals(DataFetchError.NONE, customersViewModel.dataFetchError.get())
    }

    @Test
    fun `assert that error level SOME is set to correctly`() {
        val mockRepository = mockk<CsvCustomerRepository>()
        every { mockRepository.getCustomersResponse(any()) } answers { ResponseErrorSome(emptyList()) }

        val customersViewModel = CustomersViewModel(mockRepository)
        val actualCalls = mutableListOf<DataFetchError?>()
        customersViewModel.dataFetchError.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                actualCalls.add(customersViewModel.dataFetchError.get())
            }
        })

        runBlocking {
            // Pass an empty string as a fake resource name
            customersViewModel.fetchAndUpdateResults("").join()
        }
        assertEquals(listOf(DataFetchError.SOME, DataFetchError.NONE), actualCalls)
    }

    @Test
    fun `assert that error level ALL is set to correctly`() {
        val mockRepository = mockk<CsvCustomerRepository>()
        every { mockRepository.getCustomersResponse(any()) } answers { ResponseErrorAll() }

        val customersViewModel = CustomersViewModel(mockRepository)
        val actualCalls = mutableListOf<DataFetchError?>()
        customersViewModel.dataFetchError.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                actualCalls.add(customersViewModel.dataFetchError.get())
            }
        })

        runBlocking {
            // Pass an empty string as a fake resource name
            customersViewModel.fetchAndUpdateResults("").join()
        }
        assertEquals(listOf(DataFetchError.ALL, DataFetchError.NONE), actualCalls)
    }
}
