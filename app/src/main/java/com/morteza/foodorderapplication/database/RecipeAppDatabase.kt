package com.morteza.foodorderapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.morteza.foodorderapplication.database.entity.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1)
@TypeConverters(RecipeAppTypeConverter::class)
abstract class RecipeAppDatabase:RoomDatabase() {
    abstract fun dao():RecipeAppDao
}