package com.magicbluepenguin.testapp.customerlistactivity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.magicbluepenguin.testapp.R
import com.magicbluepenguin.testapp.data.DataFetchError
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test

@LargeTest
class CustomerListActivityTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(CustomerListActivity::class.java)

    val customersViewModel by lazy { activityTestRule.activity.getViewModel(CustomersViewModel::class.java) }

    @Test
    fun resourceErrorTest() {
        // Tests that no resource error is displayed correctly
        customersViewModel.noResourcesProvided.set(true)
        customersViewModel.noResourcesProvided.set(false)
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.no_resources_snackbar)))
    }

    @Test
    fun noResourceErrorTest() {
        // Tests that when the correct resources are passed we do not display an error
        customersViewModel.noResourcesProvided.set(false)
        onView(
            allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(R.string.no_resources_snackbar)
            )
        ).check(doesNotExist())
    }

    @Test
    fun someErrorDataErrorTest() {
        // Tests that data corruption error is displayed properly
        customersViewModel.dataFetchError.set(DataFetchError.SOME)
        customersViewModel.dataFetchError.set(DataFetchError.NONE)
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.records_corrupted)))
    }

    @Test
    fun allErrorDataErrorTest() {
        // Tests that data failure error is displayed properly
        customersViewModel.dataFetchError.set(DataFetchError.ALL)
        customersViewModel.dataFetchError.set(DataFetchError.NONE)
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.no_records_found)))
    }

    @Test
    fun noErrorDataErrorTest() {
        // Tests that correct retrieval doe not show any error
        customersViewModel.dataFetchError.set(DataFetchError.NONE)
        onView(
            allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(R.string.no_records_found)
            )
        ).check(doesNotExist())

        onView(
            allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(R.string.records_corrupted)
            )
        ).check(doesNotExist())
    }
}
