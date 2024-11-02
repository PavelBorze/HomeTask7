package com.pavelb.hometask.data.remote

data class RecipeResponse(
    val id: String,
    val name: String,
    val description: String,
    val calories: String,
    val carbos: String,
    val country: String,
    val difficulty: Int,
    val fats: String,
    val headline: String,
    val image: String,
    val proteins: String,
    val thumb: String,
    val time: String
    )
