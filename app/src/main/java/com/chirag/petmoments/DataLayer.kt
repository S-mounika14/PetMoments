package com.chirag.petmoments

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val userAvatar: String,
    val imagePath: String,
    val caption: String,
    val hashtags: String,
    val petCategory: String,
    val likes: Int = 0,
    val isLiked: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)