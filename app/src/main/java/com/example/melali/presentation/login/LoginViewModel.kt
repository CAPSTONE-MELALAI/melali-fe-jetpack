package com.example.melali.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melali.data.Repository
import com.example.melali.model.request.LoginRequest
import com.example.melali.model.response.LoginResponse
import com.example.melali.model.response.ResponseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    fun login(
        body: LoginRequest,
        onSuccess: (ResponseWrapper<LoginResponse>) -> Unit,
        onFailed: (Exception) -> Unit
    ){ viewModelScope.launch {
            repository.login(body,onSuccess,onFailed)
        }
    }

    fun saveToken(token: String){
        repository.saveToken(token)
    }

}