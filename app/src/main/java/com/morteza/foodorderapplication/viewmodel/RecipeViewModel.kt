package com.morteza.foodorderapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.morteza.foodorderapplication.database.entity.RecipeEntity
import com.morteza.foodorderapplication.models.ResponseRecipes
import com.morteza.foodorderapplication.repository.MenuRepository
import com.morteza.foodorderapplication.repository.RecipeRepository
import com.morteza.foodorderapplication.utils.Constants
import com.morteza.foodorderapplication.utils.NetworkRequest
import com.morteza.foodorderapplication.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val menuRepository: MenuRepository
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
            //catch
            val cache = popularData.value?.data
            if (cache != null)
                offlinePopular(cache)
        }
    }

    private fun savePopularData(entity: RecipeEntity):Job{
        return viewModelScope.launch {
            recipeRepository.saveRecipe(entity)
        }
    }

    val readPopularDataFromDb = recipeRepository.loadRecipe().asLiveData()

    private fun offlinePopular(response: ResponseRecipes) {
        val entity = RecipeEntity(0, response)
        savePopularData(entity)
    }

    //recent
    private var mealType = Constants.MAIN_COURSE
    private var dietType = Constants.GLUTEN_FREE
    fun recentQueries(): HashMap<String, String> {
        viewModelScope.launch {
            menuRepository.readMenuData.collect {
                mealType = it.meal
                dietType = it.diet
            }
        }
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.API_KEY] = Constants.MY_API_KEY
        queries[Constants.TYPE] = mealType
        queries[Constants.DIET] = dietType
        queries[Constants.NUMBER] = Constants.FULL_COUNT.toString()
        queries[Constants.ADD_RECIPE_INFORMATION] = Constants.TRUE
        return queries
    }

    val recentData = MutableLiveData<NetworkRequest<ResponseRecipes>>()
    fun callRecentApi(queries: Map<String, String>) = viewModelScope.launch {
        recentData.value = NetworkRequest.Loading()
        val response = recipeRepository.getRecipe(queries)
        recentData.value = NetworkResponse(response).generalNetworkResponse()
        //Cache
        val cache = recentData.value?.data
        if (cache != null)
            offlineRecent(cache)

    }
    private fun saveRecent(entity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        recipeRepository.saveRecipe(entity)
    }

    val readRecentFromDb = recipeRepository.loadRecipe().asLiveData()

    private fun offlineRecent(response: ResponseRecipes) {
        val entity = RecipeEntity(1, response)
        saveRecent(entity)
    }


}