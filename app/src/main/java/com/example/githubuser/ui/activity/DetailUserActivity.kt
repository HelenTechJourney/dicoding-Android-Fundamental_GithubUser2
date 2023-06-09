package com.example.githubuser.ui.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.entity.FavoriteEntity
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.example.githubuser.remote.response.DetailUserResponse
import com.example.githubuser.remote.response.ItemsItem
import com.example.githubuser.ui.adapter.SectionsPagerAdapter
import com.example.githubuser.ui.viewmodel.DetailViewModel
import com.example.githubuser.ui.viewmodel.FavoriteViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailViewModel
    private var isFavorite: Boolean = false
    private var userFavorite: FavoriteEntity = FavoriteEntity()

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_DETAIL = intArrayOf(
            R.string.tab_follower,
            R.string.tab_following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, FavoriteViewModelFactory.getInstance(this))[DetailViewModel::class.java]

        val detailUser = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_USER, ItemsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_USER)
        }
        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.username = detailUser?.login
        viewModel.detailUser.observe(this) { userData ->
            setDetailData(userData)
            viewModel.getFavorite().observe(this) {
                userFavorite = FavoriteEntity(userData.login, userData.avatarUrl)
                isFavorite = it.contains(userFavorite)
                setFavorite()
            }
        }
        viewModel.isLoading.observe(this) { showLoading(it) }

        binding.fabFavorite.setOnClickListener {
            if (isFavorite) {
                viewModel.delete(userFavorite)
                isFavorite = false
                setFavorite()
                Toast.makeText(this, "Dihapus dari Favorite", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.insertFavorite(userFavorite)
                isFavorite = true
                setFavorite()
                Toast.makeText(this, "Ditambahkan ke Favorite", Toast.LENGTH_SHORT).show()
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_DETAIL[position])
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private fun setDetailData(detailUser: DetailUserResponse?) {
        binding.tvItemName.text = detailUser?.name
        binding.tvItemUsername.text = detailUser?.login
        Glide.with(this)
            .load(detailUser?.avatarUrl)
            .into(binding.imgItemPhoto)
        binding.tvItemFollower.text = "${detailUser?.followers} Followers"
        binding.tvItemFollowing.text = "${detailUser?.following} Following"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setFavorite() {
        if (isFavorite) {
            binding.fabFavorite.setImageDrawable(resources.getDrawable(R.drawable.favorite_full))
        } else {
            binding.fabFavorite.setImageDrawable(resources.getDrawable(R.drawable.favorite_border))
        }
    }
}