package com.pavelb.hometask.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.pavelb.hometask.R
import com.pavelb.hometask.databinding.FragRecipeBinding
import com.pavelb.hometask.domain.encryption.BiometricDelegate
import com.pavelb.hometask.domain.encryption.BiometricDelegateImpl
import com.pavelb.hometask.domain.entities.EncryptedData
import com.pavelb.hometask.domain.entities.ErrorEntity
import com.pavelb.hometask.domain.entities.ErrorType
import com.pavelb.hometask.domain.entities.Recipe
import com.pavelb.hometask.presentation.error.ErrorDialogDelegate
import com.pavelb.hometask.presentation.error.ErrorDialogDelegateImpl
import com.pavelb.hometask.presentation.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecipeFragment : Fragment(), ErrorDialogDelegate by ErrorDialogDelegateImpl(), BiometricDelegate by BiometricDelegateImpl() {

    private lateinit var _binding: FragRecipeBinding

    private val args: RecipeFragmentArgs by navArgs()
    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragRecipeBinding.inflate(inflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val encryptedData = args.encryptedData
        authenticateAndDecrypt(encryptedData)
        initObservers()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.error.collect { errorEntity ->
                        errorEntity?.let {
                            handleErrors(errorEntity)
                        }
                    }
                }
                launch {
                    viewModel.loading.collect { isLoading ->
                        _binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    }
                }
                launch {
                    viewModel.currentRecipe.collect { recipe ->
                        recipe?.let {
                            populateRecipe(recipe)
                        }
                    }
                }
            }
        }
    }

    private fun populateRecipe(recipe: Recipe) {
        with(_binding) {
            name.text = recipe.name
            description.text = recipe.description
            carbos.text = getString(R.string.carbs_template, recipe.carbos)
            calories.text = getString(R.string.calories_template, recipe.calories)
            fats.text = getString(R.string.fats_template ,recipe.fats)
            Glide.with(requireContext())
                .load(recipe.image)
                .error(R.drawable.baseline_error_outline_24)
                .into(image)
        }
    }

    private fun authenticateAndDecrypt(encryptedData: EncryptedData) {
        val cipher = getDecryptCipher(encryptedData.iv)
        authenticate(requireActivity(), cipher) { result ->
            viewModel.decryptDataToRecipe(encryptedData, result)
        }
    }

    private fun handleErrors(errorEntity: ErrorEntity) {
        if (!isAdded) return // prevent pesky crashes when fragment is detached
        viewModel.clearError()
        when (errorEntity.type) {
            ErrorType.ENCRYPTION -> {
                showErrorDialog(
                    context = requireContext(),
                    errorMessage = errorEntity.message,
                    positiveBtn = getString(R.string.close),
                    negativeBtn = null,
                    onPositiveClick = { goBack() },
                    onNegativeClick = null
                )
            }
            else -> {
                processError(requireContext(), errorEntity)
            }

        }
    }

    private fun goBack() {
        findNavController().popBackStack()
    }
}
