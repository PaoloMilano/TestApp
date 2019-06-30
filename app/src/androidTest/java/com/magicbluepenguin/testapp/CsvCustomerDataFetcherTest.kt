package com.magicbluepenguin.testapp

import androidx.test.platform.app.InstrumentationRegistry
import com.magicbluepenguin.testapp.customer.Customer
import com.magicbluepenguin.testapp.data.CsvCustomerDataFetcher
import com.magicbluepenguin.testapp.data.ResponseNoValue
import com.magicbluepenguin.testapp.data.ResponseWithValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@Suppress("UNCHECKED_CAST")
class CsvCustomerDataFetcherTest {

    @Test
    fun parseFileNoErrors() {
        // Assert that a file with no errors can be parsed correctly
        val tested = CsvCustomerDataFetcher(InstrumentationRegistry.getInstrumentation().context.assets)
        val response = tested.fetchAndParseCustomers("issues_no_error.csv")
        assertTrue(response is ResponseWithValue)
        val data = (response as ResponseWithValue<List<Customer>>).data
        assertEquals(1, data.size)
        assertEquals(Customer("Theo", "Jansen", "02-Jan-1978", 5), data.get(0))
    }

    @Test
    fun parseFileSomeErrors() {
        // Assert parsing errors are correctly propagated to client
        val tested = CsvCustomerDataFetcher(InstrumentationRegistry.getInstrumentation().context.assets)
        val response = tested.fetchAndParseCustomers("issues_some_error.csv")
        assertTrue(response is ResponseWithValue)
        val data = (response as ResponseWithValue<List<Customer>>).data
        assertEquals(1, data.size)
        assertEquals(Customer("Theo", "Jansen", null, null), data.get(0))
    }

    @Test
    fun parseFileAllErrors() {
        // Assert that when a file can't be accessed the error is correctly propagated to the client
        val tested = CsvCustomerDataFetcher(InstrumentationRegistry.getInstrumentation().context.assets)
        val response = tested.fetchAndParseCustomers("inexistent_file")
        assertTrue(response is ResponseNoValue)
    }
}
