package com.magicbluepenguin.testapp.customerlistactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.google.android.material.snackbar.Snackbar
import com.magicbluepenguin.testapp.R
import com.magicbluepenguin.testapp.base.BaseActivity
import com.magicbluepenguin.testapp.bindings.BindableViewHolder
import com.magicbluepenguin.testapp.bindings.BoundPagedRecyclerViewAdapter
import com.magicbluepenguin.testapp.customer.Customer
import com.magicbluepenguin.testapp.data.DataFetchError
import com.magicbluepenguin.testapp.databinding.ActivityCustomerListBinding
import com.magicbluepenguin.testapp.databinding.CustomerListItemBinding
import kotlinx.android.synthetic.main.activity_customer_list.toolbar

class CustomerListActivity : BaseActivity() {

    val activityViewBinding by lazy {
        DataBindingUtil.setContentView<ActivityCustomerListBinding>(this, R.layout.activity_customer_list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityViewBinding.itemList.customerListView.boundAdapter = CustomerRecyclerViewAdapter()
        with(getViewModel(CustomersViewModel::class.java)) {

            observeNoResourceError(noResourcesProvided)
            observeDataFetchingError(dataFetchError)

            activityViewBinding.itemList.customersViewModel = this
            fetchAndUpdateResults("issues.csv")
        }

        setSupportActionBar(toolbar)
        toolbar.title = title
    }

    private fun observeNoResourceError(observableBoolean: ObservableBoolean) {
        observableBoolean.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (observableBoolean.get()) {
                    showSnackbar(R.string.no_resources_snackbar)
                }
            }
        })
    }

    private fun observeDataFetchingError(dataFetchingErrorField: ObservableField<DataFetchError>) {
        dataFetchingErrorField.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val dataFetchingError = dataFetchingErrorField.get()
                if (dataFetchingError == DataFetchError.SOME) {
                    showSnackbar(R.string.records_corrupted)
                } else if (dataFetchingError == DataFetchError.ALL) {
                    showSnackbar(R.string.no_records_found)
                }
            }
        })
    }

    private fun showSnackbar(@StringRes stringId: Int) {
        Snackbar.make(
            activityViewBinding.frameLayout,
            stringId,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    class CustomerRecyclerViewAdapter :
        BoundPagedRecyclerViewAdapter<Customer, CustomerItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerItemViewHolder {
            return CustomerItemViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.customer_list_item,
                    parent,
                    false
                )
            )
        }
    }

    class CustomerItemViewHolder(binder: CustomerListItemBinding) :
        BindableViewHolder<Customer, CustomerListItemBinding>(binder) {

        override fun bind(viewBinding: CustomerListItemBinding, item: Customer) {
            viewBinding.customer = item
        }
    }
}
