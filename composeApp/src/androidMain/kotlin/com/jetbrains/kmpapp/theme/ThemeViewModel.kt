// Create a new file, e.g., viewmodel/ThemeViewModel.kt
package com.jetbrains.kmpapp.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Represents the theme options
enum class Theme {
    LIGHT, DARK, SYSTEM
}

class ThemeViewModel : ViewModel() {
    private val _theme = MutableStateFlow(Theme.SYSTEM)
    val theme: StateFlow<Theme> = _theme.asStateFlow()

    fun changeTheme(newTheme: Theme) {
        viewModelScope.launch {
            _theme.value = newTheme
        }
    }
}
