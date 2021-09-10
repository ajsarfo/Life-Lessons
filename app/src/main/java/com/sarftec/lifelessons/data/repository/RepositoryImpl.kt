package com.sarftec.lifelessons.data.repository

import android.content.Context
import com.sarftec.lifelessons.application.file.APP_CREATED
import com.sarftec.lifelessons.application.file.editSettings
import com.sarftec.lifelessons.application.file.readSettings
import com.sarftec.lifelessons.data.CATEGORY_QUOTES_FILE
import com.sarftec.lifelessons.data.database.Database
import com.sarftec.lifelessons.data.json.JsonQuoteList
import kotlinx.coroutines.flow.first
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class RepositoryImpl(
    private val context: Context,
    private val database: Database
) : Repository {

    override fun database(): Database {
        return database
    }

    override suspend fun setupDatabase() {
        context.assets.open(CATEGORY_QUOTES_FILE).use { inputStream ->
            val result: List<JsonQuoteList> = Json.decodeFromString(
                inputStream.buffered().readBytes().decodeToString()
            )
            result.forEach { jsonQuoteList ->
               val category = jsonQuoteList.mapToCategory()
                database.categoryDao().insert(category)
                database.quoteDao().insert(
                    jsonQuoteList.quotes.map { jsonQuote ->
                        jsonQuote.mapToQuote(category)
                    }
                )
            }
        }
        Runtime.getRuntime().gc()
        context.editSettings(APP_CREATED, true)
    }

    override suspend fun isDatabaseCreated(): Boolean {
        return context.readSettings(APP_CREATED, false).first()
    }
}