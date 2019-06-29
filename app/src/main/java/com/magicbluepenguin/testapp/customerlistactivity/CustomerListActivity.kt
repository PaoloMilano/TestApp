package com.magicbluepenguin.testapp.customerlistactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import com.google.android.material.snackbar.Snackbar
import com.magicbluepenguin.testapp.R
import com.magicbluepenguin.testapp.base.BaseActivity
import com.magicbluepenguin.testapp.bindings.BindableViewHolder
import com.magicbluepenguin.testapp.bindings.BoundPagedRecyclerViewAdapter
import com.magicbluepenguin.testapp.customer.Customer
import com.magicbluepenguin.testapp.databinding.ActivityCustomerListBinding
import com.magicbluepenguin.testapp.databinding.CustomerListItemBinding
import kotlinx.android.synthetic.main.activity_customer_list.toolbar

class CustomerListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityViewBinding =
            DataBindingUtil.setContentView<ActivityCustomerListBinding>(this, R.layout.activity_customer_list)
        activityViewBinding.itemList.customerListView.boundAdapter = CustomerRecyclerViewAdapter()
        with(getViewModel(CustomersViewModel::class.java)) {
            noResourcesProvided.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    if (noResourcesProvided.get()) {
                        Snackbar.make(
                            activityViewBinding.frameLayout,
                            R.string.no_resources_snackbar,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            })
            activityViewBinding.itemList.customersViewModel = this
            fetchAndUpdateResults("issues.csv")
        }

        setSupportActionBar(toolbar)
        toolbar.title = title
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
