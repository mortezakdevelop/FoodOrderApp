package com.morteza.foodorderapplication.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.morteza.foodorderapplication.models.ResponseRecipes
import com.morteza.foodorderapplication.utils.Constants

@Entity(tableName = Constants.RECIPE_TABLE_NAME)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = false)
    val id:Int,
    val response: ResponseRecipes
)
