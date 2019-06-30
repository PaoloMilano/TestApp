package com.magicbluepenguin.testapp.data

import android.content.res.AssetManager
import com.magicbluepenguin.testapp.customer.Customer
import com.opencsv.CSVReaderHeaderAware
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.Reader
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvCustomerDataFetcher @Inject constructor(val assetManager: AssetManager) : CustomerDataFetcher<String> {

    private val FIST_NAME_KEY = "First name"
    private val SUR_NAME_KEY = "Sur name"
    private val ISSUE_COUNT_KEY = "Issue count"
    private val DATE_OF_BIRTH_KEY = "Date of birth"

    private val dateParseFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    private val stringParseFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

    @Suppress("UNCHECKED_CAST")
    override fun fetchAndParseCustomers(fromSource: String?): DataResponse<List<Customer>> {
        if (fromSource == null) {
            return ResponseWithValue(emptyList(), DataFetchError.NONE)
        }

        return try {

            val stream = assetManager.open(fromSource)

            with(CSVReaderHeaderAware(InputStreamReader(stream) as Reader?)) {
                val customerDataList = mutableListOf<Customer>()
                var customerData: Map<String, String>? = readMap()
                var error = DataFetchError.NONE
                while (customerData != null) {
                    (parseCustomer(customerData) as? ResponseWithValue<Customer>)?.let {
                        if (it.error != DataFetchError.NONE) {
                            error = DataFetchError.SOME
                        }

                        customerDataList.add(it.data)
                    }
                    customerData = readMap()
                }
                return ResponseWithValue(customerDataList, error)
            }
        } catch (e: FileNotFoundException) {
            ResponseNoValue()
        }
    }

    private fun parseCustomer(customerData: Map<String, String>): DataResponse<Customer> {

        var error = DataFetchError.NONE
        val date = try {
            stringParseFormat.format(dateParseFormat.parse(customerData[DATE_OF_BIRTH_KEY]))
        } catch (e: ParseException) {
            error = DataFetchError.SOME
            null
        }

        val issueCount = try {
            customerData[ISSUE_COUNT_KEY]?.toInt()
        } catch (e: NumberFormatException) {
            error = DataFetchError.SOME
            null
        }

        return ResponseWithValue(
            Customer(
                customerData[FIST_NAME_KEY],
                customerData[SUR_NAME_KEY],
                date,
                issueCount
            ), error
        )
    }
}