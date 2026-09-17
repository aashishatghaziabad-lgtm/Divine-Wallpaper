package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_wallpapers ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteWallpaperEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_wallpapers WHERE id = :id)")
    fun isFavorite(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(entity: FavoriteWallpaperEntity)

    @Query("DELETE FROM favorite_wallpapers WHERE id = :id")
    suspend fun deleteFavorite(id: String)
}
