package com.example.melali.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melali.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {
    fun afterDelay(
        block:() -> Unit
    ){
        viewModelScope.launch {
            delay(2500)
            block()
        }
    }
}