package com.example.melali.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melali.data.Repository
import com.example.melali.model.response.ResponseWrapper
import com.example.melali.model.response.SingleDestinationCCResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel(){



    fun getDestinationByIndex(
        index:Long,
        onSuccess:(ResponseWrapper<SingleDestinationCCResponse>) -> Unit,
        onFailed: (Exception) -> Unit
    ){
        viewModelScope.launch {
            repository.getDestinationByIndex(index,onSuccess,onFailed)
        }
    }
}