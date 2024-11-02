package com.pavelb.hometask.data.remote

import java.io.IOException
import javax.inject.Inject

class RecipesRemoteDataSource @Inject constructor(
    private val networkApi:INetworkApi):IRecipesRemoteDataSource {

    override suspend fun getRecipes(): NetworkResult<List<RecipeResponse>> {
        return try {
            val response = networkApi.getRecipes()
            NetworkResult.Success(response)
        } catch (e: IOException) {
            NetworkResult.Error(e)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}