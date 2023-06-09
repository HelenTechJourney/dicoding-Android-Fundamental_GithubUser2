package com.example.githubuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.remote.response.ItemsItem
import com.example.githubuser.ui.adapter.UserAdapter
import com.example.githubuser.ui.viewmodel.FavoriteViewModel
import com.example.githubuser.ui.viewmodel.FavoriteViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorites"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = layoutManager
        viewModel = ViewModelProvider(this, FavoriteViewModelFactory.getInstance(this))[FavoriteViewModel::class.java]

        viewModel.getFavorite().observe(this){ it ->
            Log.d("TAG", it.toString())
            val list = it.map{ItemsItem(
                    it.following?:"",
                    it.username?:"",
                    it.followers?:"",
                    it.avatarUrl?:"",
                    it.id?.toInt()?:0)
            }
            setUserData(list)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserData(listFavorite: List<ItemsItem>) {
        val adapter = UserAdapter(listFavorite)
        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.visibility = View.VISIBLE
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(listUser: ItemsItem) {
                showSelectedUser(listUser)
            }
        })
    }

    private fun showSelectedUser(listFavorite: ItemsItem) {
        val intent = Intent(this, DetailUserActivity::class.java)

        intent.putExtra(DetailUserActivity.EXTRA_USER, listFavorite)
        startActivity(intent)
    }
}