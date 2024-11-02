package com.pavelb.hometask.presentation.viewmodel


import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelb.hometask.R
import com.pavelb.hometask.data.remote.NetworkResult
import com.pavelb.hometask.data.repositories.IRemoteRecipeListRepository
import com.pavelb.hometask.domain.entities.EncryptedData
import com.pavelb.hometask.domain.entities.ErrorEntity
import com.pavelb.hometask.domain.entities.ErrorType
import com.pavelb.hometask.domain.entities.Recipe
import com.pavelb.hometask.utils.StringUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteRecipeRepository: IRemoteRecipeListRepository,

    ) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    private val _error = MutableStateFlow<ErrorEntity?>(null)
    private val _loading = MutableStateFlow(false)
    private val _navigateToDetails = MutableStateFlow<EncryptedData?>(null)
    private var lastFetchTime: Long = 0

    val recipes: StateFlow<List<Recipe>> get() = _recipes
    val error: StateFlow<ErrorEntity?> get() = _error
    val loading: StateFlow<Boolean> get() = _loading
    val navigateToDetails: StateFlow<EncryptedData?> get() = _navigateToDetails

    fun fetchRecipes() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastFetchTime >= 60000) {
            lastFetchTime = currentTime  // we want to prevent reloading too often
            _loading.value = true
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    when (val result = remoteRecipeRepository.getRecipes()) {
                        is NetworkResult.Success -> _recipes.value = result.data
                        is NetworkResult.Error -> {
                            _recipes.value = emptyList()
                            _error.value =
                                ErrorEntity(
                                    result.exception.message ?: "",
                                    ErrorType.NETWORK
                                ) // in a real app we would have a mapper to map exception message to more user-friendly text
                            lastFetchTime = 0

                        }
                    }
                    _loading.value = false
                }

            }
        }
    }

    fun refreshRecipes() {
        lastFetchTime = 0
        fetchRecipes()
    }

    fun navigateToRecipeDetails(recipe: Recipe, result: BiometricPrompt.AuthenticationResult) {
        val encryptedData = encryptRecipe(recipe, result)
        _navigateToDetails.value = encryptedData
    }

    private fun encryptRecipe(
        recipe: Recipe,
        result: BiometricPrompt.AuthenticationResult,
    ): EncryptedData? {
        try {
            val encryptedData =
                result.cryptoObject?.cipher?.doFinal(recipe.toJsonString().toByteArray())
            val iv = result.cryptoObject?.cipher?.iv
            if (encryptedData != null && iv != null) {
                return (EncryptedData(encryptedData, iv))
            } else {
                _error.value = ErrorEntity(
                    StringUtils.getStringSafe(R.string.encryption_failed),
                    ErrorType.ENCRYPTION
                )
                return null
            }
        } catch (e: Exception) {
            _error.value = ErrorEntity(
                StringUtils.getStringSafe(
                    R.string.encryption_failed_template,
                    e.message
                ), ErrorType.ENCRYPTION
            )
            return null
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun postError(errorMsg: String, errorType: ErrorType) {
        _error.value = ErrorEntity(errorMsg, errorType)
    }

    fun onNavigatedToDetails() {
        _navigateToDetails.value = null
    }

}
