package com.chirag.petmoments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository
    val allPosts: LiveData<List<Post>>

    private val _selectedCategory = MutableLiveData<String>("All")
    val selectedCategory: LiveData<String> = _selectedCategory

    private val _filteredPosts = MutableLiveData<List<Post>>()
    val filteredPosts: LiveData<List<Post>> = _filteredPosts

    init {
        val postDao = PostDatabase.getDatabase(application).postDao()
        repository = PostRepository(postDao)
        allPosts = repository.allPosts
    }

    fun insertPost(post: Post) = viewModelScope.launch {
        repository.insert(post)
    }

    fun toggleLike(post: Post) = viewModelScope.launch {
        repository.toggleLike(post)
    }

    fun filterByCategory(category: String, allPosts: List<Post>) {
        _selectedCategory.value = category
        _filteredPosts.value = if (category == "All") {
            allPosts
        } else {
            allPosts.filter { it.petCategory == category }
        }
    }
    fun updateAllPostAvatars() = viewModelScope.launch {
        repository.updateAllAvatarsBasedOnCategory()
    }
}