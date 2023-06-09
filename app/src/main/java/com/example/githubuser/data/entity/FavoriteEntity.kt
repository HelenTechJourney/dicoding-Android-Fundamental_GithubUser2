package com.example.githubuser.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "login")
    var username: String = "",

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null,

    @ColumnInfo(name = "followers_url")
    var followers: String? = null,

    @ColumnInfo(name = "following_url")
    var following: String? = null,

    @ColumnInfo(name = "id")
    var id: String? = null,
)

