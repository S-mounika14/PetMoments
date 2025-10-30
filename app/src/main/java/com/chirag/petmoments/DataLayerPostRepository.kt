package com.chirag.petmoments

import androidx.lifecycle.LiveData
import com.chirag.petmoments.Post
import com.chirag.petmoments.PostDao

class PostRepository(private val postDao: PostDao) {

    val allPosts: LiveData<List<Post>> = postDao.getAllPosts()

    fun getPostsByCategory(category: String): LiveData<List<Post>> {
        return postDao.getPostsByCategory(category)
    }

    suspend fun insert(post: Post): Long {
        return postDao.insertPost(post)
    }

    suspend fun update(post: Post) {
        postDao.updatePost(post)
    }

    suspend fun toggleLike(post: Post) {
        val updatedPost = post.copy(
            isLiked = !post.isLiked,
            likes = if (post.isLiked) post.likes - 1 else post.likes + 1
        )
        postDao.updatePost(updatedPost)
    }
    suspend fun updateAllAvatarsBasedOnCategory() {
        val allPosts = postDao.getAllPostsSync()
        allPosts.forEach { post ->
            val correctAvatar = when (post.petCategory.lowercase()) {
                "dog" -> "🐶"
                "cat" -> "🐱"
                "bird" -> "🦜"
                "rabbit" -> "🐰"
                else -> "🐾"
            }
            val updatedPost = post.copy(userAvatar = correctAvatar)
            postDao.updatePost(updatedPost)
        }
    }
}