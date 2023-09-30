package com.morteza.foodorderapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morteza.foodorderapplication.models.BodyRegister
import com.morteza.foodorderapplication.models.ResponseRegister
import com.morteza.foodorderapplication.repository.RegisterRepository
import com.morteza.foodorderapplication.utils.NetworkRequest
import com.morteza.foodorderapplication.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: RegisterRepository):ViewModel() {

    val registerData = MutableLiveData<NetworkRequest<ResponseRegister>>()

    fun callRegisterApi(apiKey: String, body: BodyRegister) = viewModelScope.launch {
        registerData.value = NetworkRequest.Loading()
        val response = repository.postRegister(apiKey, body)
        registerData.value = NetworkResponse(response).generalNetworkResponse()
    }

    //Stored data
    fun saveData(username: String, hash: String) = viewModelScope.launch {
        repository.saveRegisterData(username, hash)
    }

    val readData = repository.readRegisterData


}