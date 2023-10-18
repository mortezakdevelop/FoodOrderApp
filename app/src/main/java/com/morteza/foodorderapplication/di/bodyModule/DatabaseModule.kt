package com.morteza.foodorderapplication.di.bodyModule

import android.content.Context
import androidx.room.Room
import com.morteza.foodorderapplication.database.RecipeAppDatabase
import com.morteza.foodorderapplication.utils.Constants
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
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, RecipeAppDatabase::class.java, Constants.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideDao(database: RecipeAppDatabase) = database.dao()
}