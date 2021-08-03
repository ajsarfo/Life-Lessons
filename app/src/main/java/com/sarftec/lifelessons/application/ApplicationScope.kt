package com.sarftec.lifelessons.application

import com.sarftec.lifelessons.data.database.entity.Quote
import com.sarftec.lifelessons.data.repository.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationScope @Inject constructor(
    private val repository: Repository
) {

    private val quotes = mutableSetOf<Quote>()

    fun saveQuote(quote: Quote) {
        quotes.add(quote)
    }

    suspend fun execute() {
        repository.database().quoteDao().update(quotes.toList())
        quotes.clear()
    }
}