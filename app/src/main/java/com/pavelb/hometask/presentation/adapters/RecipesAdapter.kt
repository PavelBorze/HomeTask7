package com.pavelb.hometask.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pavelb.hometask.R
import com.pavelb.hometask.databinding.ItemRecipeBinding
import com.pavelb.hometask.domain.entities.Recipe
import com.pavelb.hometask.presentation.error.ErrorDialogDelegate
import com.pavelb.hometask.presentation.error.ErrorDialogDelegateImpl

class RecipesAdapter(
    private var recipes: List<Recipe> = emptyList(),
    private val itemClickListener: RecipeClickListener
) : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>(),
    ErrorDialogDelegate by ErrorDialogDelegateImpl() {

    fun setItems(newRecipes: List<Recipe>) {
        //using diffUtil instead of notifyDataSetChanged to prevent flickering if the list didn't change
        val diffCallback = RecipesDiffCallback(recipes, newRecipes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        recipes = newRecipes
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size

    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            Glide.with(binding.thumb.context)
                .load(recipe.thumb)
                .error(R.drawable.baseline_error_outline_24)
                .into(binding.thumb)
            binding.name.text = recipe.name
            binding.fats.text = binding.root.context.getString(R.string.fats_template, recipe.fats)
            binding.carbos.text = binding.root.context.getString(R.string.carbs_template, recipe.carbos)
            binding.calories.text =
                binding.root.context.getString(R.string.calories_template, recipe.calories)
            binding.root.setOnClickListener {
                itemClickListener.onRecipeClicked(recipe)
            }
        }
    }

    class RecipesDiffCallback(
        private val oldList: List<Recipe>,
        private val newList: List<Recipe>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}

interface RecipeClickListener {
    fun onRecipeClicked(recipe: Recipe)
}