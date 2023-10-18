package com.morteza.foodorderapplication.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.morteza.foodorderapplication.models.ResponseRecipes

class RecipeAppTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun recipeToJson(recipe: ResponseRecipes): String {
        return gson.toJson(recipe)
    }

    @TypeConverter
    fun stringToRecipe(data: String): ResponseRecipes {
        return gson.fromJson(data, ResponseRecipes::class.java)
    }

}