package com.sarftec.lifelessons.data.injection

import android.content.Context
import androidx.room.Room
import com.sarftec.lifelessons.data.database.Database
import com.sarftec.lifelessons.data.repository.Repository
import com.sarftec.lifelessons.data.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ObjectModule {

    @Singleton
    @Provides
    fun repository(@ApplicationContext context: Context): Repository {
        return RepositoryImpl(
            context,
            Room.databaseBuilder(
                context,
                Database::class.java, "app_database"
            ).fallbackToDestructiveMigration().build()
        )
    }
}