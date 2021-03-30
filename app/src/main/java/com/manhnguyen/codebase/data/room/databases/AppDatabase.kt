package com.manhnguyen.codebase.data.room.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.manhnguyen.codebase.domain.model.GoldPriceModel


@Database(
    entities = [GoldPriceModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {


}