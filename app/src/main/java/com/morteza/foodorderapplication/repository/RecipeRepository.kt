package com.morteza.foodorderapplication.repository

import com.morteza.foodorderapplication.database.RecipeAppDao
import com.morteza.foodorderapplication.database.entity.RecipeEntity
import com.morteza.foodorderapplication.models.ResponseRecipes
import com.morteza.foodorderapplication.network.ApiServices
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val apiServices: ApiServices,
    private val dao:RecipeAppDao
) {

    suspend fun getRecipe(query: Map<String, String>): Response<ResponseRecipes> {
        return apiServices.getRecipes(query)
    }

    suspend fun saveRecipe(entity:RecipeEntity){
        return dao.saveRecipe(entity)
    }

    fun loadRecipe():Flow<List<RecipeEntity>>{
        return dao.loadRecipe()
    }

}