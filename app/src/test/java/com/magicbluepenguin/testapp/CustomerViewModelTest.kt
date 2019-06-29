package com.magicbluepenguin.testapp

import androidx.databinding.Observable
import com.magicbluepenguin.testapp.customer.Customer
import com.magicbluepenguin.testapp.customerlistactivity.CustomersViewModel
import com.magicbluepenguin.testapp.customerrepository.CsvCustomerRepository
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
        every { mockRepository.getCustomers(any()) } answers { testListOfCustomers }

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
        every { mockRepository.getCustomers(any()) } answers { emptyList() }

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
        every { mockRepository.getCustomers(any()) } answers { emptyList() }

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
        every { mockRepository.getCustomers(any()) } answers { emptyList() }

        val customersViewModel = CustomersViewModel(mockRepository)

        runBlocking {
            customersViewModel.fetchAndUpdateResults(resourceName).join()
            customersViewModel.refreshResults().join()
        }
        verify(exactly = 2) {
            mockRepository.getCustomers(resourceName)
        }
    }
}
