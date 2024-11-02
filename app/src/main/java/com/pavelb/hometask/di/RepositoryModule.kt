package com.pavelb.hometask.di

import com.pavelb.hometask.data.remote.INetworkApi
import com.pavelb.hometask.data.remote.IRecipesRemoteDataSource
import com.pavelb.hometask.data.remote.RecipesRemoteDataSource
import com.pavelb.hometask.data.repositories.IRemoteRecipeListRepository
import com.pavelb.hometask.data.repositories.RemoteRecipeListRepository
import com.pavelb.hometask.domain.mappers.RecipeResponseMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Provides
    @Singleton
    fun providesNetworkAPI(
        @NetworkAPIClient retrofit: Retrofit
    ): INetworkApi {
        return retrofit.create(INetworkApi::class.java)
    }



    @Provides
    @Singleton
    fun providesRecipesRemoteDataSource(
        networkAPI: INetworkApi
    ): IRecipesRemoteDataSource {
        return RecipesRemoteDataSource(networkAPI)
    }


    @Provides
    @Singleton
    fun providesRecipeResponseMapper(): RecipeResponseMapper {
        return RecipeResponseMapper()
    }


    @Provides
    @Singleton
    fun providesRecipeListRepository(
       dataSource: IRecipesRemoteDataSource,
       mapper: RecipeResponseMapper
    ): IRemoteRecipeListRepository {
        return RemoteRecipeListRepository(dataSource, mapper)
    }



}