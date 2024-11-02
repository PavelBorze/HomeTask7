package com.pavelb.hometask.domain.entities

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.Gson

@Parcelize
data class Recipe(
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
) : Parcelable {

    fun toJsonString(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun fromJsonString(jsonString: String): Recipe {
            return Gson().fromJson(jsonString, Recipe::class.java)
        }
    }

}