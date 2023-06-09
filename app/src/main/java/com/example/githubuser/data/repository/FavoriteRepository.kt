package com.example.githubuser.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.githubuser.data.entity.FavoriteEntity
import com.example.githubuser.data.room.FavoriteDao
import com.example.githubuser.data.room.FavoriteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(context: Context) {
    private val favoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getInstance(context)
        favoriteDao = db.favoriteDao()
    }

    fun getFavorite(): LiveData<List<FavoriteEntity>> = favoriteDao.getFavorite()

    fun delete(favorite: FavoriteEntity) {
        executorService.execute { favoriteDao.delete(favorite) }
    }

    fun insertFavorite(favorite: FavoriteEntity) {
        executorService.execute { favoriteDao.insertFavorite(favorite) }
    }

    companion object {
        @Volatile
        private var instance: FavoriteRepository? = null
        fun getInstance(context: Context): FavoriteRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteRepository(context)
            }.also { instance = it }
    }
}