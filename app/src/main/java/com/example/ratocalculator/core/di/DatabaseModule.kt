package com.example.ratocalculator.core.di

import android.content.Context
import androidx.room.Room
import com.example.ratocalculator.core.database.AppDatabase
import com.example.ratocalculator.core.database.HistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "rato_calculator_db"
        ).build()
    }

    @Provides
    fun provideHistoryDao(database: AppDatabase): HistoryDao{
        return database.historyDao()
    }

}