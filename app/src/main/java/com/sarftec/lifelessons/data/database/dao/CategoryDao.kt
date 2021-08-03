package com.sarftec.lifelessons.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sarftec.lifelessons.data.database.CATEGORY_TABLE
import com.sarftec.lifelessons.data.database.entity.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: List<Category>)

    @Query("select * from $CATEGORY_TABLE order by category asc")
    suspend fun categories() : List<Category>
}