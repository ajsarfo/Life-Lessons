package com.sarftec.lifelessons.data.json

import com.sarftec.lifelessons.data.database.entity.Category
import com.sarftec.lifelessons.data.mapper.CategoryMapper
import kotlinx.serialization.Serializable

@Serializable
class JsonQuoteList(
    val title: String,
    val quotes: List<JsonQuote>
) : CategoryMapper {

    override fun mapToCategory(): Category {
        return Category(category = title, size = quotes.size)
    }
}