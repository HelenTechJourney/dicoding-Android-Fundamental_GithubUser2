package com.example.githubuser.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.entity.FavoriteEntity
import com.example.githubuser.data.repository.FavoriteRepository
import com.example.githubuser.remote.response.DetailUserResponse
import com.example.githubuser.remote.response.ItemsItem
import com.example.githubuser.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel (context: Context): ViewModel() {

    private val favoriteRepository: FavoriteRepository = FavoriteRepository.getInstance(context)

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers: LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following: LiveData<List<ItemsItem>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "DetailViewModel"
    }

    var username : String? = ""
        set(value) {
            field= value
            if (value !=  null) {
                findDetail(value)
                getFollowers(value)
                getFollowing(value)
            }
        }


    private fun findDetail(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}") }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    fun getFollowers(query: String ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(query)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followers.value = response.body()
                } else {
                    Log.d("MainViewModel", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowing(query: String ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(query)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _following.value = response.body()
                } else {
                    Log.d("MainViewModel", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }
    fun getFavorite(): LiveData<List<FavoriteEntity>> = favoriteRepository.getFavorite()

    fun delete(favorite: FavoriteEntity) {
        favoriteRepository.delete(favorite)
    }

    fun insertFavorite(favorite: FavoriteEntity) {
        favoriteRepository.insertFavorite(favorite)
    }
}
