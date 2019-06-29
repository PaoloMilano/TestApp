package com.magicbluepenguin.testapp.bindings

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Generic RecyclerView that uses to enable reuse with data bindings
 */
class BoundRecyclerView<T>(context:Context, attrs: AttributeSet, defStyle: Int) : RecyclerView(context, attrs) {

    var boundAdapter: BoundPagedRecyclerViewAdapter<T, *>?
        set(value) {
            value?.let {
                adapter = it
            }
        }

        get() {
            if (adapter is BoundPagedRecyclerViewAdapter<*, *>){
                return adapter as? BoundPagedRecyclerViewAdapter<T, *>?
            }
            return null
        }
    constructor(context:Context, attrs: AttributeSet) : this(context, attrs, 0)
}


/**
 * Custom Adapter that uses generics to enable reuse with data bindings. This adapter is low on functionality, limiting
 * itself to providing fields for data to be updated while letting subclasses decide how to answer update events
 */
abstract class BoundPagedRecyclerViewAdapter<T, I : BindableViewHolder<T, out ViewDataBinding>> :
    RecyclerView.Adapter<I>() {

    val itemList = ArrayList<T>()

    fun getItem(position: Int): T? {
        return if (position < itemList.size) {
            itemList.get(position)
        } else {
            null
        }
    }

    fun setItems(items: List<T>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: I, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}

abstract class BindableViewHolder<T, I : ViewDataBinding>(private val viewBinding: I) :
    RecyclerView.ViewHolder(viewBinding.root) {

    internal fun bind(item: T) {
        bind(viewBinding, item)
    }

    abstract fun bind(viewBinding: I, item: T)
}

