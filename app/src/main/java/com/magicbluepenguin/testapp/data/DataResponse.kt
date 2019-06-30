package com.magicbluepenguin.testapp.data

/**
 * This class handles basic error response in a way that can be easily interpreted for the end user.
 * It includes 3 scenarios:
 *
 * [ResponseErrorAll] An error was encountered that prevented any data from being fetched
 * [ResponseErrorSome] The data was fetched but some of it may have been corrupted
 * [ResponseErrorNone] No error, all data was fetched successfully
 *
 * We can further make the distinction between classes that return a value (some, none) and those that don't (all)
 * by checking if they implement [ResponseWithValue]
 */
sealed class DataResponse<T>

class ResponseErrorAll<T> : DataResponse<T>()

interface ResponseWithValue<T> {
    val data: T
}

class ResponseErrorSome<T>(override val data: T) : ResponseWithValue<T>, DataResponse<T>()

class ResponseErrorNone<T>(override val data: T) : ResponseWithValue<T>, DataResponse<T>()
