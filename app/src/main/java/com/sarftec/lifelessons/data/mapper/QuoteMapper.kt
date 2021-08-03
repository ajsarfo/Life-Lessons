package com.sarftec.lifelessons.data.mapper

import com.sarftec.lifelessons.data.database.entity.Category
import com.sarftec.lifelessons.data.database.entity.Quote

interface QuoteMapper {
    fun mapToQuote(category: Category) : Quote
}