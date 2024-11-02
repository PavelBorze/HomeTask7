package com.pavelb.hometask.data.repositories

import com.pavelb.hometask.data.remote.NetworkResult
import com.pavelb.hometask.domain.entities.Recipe


interface IRemoteRecipeListRepository {

    suspend fun getRecipes(): NetworkResult<List<Recipe>>


}
