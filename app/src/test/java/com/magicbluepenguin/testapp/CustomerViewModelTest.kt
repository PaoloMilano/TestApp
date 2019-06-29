package com.magicbluepenguin.testapp

import com.magicbluepenguin.testapp.customer.Customer
import com.magicbluepenguin.testapp.customerrepository.CsvCustomerRepository
import com.magicbluepenguin.testapp.customerlistactivity.CustomersViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CustomerViewModelTest {

    @Test
    fun `assert that observable is updated correctly`(){

        val testListOfCustomers = listOf(
            Customer("Pinco", "Pallino", "12-17-1998", 3),
            Customer("Porco", "Spino", "12-13-1980", 4),
            Customer("Mamma", "Maria", "09-11-1980", 7)
        )

        val mockRepository = mockk<CsvCustomerRepository>()
        every { mockRepository.getCustomers() } answers { testListOfCustomers }

        val customersViewModel = CustomersViewModel(mockRepository)
        runBlocking { customersViewModel.fetchAndUpdateResults() }

        assertEquals(customersViewModel.customersLiveList.get(), testListOfCustomers)
    }
}
