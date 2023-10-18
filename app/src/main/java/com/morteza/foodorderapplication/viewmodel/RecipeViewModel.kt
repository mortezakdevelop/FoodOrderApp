package com.morteza.foodorderapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.morteza.foodorderapplication.database.entity.RecipeEntity
import com.morteza.foodorderapplication.models.ResponseRecipes
import com.morteza.foodorderapplication.repository.RecipeRepository
import com.morteza.foodorderapplication.utils.Constants
import com.morteza.foodorderapplication.utils.NetworkRequest
import com.morteza.foodorderapplication.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    val popularData = MutableLiveData<NetworkRequest<ResponseRecipes>>()

    fun popularQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.API_KEY] = Constants.MY_API_KEY
        queries[Constants.SORT] = Constants.POPULARITY
        queries[Constants.NUMBER] = Constants.LIMITED_COUNT.toString()
        queries[Constants.ADD_RECIPE_INFORMATION] = Constants.TRUE
        return queries
    }

    fun callPopularApi(queries: Map<String, String>): Job {
        return viewModelScope.launch {
            popularData.value = NetworkRequest.Loading()
            val response = recipeRepository.getRecipe(queries)
            popularData.value = NetworkResponse(response).generalNetworkResponse()
        }
    }

    fun savePopularData(entity: RecipeEntity):Job{
        return viewModelScope.launch {
            recipeRepository.saveRecipe(entity)
        }
    }

    val readPopularDataFromDb = recipeRepository.loadRecipe().asLiveData()
}