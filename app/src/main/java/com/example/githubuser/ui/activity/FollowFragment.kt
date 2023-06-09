package com.example.githubuser.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.ui.adapter.UserAdapter
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.remote.response.ItemsItem
import com.example.githubuser.ui.viewmodel.DetailViewModel

class FollowFragment : Fragment() {
    companion object {
        const val SECTION_NUMBER = "section_number"
        const val SECTION_USERNAME = "username"
    }

    private lateinit var binding: FragmentFollowBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var username = arguments?.getString(SECTION_USERNAME)
        var number = 0

        Log.d("arguments: number", number.toString())
        Log.d("arguments: username", username.toString())

        detailViewModel =
            ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
                DetailViewModel::class.java
            )
        arguments?.let {
            number = it.getInt(SECTION_NUMBER)
            username = it.getString(SECTION_USERNAME)
        }

        if (number == 1) {
            showLoading(true)
            username?.let { detailViewModel.getFollowers(it) }
            detailViewModel.followers.observe(viewLifecycleOwner) {
                setFollow(it)
                showLoading(false)
            }
        } else {
            showLoading(true)
            username?.let { detailViewModel.getFollowing(it) }
            detailViewModel.following.observe(viewLifecycleOwner) {
                setFollow(it)
                showLoading(false)
            }
        }
    }

    private fun setFollow(Items: List<ItemsItem>?) {
        binding.apply {
            binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = Items?.let { UserAdapter(it) }
            binding.rvFollow.adapter = adapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}