package com.pavelb.hometask.data.remote

interface IRecipesRemoteDataSource {

    suspend fun getRecipes(): NetworkResult<List<RecipeResponse>>

}