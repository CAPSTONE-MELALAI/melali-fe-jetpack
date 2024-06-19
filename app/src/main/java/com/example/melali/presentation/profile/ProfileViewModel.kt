package com.example.melali.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melali.data.Repository
import com.example.melali.model.response.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {
    private val _user = MutableStateFlow<UserResponse?>(null)
    val user: StateFlow<UserResponse?> get() = _user
    fun getUserFromLocal(): StateFlow<UserResponse?> {
        viewModelScope.launch {
            _user.value = repository.getUserFromLocal()
        }
        return user
    }
}