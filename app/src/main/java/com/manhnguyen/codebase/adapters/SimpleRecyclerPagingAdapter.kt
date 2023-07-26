package com.giaphat.component.adapters

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil

open class SimpleRecyclerPagingAdapter() :
    BindableRecyclerPagingAdapter(ITEM_COMPARATOR) {

    override fun getViewItem(position: Int): Any {
        return getItem(position)!!
    }

    override fun getLayout(position: Int): Int {
        return getItem(position)!!.getLayout()
    }

    override fun getViewHolder(binding: ViewDataBinding, viewType: Int): BindableViewHolder? {
        snapshot().forEach {
            it?.let {
                if (it.getLayout() == viewType) {
                    // Important - Must call getItem() to trigger load next page if needed
                    return getItem(snapshot().indexOf(it))?.getViewHolderProvider()?.invoke(binding)
                }
            }
        }
        return null
    }


    suspend fun submitItemData(pageData: PagingData<SimpleRecyclerPagingItem>) {
        submitData(pageData)
    }

    interface ItemViewClickListener {
        fun onItemViewClick(itemView: SimpleRecyclerPagingItem) {}
        fun <T> onChildAction(item: T) {}
    }


    companion object {
        val ITEM_COMPARATOR = object :
            DiffUtil.ItemCallback<SimpleRecyclerPagingItem>() {
            override fun areItemsTheSame(
                oldItem: SimpleRecyclerPagingItem,
                newItem: SimpleRecyclerPagingItem
            ): Boolean {
                return oldItem.getItemDataId() == newItem.getItemDataId()
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: SimpleRecyclerPagingItem,
                newItem: SimpleRecyclerPagingItem
            ): Boolean {
                return oldItem.getItemData() == newItem.getItemData()
            }

        }
    }


}