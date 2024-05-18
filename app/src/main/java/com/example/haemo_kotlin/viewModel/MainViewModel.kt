package com.example.haemo_kotlin.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.theme.ColorOption
import com.example.haemo_kotlin.model.user.LoginModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.UserRepository
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val context: Context
) : ViewModel() {
    var beforeStack = MutableStateFlow("mainScreen")

    private val _mainColor = MutableStateFlow(ColorOption.BLUE)
    val mainColor: StateFlow<ColorOption> = _mainColor

    fun updateColor(colorOption: ColorOption) {
        viewModelScope.launch {
            _mainColor.emit(colorOption)
        }
    }
}