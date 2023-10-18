package com.morteza.foodorderapplication.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.morteza.foodorderapplication.database.entity.RecipeEntity
import com.morteza.foodorderapplication.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeAppDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveRecipe(entity: RecipeEntity)

    @Query("SELECT * FROM ${Constants.RECIPE_TABLE_NAME} ORDER BY ID ASC")
    fun loadRecipe():Flow<List<RecipeEntity>>
}