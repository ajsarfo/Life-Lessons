package com.sarftec.lifelessons.data.repository

import com.sarftec.lifelessons.data.database.Database

interface Repository {

    fun database() : Database
    suspend fun setupDatabase()
    suspend fun isDatabaseCreated() : Boolean
}