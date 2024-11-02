package com.pavelb.hometask.data.repositories

import com.pavelb.hometask.domain.entities.Recipe
import com.pavelb.hometask.data.remote.IRecipesRemoteDataSource
import com.pavelb.hometask.domain.mappers.RecipeResponseMapper
import com.pavelb.hometask.data.remote.NetworkResult

import javax.inject.Inject

class RemoteRecipeListRepository @Inject constructor(
    private val dataSource: IRecipesRemoteDataSource,
    private val mapper: RecipeResponseMapper
) : IRemoteRecipeListRepository {

    override suspend fun getRecipes(): NetworkResult<List<Recipe>> {
        return when (val result = dataSource.getRecipes()) {
            is NetworkResult.Success -> {
                val recipes = result.data.map { mapper.toRecipe(it) }
                NetworkResult.Success(recipes)
            }
            is NetworkResult.Error -> {
                NetworkResult.Error(result.exception)
            }
        }
    }
}