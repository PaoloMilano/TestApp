package com.magicbluepenguin.testapp.data

import android.annotation.SuppressLint
import android.content.res.AssetManager
import com.magicbluepenguin.testapp.customer.Customer
import com.opencsv.CSVReaderHeaderAware
import java.io.InputStreamReader
import java.io.Reader
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvCustomerDataFetcher @Inject constructor(val assetManager: AssetManager) : CustomerDataFetcher<String> {

    private val FIST_NAME_KEY = "First name"
    private val SUR_NAME_KEY = "Sur name"
    private val ISSUE_COUNT_KEY = "Issue count"
    private val DATE_OF_BIRTH_KEY = "Date of birth"

    @SuppressLint("SimpleDateFormat")
    private val dateParseFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    @SuppressLint("SimpleDateFormat")
    private val stringParseFormat = SimpleDateFormat("dd-MMM-yyyy")

    override fun fetchAndParseCustomers(fromSource: String?): List<Customer> {
        if (fromSource == null) {
            return emptyList()
        }

        val stream = assetManager.open(fromSource)

        with(CSVReaderHeaderAware(InputStreamReader(stream) as Reader?)) {
            val customerDataList = mutableListOf<Customer>()
            var customerData: Map<String, String>? = readMap()
            while (customerData != null) {
                customerDataList.add(
                    Customer(
                        customerData[FIST_NAME_KEY],
                        customerData[SUR_NAME_KEY],
                        stringParseFormat.format(dateParseFormat.parse(customerData[DATE_OF_BIRTH_KEY])),
                        customerData[ISSUE_COUNT_KEY]?.toInt()
                    )
                )
                customerData = readMap()
            }
            return customerDataList
        }
    }
}