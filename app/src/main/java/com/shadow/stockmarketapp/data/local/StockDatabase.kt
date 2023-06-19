package com.shadow.stockmarketapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shadow.stockmarketapp.data.local.entity.CompanyListingEntity

@Database(
    entities = [CompanyListingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StockDatabase : RoomDatabase() {
    abstract val dao: StockDao
}