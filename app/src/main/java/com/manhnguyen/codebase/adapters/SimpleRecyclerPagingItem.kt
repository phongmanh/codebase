package co.kr.gogox.driver.util.adapters

import androidx.databinding.ViewDataBinding

abstract class SimpleRecyclerPagingItem {
    abstract var adapter: BindableRecyclerPagingAdapter
    abstract fun getItemData(): Any
    abstract fun getItemDataId(): Any
    abstract fun getLayout(): Int
    fun getViewType() = getLayout()
    abstract fun getViewHolderProvider(): (binding: ViewDataBinding) -> BindableViewHolder

}