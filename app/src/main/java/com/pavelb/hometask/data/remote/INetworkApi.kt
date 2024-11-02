package com.pavelb.hometask.data.remote

import retrofit2.http.GET

interface INetworkApi {

    //we simulate a real api
    @GET("/android-test/recipes.json")
    suspend fun getRecipes(
    ): List<RecipeResponse>

}