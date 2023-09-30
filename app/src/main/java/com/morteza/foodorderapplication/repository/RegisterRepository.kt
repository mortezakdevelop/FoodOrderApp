package com.morteza.foodorderapplication.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.morteza.foodorderapplication.models.BodyRegister
import com.morteza.foodorderapplication.models.RegisterStoredModel
import com.morteza.foodorderapplication.network.ApiServices
import com.morteza.foodorderapplication.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class RegisterRepository @Inject constructor(
    @ApplicationContext private val context:Context,
    private val apiService:ApiServices

) {
    //Store user info
    private object StoredKeys {
        val username = stringPreferencesKey(Constants.REGISTER_USERNAME)
        val hash = stringPreferencesKey(Constants.REGISTER_HASH)
    }


    //instance Preferences
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.REGISTER_USER_INFO)

    //write to data store
    suspend fun saveRegisterData(username: String, hash: String) {
        context.dataStore.edit {
            it[StoredKeys.username] = username
            it[StoredKeys.hash] = hash
        }
    }


    //read from data store
    val readRegisterData: Flow<RegisterStoredModel> = context.dataStore.data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val username = it[StoredKeys.username] ?: ""
            val hash = it[StoredKeys.hash] ?: ""
            RegisterStoredModel(username, hash)
        }

    //API
    suspend fun postRegister(apiKey:String, body:BodyRegister) = apiService.postRegister(apiKey,body)
}