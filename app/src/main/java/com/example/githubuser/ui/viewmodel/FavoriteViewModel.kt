package com.example.githubuser.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.entity.FavoriteEntity
import com.example.githubuser.data.repository.FavoriteRepository
import com.example.githubuser.remote.response.ItemsItem

class FavoriteViewModel (context: Context) : ViewModel() {

    private val _listFavorite = MutableLiveData<List<ItemsItem>>()
    val listFavorite: LiveData<List<ItemsItem>> = _listFavorite

    private val favoriteRepository: FavoriteRepository = FavoriteRepository.getInstance(context)

    fun getFavorite(): LiveData<List<FavoriteEntity>> = favoriteRepository.getFavorite()

    fun delete(favorite: FavoriteEntity) {
        favoriteRepository.delete(favorite)
    }

    fun insertFavorite(favorite: FavoriteEntity) {
        favoriteRepository.insertFavorite(favorite)
    }
}