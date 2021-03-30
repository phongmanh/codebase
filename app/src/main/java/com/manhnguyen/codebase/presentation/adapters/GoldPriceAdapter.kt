package com.manhnguyen.codebase.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.domain.model.GoldPriceModel
import com.manhnguyen.codebase.util.DateTimeUtils.Companion.getDateMonthName
import kotlinx.android.synthetic.main.gold_price_item_layout.view.*

class GoldPriceAdapter constructor(
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data: ArrayList<GoldPriceModel> = ArrayList()

    fun bindData(items: ArrayList<GoldPriceModel>) {
        data = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.gold_price_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        val holderView = holder as ViewHolder
        holderView.dateTextView.text = getDateMonthName(item.date)
        holderView.amountTextView.text = "$${item.amount}"
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
    }

}