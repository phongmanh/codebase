package com.giaphat.component.adapters

import androidx.databinding.ViewDataBinding
import com.giaphat.component.adapters.BindableRecyclerPagingAdapter
import com.giaphat.component.adapters.BindableViewHolder

abstract class SimpleRecyclerPagingItem {
    abstract var adapter: BindableRecyclerPagingAdapter
    abstract fun getItemData(): Any
    abstract fun getItemDataId(): Any
    abstract fun getLayout(): Int
    fun getViewType() = getLayout()
    abstract fun getViewHolderProvider(): (binding: ViewDataBinding) -> BindableViewHolder

}