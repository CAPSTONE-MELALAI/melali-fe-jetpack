package com.example.melali.presentation.list

import androidx.lifecycle.ViewModel
import com.example.melali.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel(){
}