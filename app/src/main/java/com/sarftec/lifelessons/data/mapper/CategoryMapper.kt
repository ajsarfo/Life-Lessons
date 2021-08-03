package com.sarftec.lifelessons.data.mapper

import com.sarftec.lifelessons.data.database.entity.Category

interface CategoryMapper {
    fun mapToCategory() : Category
}