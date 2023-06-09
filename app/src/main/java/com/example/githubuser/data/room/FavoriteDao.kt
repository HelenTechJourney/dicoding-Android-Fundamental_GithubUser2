package com.example.githubuser.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuser.data.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Query("SELECT* FROM favorite ORDER BY login ASC")
    fun getFavorite(): LiveData<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(favorite: FavoriteEntity)

    @Delete
    fun delete(favorite : FavoriteEntity)
}