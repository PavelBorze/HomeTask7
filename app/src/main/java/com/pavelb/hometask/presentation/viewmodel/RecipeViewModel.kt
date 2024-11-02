package com.pavelb.hometask.presentation.viewmodel

import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelb.hometask.R
import com.pavelb.hometask.domain.entities.EncryptedData
import com.pavelb.hometask.domain.entities.ErrorEntity
import com.pavelb.hometask.domain.entities.ErrorType
import com.pavelb.hometask.domain.entities.Recipe
import com.pavelb.hometask.utils.StringUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor() : ViewModel() {

    private val _currentRecipe = MutableStateFlow<Recipe?>(null)
    private val _error = MutableStateFlow<ErrorEntity?>(null)
    private val _loading = MutableStateFlow(false)

    val currentRecipe: StateFlow<Recipe?> get() = _currentRecipe
    val error: StateFlow<ErrorEntity?> get() = _error
    val loading: StateFlow<Boolean> get() = _loading

    fun decryptDataToRecipe(
        encryptedData: EncryptedData,
        result: BiometricPrompt.AuthenticationResult
    ) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val cipher =
                    result.cryptoObject?.cipher ?: throw IllegalStateException("Cipher is null")
                val decryptedBytes = cipher.doFinal(encryptedData.encryptedData)
                val decryptedRecipe = Recipe.fromJsonString(String(decryptedBytes, Charsets.UTF_8))
                _currentRecipe.value = decryptedRecipe
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = ErrorEntity(
                    StringUtils.getStringSafe(
                        R.string.decryption_failed_template,
                        e.message
                    ), ErrorType.ENCRYPTION
                )
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}