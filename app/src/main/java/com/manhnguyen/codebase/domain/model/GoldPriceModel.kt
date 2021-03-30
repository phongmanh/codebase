package com.manhnguyen.codebase.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class GoldPriceModel constructor(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("date")
    @PrimaryKey val date: String) {
}