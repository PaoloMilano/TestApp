package com.magicbluepenguin.testapp.data

/**
 * This enum handles basic error response in a way that can be easily interpreted for the end client.
 * It includes 3 scenarios:
 *
 * [ALL] An error was encountered that prevented any data from being fetched
 * [SOME] The data was fetched but some of it may have been corrupted
 * [NONE] No error, all data was fetched successfully
 *
 */
enum class DataFetchError { NONE, SOME, ALL }

sealed class DataResponse<T>(val error: DataFetchError)

class ResponseNoValue<T>() : DataResponse<T>(DataFetchError.ALL)

class ResponseWithValue<T>(val data: T, error: DataFetchError) : DataResponse<T>(error) {
    init {
        if (error == DataFetchError.ALL) {
            throw IllegalArgumentException("Cannot return a value from a response with an error of type ALL")
        }
    }
}
