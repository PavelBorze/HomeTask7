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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.pavelb.hometask.R
import com.pavelb.hometask.databinding.FragHomeBinding
import com.pavelb.hometask.domain.encryption.BiometricDelegate
import com.pavelb.hometask.domain.encryption.BiometricDelegateImpl
import com.pavelb.hometask.domain.entities.ErrorEntity
import com.pavelb.hometask.domain.entities.ErrorType
import com.pavelb.hometask.domain.entities.Recipe
import com.pavelb.hometask.presentation.adapters.RecipeClickListener
import com.pavelb.hometask.presentation.adapters.RecipesAdapter
import com.pavelb.hometask.presentation.error.ErrorDialogDelegate
import com.pavelb.hometask.presentation.error.ErrorDialogDelegateImpl
import com.pavelb.hometask.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), ErrorDialogDelegate by ErrorDialogDelegateImpl(), BiometricDelegate by BiometricDelegateImpl() {
    // delegate pattern is used instead of inheritance since in real life the fragments
    // will have multiple behaviors that some fragment will share and some will not

    private lateinit var _binding: FragHomeBinding
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragHomeBinding.inflate(inflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
        fetchRecipes()
    }

    private fun fetchRecipes() {
        viewModel.fetchRecipes()
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
                        _binding.loadingShroud.visibility = if (isLoading) View.VISIBLE else View.GONE
                    }
                }
                viewModel.navigateToDetails.collect { encryptedData ->
                    encryptedData?.let {
                        val action = HomeFragmentDirections.actionHomeFragmentToRecipeFragment(encryptedData)
                        findNavController().navigate(action)
                        viewModel.onNavigatedToDetails()
                    }
                }
            }
        }
    }

    private fun handleErrors(errorEntity: ErrorEntity) {
        if (!isAdded) return // prevent pesky crashes when fragment is detached
        viewModel.clearError()
        when (errorEntity.type) {
            ErrorType.NETWORK -> {
                showErrorDialog(
                    context = requireContext(),
                    errorMessage = errorEntity.message,
                    positiveBtn = getString(R.string.retry),
                    negativeBtn = getString(R.string.close),
                    onPositiveClick = viewModel::refreshRecipes,
                    onNegativeClick = null
                )
            }
            else -> {
                processError(requireContext(), errorEntity)
            }

        }
    }

    private fun initViews() {
        val adapter = RecipesAdapter(emptyList(), createItemClickListener())
        with(_binding) {
            with(recyclerView) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                itemAnimator = DefaultItemAnimator()
                this.adapter = adapter
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipes.collect { recipes ->
                    adapter.setItems(recipes)
                }
            }
        }
    }

    private fun createItemClickListener(): RecipeClickListener {
        return object : RecipeClickListener {
            override fun onRecipeClicked(recipe: Recipe) {
                val cipher = getCipher()
                try {
                    authenticate(requireActivity(), cipher) { result ->
                        viewModel.navigateToRecipeDetails(
                            recipe, result)
                    }
                } catch (e: Exception) {
                    viewModel.postError(getString(R.string.biometric_auth_error_template, e.message), ErrorType.AUTH)
                }
            }
        }
    }
}