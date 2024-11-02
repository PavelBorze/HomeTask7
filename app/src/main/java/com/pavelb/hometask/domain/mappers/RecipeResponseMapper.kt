package com.pavelb.hometask.domain.mappers

import com.pavelb.hometask.data.remote.RecipeResponse
import com.pavelb.hometask.domain.entities.Recipe

class RecipeResponseMapper {
    fun toRecipe(response: RecipeResponse): Recipe {
        return Recipe(
            id = response.id,
            name = response.name,
            description = response.description,
            calories = response.calories,
            carbos = response.carbos,
            country = response.country?:"Unknown",
            difficulty = response.difficulty,
            fats = response.fats,
            headline = response.headline,
            image = response.image,
            proteins = response.proteins,
            thumb = response.thumb,
            time = response.time
        )
    }
}