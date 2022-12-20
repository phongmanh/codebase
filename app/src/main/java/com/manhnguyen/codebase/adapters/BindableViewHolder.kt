package co.kr.gogox.driver.util.adapters

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BindableViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item: Any)
}