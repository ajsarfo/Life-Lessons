package com.sarftec.lifelessons.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sarftec.lifelessons.data.database.CATEGORY_TABLE

@Entity(
    tableName = CATEGORY_TABLE,
    indices = [
        Index(value = ["category"], unique = true)
    ]
)
data class Category(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val category: String,
    val size: Int
)