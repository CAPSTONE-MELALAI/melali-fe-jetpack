package com.example.melali.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.melali.data.Repository
import com.example.melali.model.request.RegisterRequest
import com.example.melali.model.response.LoginResponse
import com.example.melali.model.response.RegisterResponse
import com.example.melali.model.response.ResponseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun register(
        body: RegisterRequest,
        onSuccess: (ResponseWrapper<RegisterResponse>) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            repository.register(body,onSuccess, onFailed)
        }
    }

    fun saveToken(t:String){
        repository.saveToken(t)
    }

}