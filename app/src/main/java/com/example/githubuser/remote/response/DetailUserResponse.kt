package com.example.githubuser.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailUserResponse(

    @field:SerializedName("following_url")
    val followingUrl: String,

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("followers_url")
    val followersUrl: String,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int
) : Parcelable
