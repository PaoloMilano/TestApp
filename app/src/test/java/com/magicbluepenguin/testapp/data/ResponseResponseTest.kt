package com.magicbluepenguin.testapp.data

import org.junit.Test

class ResponseResponseTest {

    @Test(expected = IllegalArgumentException::class)
    fun `assert that ALL error cannot be set on response with value`() {
        ResponseWithValue(emptyList<Any>(), DataFetchError.ALL)
    }
}