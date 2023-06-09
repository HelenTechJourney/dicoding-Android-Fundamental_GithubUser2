package com.example.githubuser.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionsPagerAdapter
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.example.githubuser.response.DetailUserResponse
import com.example.githubuser.response.ItemsItem
import com.example.githubuser.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val viewModel: DetailViewModel by viewModels()

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
        setContentView(R.layout.activity_detail_user)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailUser = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_USER, ItemsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_USER)
        }

        viewModel.username = detailUser?.login

//        viewModel.DetailViewModel().observe(this, { detailUser -> setDetailData(detailUser) })

        viewModel.detailUser.observe(this, { userData -> setDetailData(userData) })
        viewModel.isLoading.observe(this) { showLoading(it) }

            val sectionsPagerAdapter = SectionsPagerAdapter(this)
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_DETAIL[position])
            }.attach()

            supportActionBar?.elevation = 0f
    }

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
}