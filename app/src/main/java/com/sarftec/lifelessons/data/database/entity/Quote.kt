package com.sarftec.lifelessons.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.sarftec.lifelessons.data.database.QUOTE_TABLE

@Entity(
    tableName = QUOTE_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["category"],
            childColumns = ["category"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class Quote (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(index = true)
    val category: String,
    val message: String,
    var favorite: Boolean = false
)