package com.sarftec.lifelessons.data.database.dao

import androidx.room.*
import com.sarftec.lifelessons.data.database.QUOTE_TABLE
import com.sarftec.lifelessons.data.database.entity.Quote

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quote: Quote)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quotes: List<Quote>)

    @Query("select * from $QUOTE_TABLE where category = :category")
    suspend fun quotes(category: String) : List<Quote>

    @Query("select * from $QUOTE_TABLE where category = :category order by random() limit 1")
    suspend fun random(category: String) : Quote

    @Query("select * from $QUOTE_TABLE where favorite = 1")
    suspend fun favorites() : List<Quote>

    @Update
    suspend fun update(quote: Quote)

    @Update
    suspend fun update(quotes: List<Quote>)
}