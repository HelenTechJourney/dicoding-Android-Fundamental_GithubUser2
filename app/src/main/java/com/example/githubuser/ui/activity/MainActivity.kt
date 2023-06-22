package com.example.githubuser.ui.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.SettingPreferences
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.remote.response.ItemsItem
import com.example.githubuser.ui.adapter.UserAdapter
import com.example.githubuser.ui.viewmodel.MainViewModel
import com.example.githubuser.ui.viewmodel.SettingViewModel
import com.example.githubuser.ui.viewmodel.ViewModelFactory

internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val layoutManager = LinearLayoutManager(this)
        binding.rvGithub.layoutManager = layoutManager
        setContentView(binding.root)

        viewModel.listUser.observe(this) { listUser -> setUserData(listUser) }
        viewModel.isLoading.observe(this) {
            showLoading(it)

            val pref = SettingPreferences.getInstance(dataStore)
            val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref))[SettingViewModel::class.java]
            settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search)?.actionView as androidx.appcompat.widget.SearchView

        searchView.clearFocus()

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val i = Intent(this, FavoriteActivity::class.java)
                startActivity(i)
                true
            }
            R.id.setting -> {
                val i = Intent(this, SettingActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
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
