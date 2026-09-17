package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_wallpapers")
data class FavoriteWallpaperEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val category: String,
    val imageUrl: String,
    val thumbnailUrl: String,
    val localDrawableRes: Int? = null,
    val addedAt: Long = System.currentTimeMillis()
)
