package com.sarftec.lifelessons.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sarftec.lifelessons.data.database.dao.CategoryDao
import com.sarftec.lifelessons.data.database.dao.QuoteDao
import com.sarftec.lifelessons.data.database.entity.Category
import com.sarftec.lifelessons.data.database.entity.Quote

@Database(entities = [Quote::class, Category::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun quoteDao() : QuoteDao
    abstract fun categoryDao() : CategoryDao
}