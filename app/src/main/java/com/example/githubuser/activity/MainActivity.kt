package com.example.githubuser.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.response.ItemsItem
import com.example.githubuser.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val layoutManager = LinearLayoutManager(this)
        binding.rvGithub.layoutManager = layoutManager
        setContentView(binding.root)

        viewModel.listUser.observe(this, { listUser -> setUserData(listUser) })
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search)?.actionView as SearchView

        searchView.clearFocus()

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                val mainViewModel = null
                viewModel.findUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun showSelectedUser(detailUser: ItemsItem) {
        val intent = Intent(this, DetailUserActivity::class.java)

        intent.putExtra(DetailUserActivity.EXTRA_USER, detailUser)
        startActivity(intent)
    }

    private fun setUserData(items: List<ItemsItem>) {

        val adapter = UserAdapter(items)
        binding.rvGithub.adapter = adapter
        binding.rvGithub.visibility = View.VISIBLE
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(listUser: ItemsItem) {
                showSelectedUser(listUser)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}

