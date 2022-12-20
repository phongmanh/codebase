package co.kr.gogox.driver.util.adapters

import androidx.databinding.ViewDataBinding

abstract class SimpleRecyclerItem {

    abstract var adapter: BindableRecyclerAdapter
    abstract fun getLayout(): Int
    fun getViewType() = getLayout()
    abstract fun getViewHolderProvider(): (binding: ViewDataBinding) -> BindableViewHolder

}