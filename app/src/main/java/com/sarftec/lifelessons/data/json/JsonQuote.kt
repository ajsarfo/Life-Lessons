package com.sarftec.lifelessons.data.json

import com.sarftec.lifelessons.data.database.entity.Category
import com.sarftec.lifelessons.data.database.entity.Quote
import com.sarftec.lifelessons.data.mapper.QuoteMapper
import kotlinx.serialization.Serializable

@Serializable
class JsonQuote(
    val name: String,
    val message: String
) : QuoteMapper {

    override fun mapToQuote(category: Category): Quote {
        return Quote(
            category = category.category,
            message = message
        )
    }
}